package br.com.manieri.amanitamuscaria.ui.screens.settings

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
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Download
import androidx.compose.material.icons.outlined.Save
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Upload
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import br.com.manieri.amanitamuscaria.model.WorkshopSettings
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens

@Composable
fun SettingsScreen(
    settings: WorkshopSettings,
    onUpdateSettings: (WorkshopSettings) -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFDBEAFE), RoundedCornerShape(10.dp))
                        .padding(10.dp),
                ) {
                    Icon(Icons.Outlined.Settings, contentDescription = null, tint = tokens.sidebarAccent)
                }
                Column {
                    Text("Configuracoes", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
                    Text("Personalize o sistema da sua oficina", color = tokens.textSecondary)
                }
            }
            Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = tokens.sidebarAccent)) {
                Icon(Icons.Outlined.Save, contentDescription = null)
                Text("Salvar Alteracoes")
            }
        }

        Spacer(Modifier.height(16.dp))
        SectionCard("Informacoes da Oficina", Icons.Outlined.Build) {
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    modifier = Modifier.width(420.dp),
                    value = settings.workshopName,
                    onValueChange = { onUpdateSettings(settings.copy(workshopName = it)) },
                    label = { Text("Nome da Oficina") },
                    singleLine = true,
                )
                OutlinedTextField(
                    modifier = Modifier.width(300.dp),
                    value = settings.cnpj,
                    onValueChange = { onUpdateSettings(settings.copy(cnpj = it)) },
                    label = { Text("CNPJ") },
                    singleLine = true,
                )
            }
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settings.address,
                onValueChange = { onUpdateSettings(settings.copy(address = it)) },
                label = { Text("Endereco") },
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                modifier = Modifier.width(300.dp),
                value = settings.phone,
                onValueChange = { onUpdateSettings(settings.copy(phone = it)) },
                label = { Text("Telefone") },
                singleLine = true,
            )
        }

        Spacer(Modifier.height(14.dp))
        SectionCard("Configuracoes de Relatorio", Icons.Outlined.Settings) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = settings.reportHeader,
                onValueChange = { onUpdateSettings(settings.copy(reportHeader = it)) },
                label = { Text("Cabecalho do Relatorio") },
                singleLine = true,
            )
            Spacer(Modifier.height(12.dp))
            ToggleRow(
                title = "Mostrar logo nos relatorios",
                subtitle = "Inclui o logo da oficina nos relatorios impressos",
                checked = settings.showLogoInReport,
                onChecked = { onUpdateSettings(settings.copy(showLogoInReport = it)) },
            )
            Spacer(Modifier.height(12.dp))
            ToggleRow(
                title = "Assinatura obrigatoria",
                subtitle = "Exige assinatura do cliente no check-out",
                checked = settings.requireSignature,
                onChecked = { onUpdateSettings(settings.copy(requireSignature = it)) },
            )
        }

        Spacer(Modifier.height(14.dp))
        SectionCard("Backup e Restauracao", Icons.Outlined.Download) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Exportar dados", fontWeight = FontWeight.Medium)
                    Text("Faca backup de atendimentos e configuracoes", color = tokens.textSecondary)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary)) {
                    Icon(Icons.Outlined.Download, contentDescription = null)
                    Text("Exportar")
                }
            }
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column {
                    Text("Importar dados", fontWeight = FontWeight.Medium)
                    Text("Restaure dados de um backup anterior", color = tokens.textSecondary)
                }
                Button(onClick = { }, colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary)) {
                    Icon(Icons.Outlined.Upload, contentDescription = null)
                    Text("Importar")
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    content: @Composable () -> Unit,
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
            Icon(icon, contentDescription = null, tint = tokens.sidebarAccent)
            Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.SemiBold)
        }
        Spacer(Modifier.height(14.dp))
        content()
    }
}

@Composable
private fun ToggleRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    onChecked: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column {
            Text(title, fontWeight = FontWeight.Medium)
            Text(subtitle, color = LocalAutoCheckTokens.current.textSecondary, style = MaterialTheme.typography.bodyMedium)
        }
        Switch(checked = checked, onCheckedChange = onChecked)
    }
}
