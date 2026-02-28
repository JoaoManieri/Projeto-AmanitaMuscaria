package br.com.manieri.amanitamuscaria.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Immutable
data class AutoCheckTokens(
    val canvasBackground: Color,
    val sidebarBackground: Color,
    val sidebarActiveBackground: Color,
    val sidebarAccent: Color,
    val sidebarInactive: Color,
    val cardBackground: Color,
    val border: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val statusBlue: Color,
    val statusOrange: Color,
    val statusGreen: Color,
    val radiusMd: androidx.compose.ui.unit.Dp,
    val radiusLg: androidx.compose.ui.unit.Dp,
    val sidebarWidth: androidx.compose.ui.unit.Dp,
)

private val lightTokens = AutoCheckTokens(
    canvasBackground = Color(0xFFF7FAFC),
    sidebarBackground = Color(0xFF1A365D),
    sidebarActiveBackground = Color(0xFF2C5282),
    sidebarAccent = Color(0xFF3182CE),
    sidebarInactive = Color(0xFFA0AEC0),
    cardBackground = Color(0xFFFFFFFF),
    border = Color(0xFFE2E8F0),
    textPrimary = Color(0xFF1A202C),
    textSecondary = Color(0xFF718096),
    statusBlue = Color(0xFF3182CE),
    statusOrange = Color(0xFFDD6B20),
    statusGreen = Color(0xFF38A169),
    radiusMd = 10.dp,
    radiusLg = 12.dp,
    sidebarWidth = 80.dp,
)

val LocalAutoCheckTokens = staticCompositionLocalOf { lightTokens }

private val AutoCheckColorScheme = lightColorScheme(
    background = Color(0xFFFFFFFF),
    surface = Color(0xFFFFFFFF),
    primary = Color(0xFF1A365D),
    secondary = Color(0xFF2C5282),
    tertiary = Color(0xFF3182CE),
    onBackground = Color(0xFF1A202C),
    onSurface = Color(0xFF1A202C),
    onPrimary = Color(0xFFFFFFFF),
    onSecondary = Color(0xFFFFFFFF),
    outline = Color(0xFFE2E8F0),
)

private val AutoCheckTypography = Typography(
    displayLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 32.sp,
        lineHeight = 38.sp,
    ),
    headlineLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
        lineHeight = 31.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 20.sp,
        lineHeight = 28.sp,
    ),
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 18.sp,
        lineHeight = 25.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 21.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp,
        lineHeight = 16.sp,
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
    ),
)

@Composable
fun AutoCheckTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalAutoCheckTokens provides lightTokens) {
        MaterialTheme(
            colorScheme = AutoCheckColorScheme,
            typography = AutoCheckTypography,
            content = content,
        )
    }
}
