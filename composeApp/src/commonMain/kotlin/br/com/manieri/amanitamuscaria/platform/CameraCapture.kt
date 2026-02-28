package br.com.manieri.amanitamuscaria.platform

import androidx.compose.runtime.Composable

@Composable
expect fun rememberPlatformCameraCapture(
    onImageCaptured: (ByteArray) -> Unit,
    onCaptureCancelled: () -> Unit,
): () -> Unit
