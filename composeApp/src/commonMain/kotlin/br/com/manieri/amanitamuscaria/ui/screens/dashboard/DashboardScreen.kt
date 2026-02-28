package br.com.manieri.amanitamuscaria.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.FileDownload
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import org.jetbrains.compose.resources.painterResource
import amanitamuscaria.composeapp.generated.resources.Res
import amanitamuscaria.composeapp.generated.resources.empty_state
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.ui.components.StatusBadge
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun DashboardScreen(
    services: List<Service>,
    selectedServiceId: String?,
    onSelectService: (String?) -> Unit,
    onAddService: (Service) -> Unit,
    onCompleteService: (String) -> Unit,
) {
    var showWizard by remember { mutableStateOf(false) }
    val activeServices = services.filter {
        it.status == ServiceStatus.IN_PROGRESS || it.status == ServiceStatus.WAITING_PICKUP
    }
    val selectedService = services.firstOrNull { it.id == selectedServiceId }

    if (showWizard) {
        CheckinWizardScreen(
            onComplete = {
                onAddService(it)
                showWizard = false
            },
            onCancel = { showWizard = false },
        )
        return
    }

    Row(modifier = Modifier.fillMaxSize()) {
        ServiceListPane(
            modifier = Modifier
                .fillMaxHeight()
                .width(430.dp),
            services = activeServices,
            selectedServiceId = selectedServiceId,
            onSelect = onSelectService,
            onNewCheckin = { showWizard = true },
        )
        ServiceDetailsPane(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC)),
            service = selectedService,
            onCheckout = {
                selectedService?.let { onCompleteService(it.id) }
            },
        )
    }
}

@Composable
private fun ServiceListPane(
    modifier: Modifier,
    services: List<Service>,
    selectedServiceId: String?,
    onSelect: (String?) -> Unit,
    onNewCheckin: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = modifier
            .background(Color(0xFFFAFAFA))
            .border(width = 1.dp, color = tokens.border),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "Atendimentos em Aberto",
                style = MaterialTheme.typography.headlineMedium,
                color = tokens.textPrimary,
            )
            Button(
                onClick = onNewCheckin,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = tokens.sidebarAccent),
                shape = RoundedCornerShape(tokens.radiusLg),
            ) {
                Icon(Icons.Outlined.Add, contentDescription = null)
                Text("Novo Check-in", style = MaterialTheme.typography.labelLarge)
            }
        }

        if (services.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text("Nenhum atendimento em aberto", color = tokens.textSecondary)
                Text(
                    "Clique em \"Novo Check-in\" para comecar",
                    color = tokens.textSecondary,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(services) { service ->
                    ServiceListItemCard(
                        service = service,
                        selected = selectedServiceId == service.id,
                        onClick = { onSelect(service.id) },
                    )
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text("Total: ${services.size}", color = tokens.textSecondary)
            Text(
                "Em andamento: ${services.count { it.status == ServiceStatus.IN_PROGRESS }}",
                color = tokens.textSecondary,
            )
        }
    }
}

@Composable
private fun ServiceListItemCard(
    service: Service,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    val borderColor = if (selected) tokens.sidebarAccent else Color.Transparent
    val bgColor = if (selected) Color(0xFFEFF6FF) else Color.White

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bgColor, RoundedCornerShape(tokens.radiusLg))
            .border(2.dp, borderColor, RoundedCornerShape(tokens.radiusLg))
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = service.plate,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )
            StatusBadge(status = service.status)
        }
        Text(text = service.client.name, color = tokens.textPrimary, fontWeight = FontWeight.Medium)
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(
                text = "${service.vehicle.brand} ${service.vehicle.model}",
                color = tokens.textSecondary,
                style = MaterialTheme.typography.bodyMedium,
            )
            Text(text = service.entryDateLabel, color = tokens.textSecondary, style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun ServiceDetailsPane(
    modifier: Modifier,
    service: Service?,
    onCheckout: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    if (service == null) {
        Box(modifier = modifier, contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(Res.drawable.empty_state),
                    contentDescription = "Empty state",
                    modifier = Modifier.width(220.dp),
                )
                Spacer(Modifier.height(16.dp))
                Text(
                    text = "Nenhum atendimento selecionado",
                    style = MaterialTheme.typography.headlineMedium,
                    color = tokens.textPrimary,
                )
                Text(
                    text = "Selecione um atendimento na lista ao lado para ver os detalhes",
                    color = tokens.textSecondary,
                )
            }
        }
        return
    }

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 980.dp)
                .align(Alignment.CenterHorizontally),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top,
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = service.plate,
                        style = MaterialTheme.typography.displayLarge,
                        color = tokens.textPrimary,
                    )
                    StatusBadge(service.status, large = true)
                }
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    if (service.status != ServiceStatus.COMPLETED) {
                        Button(
                            onClick = onCheckout,
                            colors = ButtonDefaults.buttonColors(containerColor = tokens.statusGreen),
                        ) {
                            Icon(Icons.Outlined.Done, contentDescription = null)
                            Text("Fazer Check-out")
                        }
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary),
                    ) {
                        Icon(Icons.Outlined.FileDownload, contentDescription = null)
                        Text("Gerar Relatorio")
                    }
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary),
                        modifier = Modifier.width(52.dp),
                    ) {
                        Icon(Icons.Outlined.Edit, contentDescription = null)
                    }
                }
            }

            Spacer(Modifier.height(18.dp))
            DetailCard(
                title = "Dados do Veiculo",
                icon = { Icon(Icons.Outlined.DirectionsCar, contentDescription = null, tint = tokens.sidebarAccent) },
            ) {
                DetailGridRow("Marca", service.vehicle.brand, "Modelo", service.vehicle.model, "Ano", service.vehicle.year.toString())
                Spacer(Modifier.height(12.dp))
                DetailGridRow("Cor", service.vehicle.color, "Quilometragem", "${service.vehicle.mileage} km", "Placa", service.vehicle.plate)
            }

            Spacer(Modifier.height(14.dp))
            DetailCard(
                title = "Dados do Cliente",
                icon = { Icon(Icons.Outlined.Person, contentDescription = null, tint = tokens.sidebarAccent) },
            ) {
                DetailGridRow("Nome", service.client.name, "Telefone", service.client.phone, "Email", service.client.email ?: "-")
            }

            Spacer(Modifier.height(14.dp))
            DetailCard(
                title = "Informacoes do Atendimento",
                icon = { Icon(Icons.Outlined.Description, contentDescription = null, tint = tokens.sidebarAccent) },
            ) {
                Text("Entrada: ${service.entryDateLabel}", color = tokens.textSecondary)
                service.exitDateLabel?.let {
                    Spacer(Modifier.height(6.dp))
                    Text("Saida: $it", color = tokens.textSecondary)
                }
                Spacer(Modifier.height(12.dp))
                Text("Observacoes", style = MaterialTheme.typography.labelLarge)
                Spacer(Modifier.height(6.dp))
                Text(
                    text = service.observations.ifBlank { "Nenhuma observacao registrada." },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp))
                        .padding(14.dp),
                    color = tokens.textPrimary,
                )
            }
        }
    }
}

@Composable
private fun DetailCard(
    title: String,
    icon: @Composable () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(tokens.radiusLg))
            .border(1.dp, tokens.border, RoundedCornerShape(tokens.radiusLg))
            .padding(20.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            icon()
            Text(title, style = MaterialTheme.typography.titleLarge, color = tokens.textPrimary)
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun DetailGridRow(
    l1: String,
    v1: String,
    l2: String,
    v2: String,
    l3: String,
    v3: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        DetailItem(l1, v1, Modifier.weight(1f))
        DetailItem(l2, v2, Modifier.weight(1f))
        DetailItem(l3, v3, Modifier.weight(1f))
    }
}

@Composable
private fun DetailItem(label: String, value: String, modifier: Modifier = Modifier) {
    val tokens = LocalAutoCheckTokens.current
    Column(modifier = modifier) {
        Text(label, style = MaterialTheme.typography.bodyMedium, color = tokens.textSecondary)
        Text(value, style = MaterialTheme.typography.bodyLarge, color = tokens.textPrimary, fontWeight = FontWeight.SemiBold)
    }
}
