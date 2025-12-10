package br.com.manieri.amanitamuscaria.ui.detalhesEntrada

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Factory
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HomeRepairService
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight

import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalhesEntradaScreen(
    state: DetalhesEntradaUiState,
    uiMessages: Flow<String>,
    onEvent: (DetalhesEntradaEvent) -> Unit,
    onAddPhotoClick: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiMessages) {
        uiMessages.collect { snackbarHostState.showSnackbar(it) }
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddPhotoClick,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Adicionar foto")
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { padding ->
        if (state.loading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                androidx.compose.material3.CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
                Text("Carregando detalhes...")
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
                text = "Edite as informações do veículo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Altere os campos necessários e salve para atualizar o histórico.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            VehicleField(
                label = "Placa",
                value = state.fieldValues[VehicleField.PLATE].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.PLATE, it)) },
                icon = { Icon(Icons.Default.Flag, contentDescription = null) },
                error = state.fieldErrors[VehicleField.PLATE]
            )

            VehicleField(
                label = "Marca",
                value = state.fieldValues[VehicleField.BRAND].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.BRAND, it)) },
                icon = { Icon(Icons.Default.Factory, contentDescription = null) },
                error = state.fieldErrors[VehicleField.BRAND]
            )

            VehicleField(
                label = "Modelo",
                value = state.fieldValues[VehicleField.MODEL].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.MODEL, it)) },
                icon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                error = state.fieldErrors[VehicleField.MODEL]
            )

            Row(horizontalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                VehicleField(
                    label = "Ano",
                    value = state.fieldValues[VehicleField.YEAR].orEmpty(),
                    onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.YEAR, it)) },
                    icon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                    error = state.fieldErrors[VehicleField.YEAR]
                )

                VehicleField(
                    label = "Cor",
                    value = state.fieldValues[VehicleField.COLOR].orEmpty(),
                    onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.COLOR, it)) },
                    icon = { Icon(Icons.Default.ColorLens, contentDescription = null) },
                    modifier = Modifier.weight(1f),
                    error = state.fieldErrors[VehicleField.COLOR]
                )
            }

            VehicleField(
                label = "Quilometragem",
                value = state.fieldValues[VehicleField.MILEAGE].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.MILEAGE, it)) },
                icon = { Icon(Icons.Default.HomeRepairService, contentDescription = null) },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = androidx.compose.ui.text.input.KeyboardType.Number),
                error = state.fieldErrors[VehicleField.MILEAGE]
            )

            VehicleField(
                label = "Nome do cliente",
                value = state.fieldValues[VehicleField.CUSTOMER].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.CUSTOMER, it)) },
                icon = { Icon(Icons.Default.Person, contentDescription = null) },
                error = state.fieldErrors[VehicleField.CUSTOMER]
            )

            VehicleField(
                label = "Observações",
                value = state.fieldValues[VehicleField.NOTES].orEmpty(),
                onValueChange = { onEvent(DetalhesEntradaEvent.UpdateField(VehicleField.NOTES, it)) },
                icon = { Icon(Icons.Default.Description, contentDescription = null) },
                singleLine = false,
                minLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fotos",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${state.photos.size} fotos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (state.photos.isEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text("Nenhuma foto", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold)
                        Text(
                            "Adicione fotos das faces do veículo para manter o histórico completo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            } else {
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(260.dp),
                    columns = GridCells.Fixed(3),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 8.dp)
                ) {
                    items(state.photos, key = { it }) { uri ->
                        PhotoItem(
                            uri = uri,
                            onRemove = { onEvent(DetalhesEntradaEvent.RemovePhoto(uri)) }
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    modifier = Modifier.weight(1f),
                    onClick = onAddPhotoClick
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null)
                    Spacer(modifier = Modifier.size(6.dp))
                    Text("Adicionar foto")
                }
                Spacer(modifier = Modifier.weight(1f))
            }

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                onClick = { onEvent(DetalhesEntradaEvent.Save) },
                enabled = !state.loading
            ) {
                Text("Salvar alterações")
            }
        }
    }
}

@Composable
private fun VehicleField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    minLines: Int = 1,
    error: String? = null
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = icon,
        keyboardOptions = keyboardOptions,
        singleLine = singleLine,
        minLines = minLines,
        isError = error != null,
        supportingText = error?.let { { Text(it, color = MaterialTheme.colorScheme.error) } }
    )
}

@Composable
private fun PhotoItem(
    uri: String,
    onRemove: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        androidx.compose.foundation.layout.Box {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Crop
            )
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.85f))
            ) {
                Icon(
                    Icons.Default.Close,
                    contentDescription = "Remover foto",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
