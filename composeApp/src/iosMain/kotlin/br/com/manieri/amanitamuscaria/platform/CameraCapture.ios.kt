package br.com.manieri.amanitamuscaria.platform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
actual fun rememberPlatformCameraCapture(
    onImageCaptured: (ByteArray) -> Unit,
    onCaptureCancelled: () -> Unit,
): () -> Unit {
    return remember {
        { onCaptureCancelled() }
    }
}
