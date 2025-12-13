package br.com.manieri.amanitamuscaria.ui.configuracoes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow
import androidx.compose.ui.res.stringResource
import br.com.manieri.amanitamuscaria.R
import kotlin.math.roundToInt

@Composable
fun ConfiguracoesScreen(
    state: CompanySettingsUiState,
    events: Flow<ConfiguracoesUiEvent>,
    onFieldChange: (SettingsField, String) -> Unit,
    onLogoClick: () -> Unit,
    onCropChange: (Float, Float) -> Unit,
    onSaveClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(events) {
        events.collect { event ->
            when (event) {
                is ConfiguracoesUiEvent.Saved -> snackbarHostState.showSnackbar(event.message)
                is ConfiguracoesUiEvent.ShowMessage -> snackbarHostState.showSnackbar(event.message)
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onSaveClick, enabled = !state.isSaving && !state.isLoading) {
                    Text(
                        if (state.isSaving) "Salvando..." else stringResource(
                            id = R.string.settings_save
                        )
                    )
                }
            }
        },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { padding ->
        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator()
                    Text(text = stringResource(id = R.string.settings_loading))
                }
            }
            return@Scaffold
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .consumeWindowInsets(padding)
                .windowInsetsPadding(WindowInsets.ime)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = stringResource(id = R.string.title_settings),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(id = R.string.settings_description),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            OutlinedTextField(
                value = state.companyName,
                onValueChange = { onFieldChange(SettingsField.COMPANY_NAME, it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.company_name_label)) },
                leadingIcon = { Icon(Icons.Default.Business, contentDescription = null) },
                isError = state.fieldErrors.containsKey(SettingsField.COMPANY_NAME),
                supportingText = state.fieldErrors[SettingsField.COMPANY_NAME]?.let {
                    { Text(it, color = MaterialTheme.colorScheme.error) }
                }
            )

            OutlinedTextField(
                value = state.address,
                onValueChange = { onFieldChange(SettingsField.ADDRESS, it) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(id = R.string.company_address_label)) },
                leadingIcon = { Icon(Icons.Default.LocationOn, contentDescription = null) }
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                OutlinedTextField(
                    value = state.phone,
                    onValueChange = { onFieldChange(SettingsField.PHONE, it) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(id = R.string.company_phone_label)) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
                OutlinedTextField(
                    value = state.whatsapp,
                    onValueChange = { onFieldChange(SettingsField.WHATSAPP, it) },
                    modifier = Modifier.weight(1f),
                    label = { Text(stringResource(id = R.string.company_whatsapp_label)) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone)
                )
            }

            LogoCard(
                logoUri = state.logoUri,
                cropX = state.cropX,
                cropY = state.cropY,
                onLogoClick = onLogoClick,
                onCropChange = onCropChange
            )

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Composable
private fun LogoCard(
    logoUri: String?,
    cropX: Float,
    cropY: Float,
    onLogoClick: () -> Unit,
    onCropChange: (Float, Float) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(id = R.string.logo_label), style = MaterialTheme.typography.titleMedium)
                TextButton(onClick = onLogoClick) {
                    Icon(Icons.Default.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.size(4.dp))
                    Text(stringResource(id = R.string.logo_pick_button))
                }
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (logoUri == null) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Image, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(
                            stringResource(id = R.string.settings_no_logo),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                } else {
                    AsyncImage(
                        model = logoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize(),
                        alignment = biasAlignment(cropX, cropY),
                        contentScale = ContentScale.Crop
                    )
                }
            }
            if (logoUri != null) {
                Text(
                    stringResource(id = R.string.settings_logo_hint),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(id = R.string.settings_horizontal_position), style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = ((cropX + 1f) / 2f).coerceIn(0f, 1f),
                        onValueChange = { onCropChange(it * 2f - 1f, cropY) },
                        valueRange = 0f..1f
                    )
                    Text(stringResource(id = R.string.settings_vertical_position), style = MaterialTheme.typography.labelMedium)
                    Slider(
                        value = ((cropY + 1f) / 2f).coerceIn(0f, 1f),
                        onValueChange = { onCropChange(cropX, it * 2f - 1f) },
                        valueRange = 0f..1f
                    )
                }
            }
        }
    }
}

private fun biasAlignment(horizontalBias: Float, verticalBias: Float): Alignment = Alignment { size, space, _ ->
    val x = ((space.width - size.width) * ((horizontalBias + 1f) / 2f)).roundToInt()
    val y = ((space.height - size.height) * ((verticalBias + 1f) / 2f)).roundToInt()
    IntOffset(x, y)
}
