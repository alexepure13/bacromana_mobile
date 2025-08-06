package com.nxt64software.bacromana.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LightColorScheme = lightColorScheme(
    primary = Color(0xFF0F2A7E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3F5CAA),
    background = Color(0xFFF2F8FF),
    surface = Color.White,
    onBackground = Color(0xFF1C1B1F),
    surfaceVariant = Color(0xFFE5ECF9),
    inverseSurface = Color(0xFF1F2F50)
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF0F2A7E),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF3F5CAA),
    background = Color(0xFF0F1F3E),
    surface = Color(0xFF1F2F50),
    onBackground = Color.White,
    surfaceVariant = Color(0xFF1F2F50),
    inverseSurface = Color(0xFFF2F8FF)
)

private val AppTypography = Typography(
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 20.sp,
        lineHeight = 28.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold
    ),
    headlineSmall = androidx.compose.ui.text.TextStyle(
        fontSize = 18.sp,
        lineHeight = 24.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
    ),
    labelSmall = androidx.compose.ui.text.TextStyle(
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp,
        fontWeight = androidx.compose.ui.text.font.FontWeight.Medium
    )
)

private val AppShapes = Shapes(
    small = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
    medium = androidx.compose.foundation.shape.RoundedCornerShape(12.dp),
    large = androidx.compose.foundation.shape.RoundedCornerShape(16.dp)
)

@Composable
fun BacRomanaTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colors,
        typography = AppTypography,
        shapes = AppShapes,
        content = content
    )
}
