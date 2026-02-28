package br.com.manieri.amanitamuscaria.platform

import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale

@Composable
actual fun PlatformPhotoPreview(
    imageBytes: ByteArray?,
    modifier: Modifier,
) {
    val bitmap = imageBytes?.let { BitmapFactory.decodeByteArray(it, 0, it.size) }
    if (bitmap == null) {
        Box(
            modifier = modifier.background(Color(0xFFF3F4F6)),
            contentAlignment = Alignment.Center,
        ) {
            Text("Sem foto")
        }
    } else {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = null,
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
