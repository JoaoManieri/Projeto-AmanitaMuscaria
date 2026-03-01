package br.com.manieri.amanitamuscaria.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
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
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = {
            androidx.compose.material3.Icon(Icons.Outlined.Description, contentDescription = null)
        },
        title = {
            Text("Detalhes do atendimento")
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Placa: ${service.plate}", fontWeight = FontWeight.SemiBold)
                Text("Cliente: ${service.client.name}")
                Text("Telefone: ${service.client.phone}")
                Text("Email: ${service.client.email ?: "-"}")
                Text("Documento: ${service.client.document ?: "-"}")
                Text("Veiculo: ${service.vehicle.brand} ${service.vehicle.model} (${service.vehicle.year})")
                Text("Cor: ${service.vehicle.color} | Km: ${service.vehicle.mileage}")
                Text("Entrada: ${service.entryDateLabel}")
                Text("Saida: ${service.exitDateLabel ?: "-"}")
                Text("Status: ${service.status.name}")
                Text("Fotos de inspecao: ${service.inspectionPhotos.size}")
                Text("Observacoes: ${service.observations.ifBlank { "Nenhuma observacao registrada." }}")
            }
        },
        confirmButton = {
            Button(
                onClick = onGenerateReport,
                colors = ButtonDefaults.buttonColors(containerColor = LocalAutoCheckTokens.current.sidebarAccent),
            ) {
                Text("Gerar/Abrir PDF")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Fechar")
            }
        },
    )
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
