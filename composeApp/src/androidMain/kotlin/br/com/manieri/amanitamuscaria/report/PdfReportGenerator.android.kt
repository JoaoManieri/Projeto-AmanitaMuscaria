package br.com.manieri.amanitamuscaria.report

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import br.com.manieri.amanitamuscaria.model.InspectionPhoto
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.WorkshopSettings
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private class AndroidPdfReportGenerator(
    private val context: Context,
) : PdfReportGenerator {

    override fun generateServiceReport(
        service: Service,
        settings: WorkshopSettings,
    ): ReportGenerationResult {
        return runCatching {
            val reportsDir = File(context.filesDir, "reports").apply { mkdirs() }
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "relatorio_${service.plate}_${timestamp}.pdf"
                .replace("/", "_")
                .replace("\\", "_")
            val outputFile = File(reportsDir, fileName)

            createPdf(outputFile, service, settings)
            val opened = openPdf(outputFile)
            val feedback = if (opened) {
                "PDF gerado e aberto: ${outputFile.name}"
            } else {
                "PDF gerado: ${outputFile.name} (nao foi possivel abrir automaticamente)"
            }

            ReportGenerationResult(
                success = true,
                message = feedback,
                filePath = outputFile.absolutePath,
            )
        }.getOrElse { error ->
            ReportGenerationResult(
                success = false,
                message = "Falha ao gerar PDF: ${error.message ?: "erro desconhecido"}",
            )
        }
    }

    private fun createPdf(
        outputFile: File,
        service: Service,
        settings: WorkshopSettings,
    ) {
        val document = PdfDocument()
        val pageWidth = 595
        val pageHeight = 842
        val margin = 36f

        var pageNumber = 1
        var page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
        var canvas = page.canvas
        var cursorY = margin

        val titlePaint = Paint().apply {
            color = Color.BLACK
            textSize = 18f
            isFakeBoldText = true
        }
        val headingPaint = Paint().apply {
            color = Color.BLACK
            textSize = 13f
            isFakeBoldText = true
        }
        val bodyPaint = Paint().apply {
            color = Color.DKGRAY
            textSize = 11f
        }
        val mutedPaint = Paint().apply {
            color = Color.GRAY
            textSize = 10f
        }
        val linePaint = Paint().apply {
            color = Color.LTGRAY
            strokeWidth = 1.5f
        }

        fun nextPage() {
            document.finishPage(page)
            pageNumber += 1
            page = document.startPage(PdfDocument.PageInfo.Builder(pageWidth, pageHeight, pageNumber).create())
            canvas = page.canvas
            cursorY = margin
        }

        fun ensureSpace(required: Float) {
            if (cursorY + required > pageHeight - margin) {
                nextPage()
            }
        }

        fun drawWrappedText(label: String, text: String, maxWidth: Float) {
            ensureSpace(20f)
            canvas.drawText(label, margin, cursorY, headingPaint)
            cursorY += 14f
            val words = text.ifBlank { "-" }.split(" ")
            var line = ""
            for (word in words) {
                val candidate = if (line.isEmpty()) word else "$line $word"
                if (bodyPaint.measureText(candidate) <= maxWidth) {
                    line = candidate
                } else {
                    ensureSpace(14f)
                    canvas.drawText(line, margin, cursorY, bodyPaint)
                    cursorY += 14f
                    line = word
                }
            }
            if (line.isNotEmpty()) {
                ensureSpace(16f)
                canvas.drawText(line, margin, cursorY, bodyPaint)
                cursorY += 18f
            }
        }

        ensureSpace(60f)
        canvas.drawText(settings.reportHeader.ifBlank { "Relatorio de Servico" }, margin, cursorY, titlePaint)
        cursorY += 18f
        canvas.drawText(settings.workshopName, margin, cursorY, bodyPaint)
        cursorY += 14f
        canvas.drawText("Gerado em: ${SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US).format(Date())}", margin, cursorY, mutedPaint)
        cursorY += 12f
        canvas.drawLine(margin, cursorY, pageWidth - margin, cursorY, linePaint)
        cursorY += 18f

        drawWrappedText("Atendimento", "Placa: ${service.plate} | Status: ${service.status.name}", pageWidth - (margin * 2))
        drawWrappedText("Veiculo", "${service.vehicle.brand} ${service.vehicle.model}, ano ${service.vehicle.year}, cor ${service.vehicle.color}, ${service.vehicle.mileage} km", pageWidth - (margin * 2))
        drawWrappedText("Cliente", "${service.client.name} | ${service.client.phone} | ${service.client.email ?: "-"}", pageWidth - (margin * 2))
        drawWrappedText("Datas", "Entrada: ${service.entryDateLabel} | Saida: ${service.exitDateLabel ?: "-"}", pageWidth - (margin * 2))
        drawWrappedText("Observacoes", service.observations, pageWidth - (margin * 2))

        ensureSpace(24f)
        canvas.drawText("Fotos da inspeção (${service.inspectionPhotos.size})", margin, cursorY, headingPaint)
        cursorY += 16f

        renderPhotos(
            photos = service.inspectionPhotos,
            pageWidth = pageWidth,
            pageHeight = pageHeight,
            margin = margin,
            bodyPaint = bodyPaint,
            ensureSpace = ::ensureSpace,
            nextPage = ::nextPage,
            getCursorY = { cursorY },
            setCursorY = { cursorY = it },
            canvasProvider = { canvas },
        )

        ensureSpace(30f)
        val signatureText = if (service.signature.isNullOrBlank()) "Sem assinatura registrada" else "Assinatura capturada"
        canvas.drawText("Assinatura: $signatureText", margin, cursorY, bodyPaint)

        document.finishPage(page)
        FileOutputStream(outputFile).use { out -> document.writeTo(out) }
        document.close()
    }

    private fun renderPhotos(
        photos: List<InspectionPhoto>,
        pageWidth: Int,
        pageHeight: Int,
        margin: Float,
        bodyPaint: Paint,
        ensureSpace: (Float) -> Unit,
        nextPage: () -> Unit,
        getCursorY: () -> Float,
        setCursorY: (Float) -> Unit,
        canvasProvider: () -> android.graphics.Canvas,
    ) {
        if (photos.isEmpty()) {
            ensureSpace(16f)
            canvasProvider().drawText("Nenhuma foto registrada", margin, getCursorY(), bodyPaint)
            setCursorY(getCursorY() + 18f)
            return
        }

        val cellWidth = (pageWidth - (margin * 2) - 12f) / 2f
        val imageHeight = 120f

        photos.chunked(2).forEach { row ->
            val blockHeight = imageHeight + 28f
            if (getCursorY() + blockHeight > pageHeight - margin) {
                nextPage()
            }
            val top = getCursorY()
            row.forEachIndexed { index, photo ->
                val left = margin + (index * (cellWidth + 12f))
                val bitmap = photo.bytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
                if (bitmap != null) {
                    val target = android.graphics.RectF(left, top, left + cellWidth, top + imageHeight)
                    canvasProvider().drawBitmap(bitmap, null, target, null)
                } else {
                    canvasProvider().drawRect(left, top, left + cellWidth, top + imageHeight, Paint().apply { color = Color.LTGRAY })
                    canvasProvider().drawText("Sem preview", left + 8f, top + 18f, bodyPaint)
                }
                canvasProvider().drawText(
                    "${photo.region} - ${photo.timestampLabel}",
                    left,
                    top + imageHeight + 14f,
                    bodyPaint,
                )
            }
            setCursorY(top + blockHeight)
        }
    }

    private fun openPdf(file: File): Boolean {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file,
        )
        val viewIntent = Intent(Intent.ACTION_VIEW).apply {
            setDataAndType(uri, "application/pdf")
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        return runCatching {
            context.startActivity(viewIntent)
            true
        }.getOrElse { error ->
            if (error is ActivityNotFoundException) {
                false
            } else {
                throw error
            }
        }
    }
}

@Composable
actual fun rememberPdfReportGenerator(): PdfReportGenerator {
    val context = LocalContext.current
    return remember(context) {
        AndroidPdfReportGenerator(context.applicationContext)
    }
}
