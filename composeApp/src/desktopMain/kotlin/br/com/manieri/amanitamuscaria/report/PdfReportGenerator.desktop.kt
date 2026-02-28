package br.com.manieri.amanitamuscaria.report

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.WorkshopSettings

private class DesktopPdfReportGenerator : PdfReportGenerator {
    override fun generateServiceReport(
        service: Service,
        settings: WorkshopSettings,
    ): ReportGenerationResult {
        return ReportGenerationResult(
            success = false,
            message = "Geracao de PDF ainda nao implementada no Desktop",
        )
    }
}

@Composable
actual fun rememberPdfReportGenerator(): PdfReportGenerator {
    return remember {
        DesktopPdfReportGenerator()
    }
}
