package br.com.manieri.amanitamuscaria.ui.screens.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Clear
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.layout.ContentScale
import org.jetbrains.compose.resources.painterResource
import amanitamuscaria.composeapp.generated.resources.Res
import amanitamuscaria.composeapp.generated.resources.car_diagram
import br.com.manieri.amanitamuscaria.model.Client
import br.com.manieri.amanitamuscaria.model.InspectionPhoto
import br.com.manieri.amanitamuscaria.model.Service
import br.com.manieri.amanitamuscaria.model.ServiceStatus
import br.com.manieri.amanitamuscaria.model.Vehicle
import br.com.manieri.amanitamuscaria.platform.rememberPlatformCameraCapture
import br.com.manieri.amanitamuscaria.platform.PlatformPhotoPreview
import br.com.manieri.amanitamuscaria.ui.theme.LocalAutoCheckTokens
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
    val signatureStrokes = remember { mutableStateListOf<List<androidx.compose.ui.geometry.Offset>>() }
    var currentStroke by remember { mutableStateOf<List<androidx.compose.ui.geometry.Offset>>(emptyList()) }

    val launchCamera = rememberPlatformCameraCapture(
        onImageCaptured = {
            val region = pendingRegion ?: return@rememberPlatformCameraCapture
            inspectionPhotos.add(
                InspectionPhoto(
                    id = Random.nextInt(100000, 999999).toString(),
                    region = region,
                    url = "captured://$region/${inspectionPhotos.size + 1}",
                    timestampLabel = "Agora",
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Novo Check-in",
                style = MaterialTheme.typography.headlineLarge,
                color = tokens.textPrimary,
            )
            TextButton(onClick = onCancel) {
                Text("Cancelar")
            }
        }

        StepIndicator(step = step)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Column(
                modifier = Modifier.widthIn(max = 920.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
            ) {
                when (step) {
                    1 -> {
                        Field("Placa", plate) { plate = it.uppercase() }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field("Marca", brand, Modifier.weight(1f)) { brand = it }
                            Field("Modelo", model, Modifier.weight(1f)) { model = it }
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field("Ano", year, Modifier.weight(1f)) { year = it }
                            Field("Cor", color, Modifier.weight(1f)) { color = it }
                            Field("Quilometragem", mileage, Modifier.weight(1f)) { mileage = it }
                        }
                    }

                    2 -> {
                        Field("Nome completo", clientName) { clientName = it }
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            Field("Telefone", phone, Modifier.weight(1f)) { phone = it }
                            Field("Documento", document, Modifier.weight(1f)) { document = it }
                        }
                        Field("Email", email) { email = it }
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

                    4 -> {
                        Text(
                            text = "Assinatura do cliente",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.SemiBold,
                        )
                        SignaturePad(
                            strokes = signatureStrokes,
                            currentStroke = currentStroke,
                            onStrokeChange = { currentStroke = it },
                            onStrokeCommit = { stroke ->
                                if (stroke.size > 1) signatureStrokes.add(stroke)
                                currentStroke = emptyList()
                            },
                            onClear = {
                                signatureStrokes.clear()
                                currentStroke = emptyList()
                            },
                        )
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(tokens.canvasBackground)
                .padding(horizontal = 24.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Button(
                onClick = { if (step > 1) step -= 1 },
                enabled = step > 1,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = tokens.textPrimary,
                ),
            ) {
                Icon(androidx.compose.material.icons.Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = null)
                Text("Voltar")
            }

            Button(
                onClick = {
                    if (step < 4) {
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
                            entryDateLabel = "Agora",
                            observations = observations,
                            inspectionPhotos = inspectionPhotos.toList(),
                            signature = if (signatureStrokes.isEmpty()) null else "signature_strokes_${signatureStrokes.size}",
                        )
                        onComplete(service)
                    }
                },
                enabled = canProceed,
                colors = ButtonDefaults.buttonColors(containerColor = tokens.sidebarAccent),
            ) {
                Text(if (step < 4) "Avancar" else "Confirmar Check-in")
                Icon(androidx.compose.material.icons.Icons.AutoMirrored.Outlined.ArrowForward, contentDescription = null)
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
private fun StepIndicator(step: Int) {
    val labels = listOf("Veiculo", "Cliente", "Inspecao", "Assinatura")
    val tokens = LocalAutoCheckTokens.current
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 12.dp),
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
                        .padding(horizontal = 14.dp, vertical = 10.dp),
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
                    modifier = Modifier.padding(top = 4.dp),
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
private fun SignaturePad(
    strokes: List<List<androidx.compose.ui.geometry.Offset>>,
    currentStroke: List<androidx.compose.ui.geometry.Offset>,
    onStrokeChange: (List<androidx.compose.ui.geometry.Offset>) -> Unit,
    onStrokeCommit: (List<androidx.compose.ui.geometry.Offset>) -> Unit,
    onClear: () -> Unit,
) {
    val tokens = LocalAutoCheckTokens.current
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp)
                .background(Color.White, RoundedCornerShape(12.dp))
                .border(2.dp, tokens.border, RoundedCornerShape(12.dp)),
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                onStrokeChange(listOf(offset))
                            },
                            onDragEnd = {
                                onStrokeCommit(currentStroke)
                            },
                            onDragCancel = {
                                onStrokeChange(emptyList())
                            },
                            onDrag = { change, _ ->
                                onStrokeChange(currentStroke + change.position)
                            },
                        )
                    },
            ) {
                strokes.forEach { stroke ->
                    drawStroke(stroke)
                }
                drawStroke(currentStroke)
            }
            if (strokes.isEmpty() && currentStroke.isEmpty()) {
                Text(
                    text = "Assine aqui",
                    modifier = Modifier.align(Alignment.Center),
                    color = tokens.textSecondary,
                    style = TextStyle(fontWeight = FontWeight.Medium),
                )
            }
        }
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text(
                text = if (strokes.isEmpty()) "Assinatura pendente" else "Assinatura capturada",
                color = if (strokes.isEmpty()) Color(0xFFDC2626) else Color(0xFF16A34A),
            )
            Button(
                onClick = onClear,
                colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = tokens.textPrimary),
            ) {
                Icon(androidx.compose.material.icons.Icons.Outlined.Clear, contentDescription = null)
                Text("Limpar")
            }
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawStroke(points: List<androidx.compose.ui.geometry.Offset>) {
    if (points.size < 2) return
    val path = Path().apply {
        moveTo(points.first().x, points.first().y)
        points.drop(1).forEach { lineTo(it.x, it.y) }
    }
    drawPath(path = path, color = Color(0xFF111827), style = Stroke(width = 3.2f))
}

@Composable
private fun Field(
    label: String,
    value: String,
    modifier: Modifier = Modifier.fillMaxWidth(),
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        shape = RoundedCornerShape(10.dp),
        singleLine = true,
    )
}
