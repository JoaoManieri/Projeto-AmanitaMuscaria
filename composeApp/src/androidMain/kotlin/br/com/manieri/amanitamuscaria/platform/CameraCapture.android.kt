package br.com.manieri.amanitamuscaria.platform

import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import java.io.ByteArrayOutputStream

@Composable
actual fun rememberPlatformCameraCapture(
    onImageCaptured: (ByteArray) -> Unit,
    onCaptureCancelled: () -> Unit,
): () -> Unit {
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
    ) { bitmap: Bitmap? ->
        if (bitmap == null) {
            onCaptureCancelled()
        } else {
            val out = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)
            onImageCaptured(out.toByteArray())
        }
    }
    return { launcher.launch(null) }
}
