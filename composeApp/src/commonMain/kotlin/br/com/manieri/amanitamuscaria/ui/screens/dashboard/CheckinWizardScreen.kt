package br.com.manieri.amanitamuscaria.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import amanitamuscaria.composeapp.generated.resources.Res
import amanitamuscaria.composeapp.generated.resources.car_diagram
import androidx.compose.foundation.text.KeyboardOptions
import br.com.manieri.amanitamuscaria.model.Client
import br.com.manieri.amanitamuscaria.model.InspectionPhoto
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.model.Vehicle
import br.com.manieri.amanitamuscaria.platform.rememberPlatformCameraCapture
import br.com.manieri.amanitamuscaria.platform.PlatformPhotoPreview
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens
import br.com.manieri.amanitamuscaria.util.currentDateTimeLabel
import kotlin.random.Random

@Composable
fun CheckinWizardScreen(
    onComplete: (Service) -> Unit,
    onCancel: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    var step by remember { mutableIntStateOf(1) }

    var plate by remember { mutableStateOf("") }
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }
    var color by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }

    var clientName by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var document by remember { mutableStateOf("") }

    var observations by remember { mutableStateOf("") }
    val inspectionPhotos = remember { mutableStateListOf<InspectionPhoto>() }
    var pendingRegion by remember { mutableStateOf<String?>(null) }

    val launchCamera = rememberPlatformCameraCapture(
        onImageCaptured = {
            val region = pendingRegion ?: return@rememberPlatformCameraCapture
            inspectionPhotos.add(
                InspectionPhoto(
                    id = Random.nextInt(100000, 999999).toString(),
                    region = region,
                    url = "captured://$region/${inspectionPhotos.size + 1}",
                    timestampLabel = currentDateTimeLabel(),
                    bytes = it,
                ),
            )
            pendingRegion = null
        },
        onCaptureCancelled = {
            pendingRegion = null
        },
    )

    val canProceed = when (step) {
        1 -> true
        2 -> true
        3 -> true
        else -> true
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        val isCompactLayout = maxWidth < 980.dp || maxHeight < 700.dp
        val headerHorizontalPadding = if (isCompactLayout) 16.dp else 24.dp
        val headerVerticalPadding = if (isCompactLayout) 10.dp else 20.dp
        val contentVerticalPadding = if (isCompactLayout) 10.dp else 16.dp
        val sectionSpacing = if (isCompactLayout) 10.dp else 14.dp
        val footerVerticalPadding = if (isCompactLayout) 8.dp else 14.dp
        val actionButtonHeight = if (isCompactLayout) 42.dp else 48.dp
        val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

        Column(modifier = Modifier.fillMaxSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = headerHorizontalPadding, vertical = headerVerticalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Novo Check-in",
                    style = if (isCompactLayout) MaterialTheme.typography.headlineMedium else MaterialTheme.typography.headlineLarge,
                    color = tokens.textPrimary,
                )
                TextButton(onClick = onCancel) {
                    Text("Cancelar")
                }
            }

            StepIndicator(
                step = step,
                compact = isCompactLayout,
                horizontalPadding = headerHorizontalPadding,
            )

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = headerHorizontalPadding, vertical = contentVerticalPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Column(
                    modifier = Modifier.widthIn(max = if (isCompactLayout) 820.dp else 920.dp),
                    verticalArrangement = Arrangement.spacedBy(sectionSpacing),
                ) {
                when (step) {
                    1 -> {
                        Field("Placa", plate) { plate = it.uppercase() }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field("Marca", brand, Modifier.weight(1f)) { brand = it }
                            Field("Modelo", model, Modifier.weight(1f)) { model = it }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field(
                                label = "Ano",
                                value = year,
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Number,
                            ) { year = it.filter(Char::isDigit).take(4) }
                            Field("Cor", color, Modifier.weight(1f)) { color = it }
                            Field(
                                label = "Quilometragem",
                                value = mileage,
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Number,
                            ) { mileage = it.filter(Char::isDigit).take(7) }
                        }
                    }

                    2 -> {
                        Field("Nome completo", clientName) { clientName = it }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field(
                                label = "Telefone",
                                value = phone,
                                modifier = Modifier.weight(1f),
                                keyboardType = KeyboardType.Phone,
                            ) { phone = formatBrazilPhone(it) }
                            Field("Documento", document, Modifier.weight(1f)) { document = it }
                        }
                        Field(
                            label = "Email",
                            value = email,
                            keyboardType = KeyboardType.Email,
                        ) { email = it }
                    }

                    3 -> {
                        Text(
                            text = "Inspecao visual",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        InspectionRegions(
                            photos = inspectionPhotos,
                            onRegionClick = { region ->
                                pendingRegion = region
                                launchCamera()
                            },
                        )
                        PhotoGrid(
                            photos = inspectionPhotos,
                            onRemovePhoto = { photoId ->
                                inspectionPhotos.removeAll { it.id == photoId }
                            },
                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = observations,
                            onValueChange = { observations = it },
                            minLines = 4,
                            label = { Text("Observacoes") },
                        )
                    }

                }
            }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(tokens.canvasBackground)
                    .padding(horizontal = headerHorizontalPadding, vertical = footerVerticalPadding),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = { if (step > 1) step -= 1 },
                    enabled = step > 1,
                    modifier = Modifier.height(actionButtonHeight),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = tokens.textPrimary,
                    ),
                ) {
                    Icon(androidx.compose.material.icons.Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                    Text("Etapa anterior")
                }

                Button(
                    onClick = {
                        if (step < 3) {
                            step += 1
                        } else {
                            val service = Service(
                                id = Random.nextInt(10000, 99999).toString(),
                                plate = plate,
                                vehicle = Vehicle(
                                    plate = plate,
                                    brand = brand,
                                    model = model,
                                    year = year.toIntOrNull() ?: 0,
                                    color = color,
                                    mileage = mileage.toIntOrNull() ?: 0,
                                ),
                                client = Client(
                                    name = clientName,
                                    phone = phone,
                                    email = email.ifBlank { null },
                                    document = document.ifBlank { null },
                                ),
                                status = ServiceStatus.IN_PROGRESS,
                                entryDateLabel = currentDateTimeLabel(),
                                observations = observations,
                                inspectionPhotos = inspectionPhotos.toList(),
                                signature = null,
                            )
                            onComplete(service)
                        }
                    },
                    enabled = canProceed,
                    modifier = Modifier.height(actionButtonHeight),
                    colors = ButtonDefaults.buttonColors(containerColor = tokens.sidebarAccent),
                ) {
                    Text(
                        if (step < 3) {
                            if (imeVisible) "Proxima etapa" else "Avancar etapa"
                        } else {
                            "Finalizar Check-in"
                        }
                    )
                    Icon(androidx.compose.material.icons.Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
                }
            }
        }
    }
}

@Composable
private fun PhotoGrid(
    photos: List<InspectionPhoto>,
    onRemovePhoto: (String) -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .border(1.dp, tokens.border, RoundedCornerShape(12.dp))
            .padding(12.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Text(
            text = "Fotos capturadas (${photos.size})",
            style = MaterialTheme.typography.titleLarge,
            color = tokens.textPrimary,
        )
        if (photos.isEmpty()) {
            Text("Nenhuma foto capturada ainda.", color = tokens.textSecondary)
        } else {
            photos.chunked(2).forEach { row ->
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    row.forEach { photo ->
                        Column(
                            modifier = Modifier
                                .widthIn(min = 200.dp)
                                .background(Color(0xFFF9FAFB), RoundedCornerShape(10.dp))
                                .border(1.dp, Color(0xFFE5E7EB), RoundedCornerShape(10.dp))
                                .padding(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .background(Color(0xFFE5E7EB), RoundedCornerShape(8.dp)),
                            ) {
                                PlatformPhotoPreview(
                                    imageBytes = photo.bytes,
                                    modifier = Modifier.fillMaxSize(),
                                )
                            }
                            Text(
                                text = photo.region,
                                style = MaterialTheme.typography.labelLarge,
                                modifier = Modifier.padding(top = 6.dp),
                            )
                            Text(
                                text = photo.timestampLabel,
                                style = MaterialTheme.typography.labelSmall,
                                color = tokens.textSecondary,
                            )
                            Button(
                                onClick = { onRemovePhoto(photo.id) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFFEE2E2),
                                    contentColor = Color(0xFFB91C1C),
                                ),
                                modifier = Modifier.padding(top = 6.dp),
                            ) {
                                Icon(androidx.compose.material.icons.Icons.Outlined.Delete, contentDescription = null)
                                Text("Remover")
                            }
                        }
                    }
                    if (row.size == 1) {
                        Spacer(Modifier.widthIn(min = 200.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun StepIndicator(
    step: Int,
    compact: Boolean,
    horizontalPadding: androidx.compose.ui.unit.Dp,
) {
    val labels = listOf("Veiculo", "Cliente", "Inspecao")
    val tokens = LocalAutoCheckTokens.current
    val verticalPadding = if (compact) 6.dp else 12.dp
    val bubbleHorizontalPadding = if (compact) 11.dp else 14.dp
    val bubbleVerticalPadding = if (compact) 7.dp else 10.dp
    val labelTopPadding = if (compact) 2.dp else 4.dp
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = horizontalPadding, vertical = verticalPadding),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        labels.forEachIndexed { index, label ->
            val current = index + 1
            val active = current == step
            val done = current < step
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .background(
                            color = when {
                                done -> tokens.statusGreen
                                active -> tokens.sidebarAccent
                                else -> Color(0xFFE5E7EB)
                            },
                            shape = CircleShape,
                        )
                        .padding(horizontal = bubbleHorizontalPadding, vertical = bubbleVerticalPadding),
                ) {
                    Text(
                        text = current.toString(),
                        color = if (active || done) Color.White else tokens.textSecondary,
                    )
                }
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (active) tokens.sidebarAccent else tokens.textSecondary,
                    modifier = Modifier.padding(top = labelTopPadding),
                )
            }
        }
    }
}

@Composable
private fun InspectionRegions(
    photos: List<InspectionPhoto>,
    onRegionClick: (String) -> Unit,
) {
    val regions = listOf(
        RegionSpot("front", 0.50f, 0.15f),
        RegionSpot("rear", 0.50f, 0.84f),
        RegionSpot("left_side", 0.16f, 0.50f),
        RegionSpot("right_side", 0.84f, 0.50f),
        RegionSpot("wheels", 0.28f, 0.74f),
        RegionSpot("interior", 0.50f, 0.50f),
    )
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF8FAFC), RoundedCornerShape(12.dp))
            .padding(14.dp),
    ) {
        Text(
            text = "Clique nas regioes para abrir a camera (${photos.size} fotos)",
            style = MaterialTheme.typography.bodyMedium,
            color = tokens.textSecondary,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(360.dp)
                .padding(top = 10.dp),
        ) {
            Image(
                painter = painterResource(Res.drawable.car_diagram),
                contentDescription = "Diagrama do veiculo",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center),
                contentScale = ContentScale.Fit,
            )
            regions.forEach { region ->
                RegionButton(
                    region = region.id,
                    completed = photos.any { it.region == region.id },
                    count = photos.count { it.region == region.id },
                    onRegionClick = onRegionClick,
                    modifier = Modifier.align(Alignment.TopStart)
                        .padding(
                            start = (region.x * 600).dp,
                            top = (region.y * 330).dp,
                        ),
                )
            }
        }
    }
}

@Composable
private fun RegionButton(
    region: String,
    completed: Boolean,
    count: Int,
    onRegionClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = { onRegionClick(region) },
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (completed) Color(0xFF16A34A) else Color(0xFFEF4444),
        ),
    ) {
        Icon(
            imageVector = if (completed) androidx.compose.material.icons.Icons.Outlined.Check else androidx.compose.material.icons.Icons.Outlined.PhotoCamera,
            contentDescription = null,
        )
        Text("$region ($count)")
    }
}

private data class RegionSpot(
    val id: String,
    val x: Float,
    val y: Float,
)

@Composable
private fun Field(
    label: String,
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
    )
}

private fun formatBrazilPhone(raw: String): String {
    val digits = raw.filter(Char::isDigit).take(11)
    if (digits.isEmpty()) return ""

    return when {
        digits.length <= 2 -> "(${digits}"
        digits.length <= 6 -> "(${digits.take(2)}) ${digits.drop(2)}"
        digits.length <= 10 -> "(${digits.take(2)}) ${digits.drop(2).take(4)}-${digits.drop(6)}"
        else -> "(${digits.take(2)}) ${digits.drop(2).take(5)}-${digits.drop(7)}"
    }
}
