package br.com.manieri.amanitamuscaria.platform

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun PlatformPhotoPreview(
    imageBytes: ByteArray?,
    modifier: Modifier = Modifier,
)
