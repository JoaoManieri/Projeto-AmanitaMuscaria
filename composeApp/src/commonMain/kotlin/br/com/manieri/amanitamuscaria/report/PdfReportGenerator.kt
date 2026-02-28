package br.com.manieri.amanitamuscaria.report

import androidx.compose.runtime.Composable
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.WorkshopSettings

data class ReportGenerationResult(
    val success: Boolean,
    val message: String,
    val filePath: String? = null,
)

interface PdfReportGenerator {
    fun generateServiceReport(
        service: Service,
        settings: WorkshopSettings,
    ): ReportGenerationResult
}

@Composable
expect fun rememberPdfReportGenerator(): PdfReportGenerator
