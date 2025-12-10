package br.com.manieri.amanitamuscaria.ui.novaEntrada

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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DirectionsCar
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold

import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaEntradaScreen(
    state: VehicleFormState,
    onFieldChange: (FormField, String) -> Unit,
    onSaveClick: () -> Unit,
    onAddPhotoClick: () -> Unit,
    onRemovePhoto: (String) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(state.fieldErrors) {
        if (state.fieldErrors.isNotEmpty()) {
            snackbarHostState.showSnackbar("Revise os campos destacados.")
        }
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
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surface)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(onClick = onSaveClick, enabled = !state.isSaving) {
                    Text(if (state.isSaving) "Salvando..." else "Salvar entrada")
                }
            }
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        contentWindowInsets = WindowInsets.safeDrawing.only(WindowInsetsSides.Top + WindowInsetsSides.Horizontal)
    ) { padding ->
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
                text = "Check-in do veículo",
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Preencha os dados para a entrada no pátio.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            VehicleField(
                value = state.plate,
                onValueChange = { onFieldChange(FormField.PLATE, it) },
                label = "Placa",
                leadingIcon = { Icon(Icons.Default.Flag, contentDescription = null) },
                isError = state.fieldErrors.containsKey(FormField.PLATE),
                supporting = state.fieldErrors[FormField.PLATE]
            )
            VehicleField(
                value = state.brand,
                onValueChange = { onFieldChange(FormField.BRAND, it) },
                label = "Marca",
                leadingIcon = { Icon(Icons.Default.Factory, contentDescription = null) },
                isError = state.fieldErrors.containsKey(FormField.BRAND),
                supporting = state.fieldErrors[FormField.BRAND]
            )
            VehicleField(
                value = state.model,
                onValueChange = { onFieldChange(FormField.MODEL, it) },
                label = "Modelo",
                leadingIcon = { Icon(Icons.Default.DirectionsCar, contentDescription = null) },
                isError = state.fieldErrors.containsKey(FormField.MODEL),
                supporting = state.fieldErrors[FormField.MODEL]
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                VehicleField(
                    modifier = Modifier.weight(1f),
                    value = state.year,
                    onValueChange = { onFieldChange(FormField.YEAR, it) },
                    label = "Ano",
                    leadingIcon = { Icon(Icons.Default.Numbers, contentDescription = null) },
                    isError = state.fieldErrors.containsKey(FormField.YEAR),
                    supporting = state.fieldErrors[FormField.YEAR],
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )
                VehicleField(
                    modifier = Modifier.weight(1f),
                    value = state.color,
                    onValueChange = { onFieldChange(FormField.COLOR, it) },
                    label = "Cor",
                    leadingIcon = { Icon(Icons.Default.ColorLens, contentDescription = null) },
                    isError = state.fieldErrors.containsKey(FormField.COLOR),
                    supporting = state.fieldErrors[FormField.COLOR]
                )
            }
            VehicleField(
                value = state.mileage,
                onValueChange = { onFieldChange(FormField.MILEAGE, it) },
                label = "Quilometragem",
                leadingIcon = { Icon(Icons.Default.HomeRepairService, contentDescription = null) },
                isError = state.fieldErrors.containsKey(FormField.MILEAGE),
                supporting = state.fieldErrors[FormField.MILEAGE],
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            VehicleField(
                value = state.customerName,
                onValueChange = { onFieldChange(FormField.CUSTOMER, it) },
                label = "Nome do cliente",
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                isError = state.fieldErrors.containsKey(FormField.CUSTOMER),
                supporting = state.fieldErrors[FormField.CUSTOMER]
            )
            VehicleField(
                value = state.notes,
                onValueChange = { onFieldChange(FormField.NOTES, it) },
                label = "Observações gerais",
                leadingIcon = { Icon(Icons.Default.Description, contentDescription = null) },
                singleLine = false,
                minLines = 3
            )

            PhotoSection(
                photos = state.photos,
                onRemovePhoto = onRemovePhoto
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun VehicleField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier.fillMaxWidth(),
    isError: Boolean = false,
    supporting: String? = null,
    singleLine: Boolean = true,
    minLines: Int = 1,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = leadingIcon,
        singleLine = singleLine,
        minLines = minLines,
        isError = isError,
        supportingText = supporting?.let { { Text(it, color = MaterialTheme.colorScheme.error) } },
        keyboardOptions = keyboardOptions
    )
}

@Composable
private fun PhotoSection(
    photos: List<String>,
    onRemovePhoto: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Fotos do veículo", style = MaterialTheme.typography.titleMedium)
            Text(
                text = "${photos.size} fotos",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        if (photos.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Column {
                        Text(
                            "Sem fotos ainda",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            "Adicione fotos de todas as faces do veículo.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp, max = 520.dp),
                columns = GridCells.Fixed(3),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 8.dp)
            ) {
                items(photos) { uri ->
                    PhotoItem(uri = uri, onRemovePhoto = { onRemovePhoto(uri) })
                }
            }
        }
    }
}

@Composable
private fun PhotoItem(
    uri: String,
    onRemovePhoto: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp)
    ) {
        Column {
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                contentScale = ContentScale.Crop
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 6.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onRemovePhoto) {
                    Text("Remover")
                }
            }
        }
    }
}
