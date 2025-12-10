package br.com.manieri.amanitamuscaria.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val lightColors = lightColorScheme(
    primary = DeepIndigo,
    onPrimary = Color.White,
    secondary = SoftOrange,
    onSecondary = Color.Black,
    background = SurfaceLight,
    surface = Color.White,
    onSurface = SteelGray
)

private val darkColors = darkColorScheme(
    primary = SoftOrange,
    onPrimary = Color.Black,
    secondary = DeepIndigo,
    onSecondary = Color.White,
    background = SteelGray,
    surface = Color(0xFF1F232B),
    onSurface = Color.White
)

@Composable
fun AmanitaTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) darkColors else lightColors
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
