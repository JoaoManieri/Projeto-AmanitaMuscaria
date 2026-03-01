package br.com.manieri.amanitamuscaria.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.model.InspectionPhoto
import br.com.manieri.amanitamuscaria.platform.PlatformPhotoPreview
import br.com.manieri.amanitamuscaria.report.ReportGenerationResult
import br.com.manieri.amanitamuscaria.ui.components.StatusBadge
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun HistoryScreen(
    services: List<Service>,
    onGenerateReport: (Service) -> ReportGenerationResult,
) {
    val tokens = LocalAutoCheckTokens.current
    var plateFilter by rememberSaveable { mutableStateOf("") }
    var clientFilter by rememberSaveable { mutableStateOf("") }
    var statusFilterName by rememberSaveable { mutableStateOf(HistoryStatusFilter.ALL.name) }
    var dialogServiceId by rememberSaveable { mutableStateOf<String?>(null) }
    var reportFeedback by rememberSaveable { mutableStateOf<String?>(null) }
    val statusFilter = HistoryStatusFilter.entries.firstOrNull { it.name == statusFilterName } ?: HistoryStatusFilter.ALL

    val filtered = services.filter { service ->
        val byPlate = plateFilter.isBlank() || service.plate.contains(plateFilter.trim(), ignoreCase = true)
        val byClient = clientFilter.isBlank() || service.client.name.contains(clientFilter.trim(), ignoreCase = true)
        val byStatus = matchesStatusFilter(service.status, statusFilter)
        byPlate && byClient && byStatus
    }
    val dialogService = services.firstOrNull { it.id == dialogServiceId }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Box(
                modifier = Modifier
                    .background(Color(0xFFDBEAFE), RoundedCornerShape(10.dp))
                    .padding(10.dp),
            ) {
                androidx.compose.material3.Icon(Icons.Outlined.History, contentDescription = null, tint = tokens.sidebarAccent)
            }
            Column {
                Text("Historico de Atendimentos", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                Text("Visualize e gerencie todos os atendimentos realizados", color = tokens.textSecondary)
            }
        }

        Spacer(Modifier.height(16.dp))
        FilterCard(
            plateFilter = plateFilter,
            clientFilter = clientFilter,
            statusFilter = statusFilter,
            onPlateChange = { plateFilter = it },
            onClientChange = { clientFilter = it },
            onStatusChange = { selected -> statusFilterName = selected.name },
            onClearFilters = {
                plateFilter = ""
                clientFilter = ""
                statusFilterName = HistoryStatusFilter.ALL.name
            },
        )

        Spacer(Modifier.height(16.dp))
        reportFeedback?.let { feedback ->
            Text(
                text = feedback,
                color = if (feedback.startsWith("PDF gerado")) Color(0xFF166534) else Color(0xFFB91C1C),
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(12.dp))
        }

        HistoryTable(
            services = filtered,
            onOpenService = { service ->
                dialogServiceId = service.id
            },
        )
    }

    dialogService?.let { service ->
        ServiceDetailsDialog(
            service = service,
            onDismiss = { dialogServiceId = null },
            onGenerateReport = {
                val result = onGenerateReport(service)
                reportFeedback = if (result.success) {
                    "${result.message} (${result.filePath ?: "-"})"
                } else {
                    result.message
                }
            },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FilterCard(
    plateFilter: String,
    clientFilter: String,
    statusFilter: HistoryStatusFilter,
    onPlateChange: (String) -> Unit,
    onClientChange: (String) -> Unit,
    onStatusChange: (HistoryStatusFilter) -> Unit,
    onClearFilters: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    var statusExpanded by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(tokens.radiusLg))
            .border(1.dp, tokens.border, RoundedCornerShape(tokens.radiusLg))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedTextField(
                modifier = Modifier.width(260.dp),
                value = plateFilter,
                onValueChange = onPlateChange,
                singleLine = true,
                label = { Text("Buscar por placa") },
            )
            OutlinedTextField(
                modifier = Modifier.width(260.dp),
                value = clientFilter,
                onValueChange = onClientChange,
                singleLine = true,
                label = { Text("Buscar por cliente") },
            )
            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
                modifier = Modifier.width(320.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    value = statusFilter.label,
                    onValueChange = { },
                    readOnly = true,
                    singleLine = true,
                    label = { Text("Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false },
                ) {
                    HistoryStatusFilter.entries.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option.label) },
                            onClick = {
                                onStatusChange(option)
                                statusExpanded = false
                            },
                        )
                    }
                }
            }
            Button(
                onClick = onClearFilters,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = tokens.textPrimary,
                ),
            ) {
                Text("Limpar")
            }
        }
    }
}

@Composable
private fun HistoryTable(
    services: List<Service>,
    onOpenService: (Service) -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(tokens.radiusLg))
            .border(1.dp, tokens.border, RoundedCornerShape(tokens.radiusLg)),
    ) {
        HeaderRow()
        if (services.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(30.dp), contentAlignment = Alignment.Center) {
                Text("Nenhum atendimento encontrado", color = tokens.textSecondary)
            }
        } else {
            services.forEach { service ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(0.5.dp, Color(0xFFF3F4F6))
                        .clickable { onOpenService(service) }
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Cell(service.entryDateLabel, Modifier.weight(1.2f))
                    Cell(service.plate, Modifier.weight(1f), bold = true)
                    Cell(service.client.name, Modifier.weight(1.6f))
                    Cell("${service.vehicle.brand} ${service.vehicle.model}", Modifier.weight(1.9f))
                    Box(modifier = Modifier.weight(1.3f)) { StatusBadge(service.status) }
                }
            }
        }
    }
}

@Composable
private fun HeaderRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9FAFB))
            .padding(horizontal = 12.dp, vertical = 10.dp),
    ) {
        HeaderCell("Data", Modifier.weight(1.2f))
        HeaderCell("Placa", Modifier.weight(1f))
        HeaderCell("Cliente", Modifier.weight(1.6f))
        HeaderCell("Veiculo", Modifier.weight(1.9f))
        HeaderCell("Status", Modifier.weight(1.3f))
    }
}

@Composable
private fun HeaderCell(text: String, modifier: Modifier, right: Boolean = false) {
    Box(modifier = modifier, contentAlignment = if (right) Alignment.CenterEnd else Alignment.CenterStart) {
        Text(
            text,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
private fun Cell(text: String, modifier: Modifier, bold: Boolean = false) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        color = Color(0xFF111827),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
}

@Composable
private fun ServiceDetailsDialog(
    service: Service,
    onDismiss: () -> Unit,
    onGenerateReport: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    var selectedPhoto by remember { mutableStateOf<InspectionPhoto?>(null) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 980.dp)
                .heightIn(max = 820.dp),
            shape = RoundedCornerShape(tokens.radiusLg),
            color = Color.White,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF8FAFC))
                        .padding(horizontal = 20.dp, vertical = 14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = service.plate,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = tokens.textPrimary,
                        )
                        StatusBadge(service.status)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            onClick = onGenerateReport,
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary),
                        ) {
                            androidx.compose.material3.Icon(Icons.Outlined.FileDownload, contentDescription = null)
                            Text("Baixar")
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E7EB)),
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 700.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    TimelineCard(service = service)

                    SectionHeader(icon = Icons.Outlined.DirectionsCar, title = "Dados do Veiculo")
                    InfoGrid {
                        InfoCell("Marca", service.vehicle.brand)
                        InfoCell("Modelo", service.vehicle.model)
                        InfoCell("Ano", service.vehicle.year.toString())
                        InfoCell("Cor", service.vehicle.color)
                        InfoCell("Quilometragem", "${service.vehicle.mileage} km")
                        InfoCell("Placa", service.vehicle.plate)
                    }

                    SectionHeader(icon = Icons.Outlined.Person, title = "Dados do Cliente")
                    InfoGrid {
                        InfoCell("Nome", service.client.name)
                        InfoCell("Telefone", service.client.phone)
                        service.client.email?.let { InfoCell("Email", it) }
                        service.client.document?.let { InfoCell("Documento", it) }
                    }

                    SectionHeader(icon = Icons.Outlined.Description, title = "Observacoes")
                    Surface(
                        color = Color(0xFFF9FAFB),
                        shape = RoundedCornerShape(12.dp),
                    ) {
                        Text(
                            text = service.observations.ifBlank { "Nenhuma observacao registrada." },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            color = tokens.textPrimary,
                        )
                    }

                    SectionHeader(
                        icon = Icons.Outlined.CameraAlt,
                        title = "Fotos relacionadas (${service.inspectionPhotos.size})",
                    )
                    if (service.inspectionPhotos.isNotEmpty()) {
                        PhotosGrid(
                            service = service,
                            onPhotoClick = { photo -> selectedPhoto = photo },
                        )
                    } else {
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = Color(0xFFF9FAFB),
                            shape = RoundedCornerShape(12.dp),
                        ) {
                            Text(
                                text = "Nenhuma foto relacionada a este atendimento.",
                                modifier = Modifier.padding(12.dp),
                                color = Color(0xFF6B7280),
                            )
                        }
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.End,
                    ) {
                        TextButton(onClick = onDismiss) {
                            Text("Fechar")
                        }
                    }
                }
            }
        }
    }

    selectedPhoto?.let { photo ->
        PhotoPreviewDialog(
            photo = photo,
            onDismiss = { selectedPhoto = null },
        )
    }
}

private fun matchesStatusFilter(status: ServiceStatus, filter: HistoryStatusFilter): Boolean {
    return when (filter) {
        HistoryStatusFilter.ALL -> true
        HistoryStatusFilter.IN_PROGRESS -> status == ServiceStatus.IN_PROGRESS
        HistoryStatusFilter.WAITING_PICKUP -> status == ServiceStatus.WAITING_PICKUP
        HistoryStatusFilter.COMPLETED -> status == ServiceStatus.COMPLETED
    }
}

private enum class HistoryStatusFilter(val label: String) {
    ALL("Todos"),
    IN_PROGRESS("Em andamento"),
    WAITING_PICKUP("Aguardando retirada"),
    COMPLETED("Finalizado"),
}

@Composable
private fun TimelineCard(service: Service) {
    val tokens = LocalAutoCheckTokens.current
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFEFF6FF),
        shape = RoundedCornerShape(12.dp),
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
        ) {
            val compact = maxWidth < 620.dp
            if (compact) {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        androidx.compose.material3.Icon(Icons.Outlined.History, contentDescription = null, tint = tokens.sidebarAccent)
                        Text("Entrada: ${service.entryDateLabel}", color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    if (service.exitDateLabel != null) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            androidx.compose.material3.Icon(Icons.Outlined.Done, contentDescription = null, tint = Color(0xFF16A34A))
                            Text("Saida: ${service.exitDateLabel}", color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Text("Em aberto", color = Color(0xFFB45309), fontWeight = FontWeight.SemiBold)
                    }
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        androidx.compose.material3.Icon(Icons.Outlined.History, contentDescription = null, tint = tokens.sidebarAccent)
                        Text("Entrada: ${service.entryDateLabel}", color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
                    }
                    if (service.exitDateLabel != null) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            androidx.compose.material3.Icon(Icons.Outlined.Done, contentDescription = null, tint = Color(0xFF16A34A))
                            Text("Saida: ${service.exitDateLabel}", color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
                        }
                    } else {
                        Text("Em aberto", color = Color(0xFFB45309), fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }
}

@Composable
private fun SectionHeader(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
) {
    val tokens = LocalAutoCheckTokens.current
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        androidx.compose.material3.Icon(icon, contentDescription = null, tint = tokens.sidebarAccent)
        Text(title, style = MaterialTheme.typography.titleLarge, color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun InfoGrid(content: @Composable () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        content()
    }
}

@Composable
private fun InfoCell(label: String, value: String) {
    Surface(
        color = Color(0xFFF9FAFB),
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier.fillMaxWidth(),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(label, style = MaterialTheme.typography.labelSmall, color = Color(0xFF6B7280))
            Text(value, style = MaterialTheme.typography.bodyLarge, color = Color(0xFF111827), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun PhotosGrid(
    service: Service,
    onPhotoClick: (InspectionPhoto) -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shape = RoundedCornerShape(12.dp),
    ) {
        BoxWithConstraints(modifier = Modifier.fillMaxWidth().padding(12.dp)) {
            val columns = when {
                maxWidth >= 900.dp -> 4
                maxWidth >= 680.dp -> 3
                maxWidth >= 420.dp -> 2
                else -> 1
            }
            val chunked = service.inspectionPhotos.chunked(columns)

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                chunked.forEach { row ->
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.fillMaxWidth()) {
                        row.forEach { photo ->
                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .background(Color(0xFFF3F4F6), RoundedCornerShape(10.dp))
                                    .clickable { onPhotoClick(photo) }
                                    .padding(6.dp),
                            ) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(90.dp)
                                        .background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)),
                                ) {
                                    PlatformPhotoPreview(
                                        imageBytes = photo.bytes,
                                        modifier = Modifier.fillMaxSize(),
                                    )
                                }
                                Text(
                                    text = photo.region.replace("_", " "),
                                    modifier = Modifier.padding(top = 4.dp),
                                    style = MaterialTheme.typography.labelMedium,
                                    color = Color(0xFF111827),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                                Text(
                                    text = photo.timestampLabel,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF6B7280),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                )
                            }
                        }
                        repeat(columns - row.size) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PhotoPreviewDialog(
    photo: InspectionPhoto,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 920.dp)
                .heightIn(max = 760.dp),
            shape = RoundedCornerShape(14.dp),
            color = Color.White,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column {
                        Text(
                            text = photo.region.replace("_", " "),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = photo.timestampLabel,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF6B7280),
                        )
                    }
                    TextButton(onClick = onDismiss) {
                        Text("Fechar")
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color(0xFFE5E7EB)),
                )
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .background(Color(0xFFF3F4F6))
                        .padding(10.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    if (photo.bytes != null) {
                        PlatformPhotoPreview(
                            imageBytes = photo.bytes,
                            modifier = Modifier.fillMaxSize(),
                        )
                    } else {
                        Text("Foto indisponivel para visualizacao")
                    }
                }
            }
        }
    }
}
