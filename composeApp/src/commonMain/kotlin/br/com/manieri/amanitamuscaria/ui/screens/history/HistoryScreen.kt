package br.com.manieri.amanitamuscaria.ui.screens.history

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Print
import androidx.compose.material.icons.outlined.RemoveRedEye
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.ui.components.StatusBadge
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun HistoryScreen(services: List<Service>) {
    val tokens = LocalAutoCheckTokens.current
    var plateFilter by remember { mutableStateOf("") }
    var clientFilter by remember { mutableStateOf("") }
    var statusFilter by remember { mutableStateOf("all") }

    val filtered = services.filter { service ->
        val byPlate = plateFilter.isBlank() || service.plate.contains(plateFilter, ignoreCase = true)
        val byClient = clientFilter.isBlank() || service.client.name.contains(clientFilter, ignoreCase = true)
        val byStatus = statusFilter == "all" || service.status.name.equals(statusFilter, ignoreCase = true)
        byPlate && byClient && byStatus
    }

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
            onStatusChange = { statusFilter = it },
        )

        Spacer(Modifier.height(16.dp))
        StatsRow(services)

        Spacer(Modifier.height(16.dp))
        HistoryTable(filtered)
    }
}

@Composable
private fun FilterCard(
    plateFilter: String,
    clientFilter: String,
    statusFilter: String,
    onPlateChange: (String) -> Unit,
    onClientChange: (String) -> Unit,
    onStatusChange: (String) -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
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
            OutlinedTextField(
                modifier = Modifier.width(320.dp),
                value = statusFilter,
                onValueChange = onStatusChange,
                singleLine = true,
                label = { Text("Status (all/in_progress/waiting_pickup/completed)") },
            )
        }
    }
}

@Composable
private fun StatsRow(services: List<Service>) {
    Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
        StatCard("Total", services.size.toString(), Color(0xFF111827))
        StatCard(
            "Em andamento",
            services.count { it.status == ServiceStatus.IN_PROGRESS }.toString(),
            Color(0xFF2563EB),
        )
        StatCard(
            "Aguardando",
            services.count { it.status == ServiceStatus.WAITING_PICKUP }.toString(),
            Color(0xFFEA580C),
        )
        StatCard(
            "Finalizados",
            services.count { it.status == ServiceStatus.COMPLETED }.toString(),
            Color(0xFF16A34A),
        )
    }
}

@Composable
private fun StatCard(label: String, value: String, color: Color) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .width(250.dp)
            .background(Color.White, RoundedCornerShape(tokens.radiusLg))
            .border(1.dp, tokens.border, RoundedCornerShape(tokens.radiusLg))
            .padding(14.dp),
    ) {
        Text(label, color = tokens.textSecondary, style = MaterialTheme.typography.bodyMedium)
        Text(value, color = color, style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun HistoryTable(services: List<Service>) {
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
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Cell(service.entryDateLabel, Modifier.width(160.dp))
                    Cell(service.plate, Modifier.width(120.dp), bold = true)
                    Cell(service.client.name, Modifier.width(180.dp))
                    Cell("${service.vehicle.brand} ${service.vehicle.model}", Modifier.width(180.dp))
                    Box(modifier = Modifier.width(190.dp)) { StatusBadge(service.status) }
                    Row(modifier = Modifier.width(160.dp), horizontalArrangement = Arrangement.End) {
                        TinyAction("Ver", Icons.Outlined.RemoveRedEye)
                        TinyAction("Print", Icons.Outlined.Print)
                        TinyAction("Down", Icons.Outlined.Download)
                    }
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
        HeaderCell("Data", Modifier.width(160.dp))
        HeaderCell("Placa", Modifier.width(120.dp))
        HeaderCell("Cliente", Modifier.width(180.dp))
        HeaderCell("Veiculo", Modifier.width(180.dp))
        HeaderCell("Status", Modifier.width(190.dp))
        HeaderCell("Acoes", Modifier.width(160.dp), right = true)
    }
}

@Composable
private fun HeaderCell(text: String, modifier: Modifier, right: Boolean = false) {
    Box(modifier = modifier, contentAlignment = if (right) Alignment.CenterEnd else Alignment.CenterStart) {
        Text(text, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun Cell(text: String, modifier: Modifier, bold: Boolean = false) {
    Text(
        text = text,
        modifier = modifier,
        fontWeight = if (bold) FontWeight.SemiBold else FontWeight.Normal,
        color = Color(0xFF111827),
    )
}

@Composable
private fun TinyAction(text: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Button(
        onClick = { },
        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF374151)),
    ) {
        androidx.compose.material3.Icon(icon, contentDescription = text)
    }
}
