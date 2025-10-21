package com.example.levelupgamer.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFB12A2A),       // rojo principal
    secondary = Color(0xFF1E90FF),     // azul neón
    tertiary = Color(0xFF39FF14),      // verde neón
    background = Color(0xFF0B0C10),    // fondo principal
    surface = Color(0xFF101217),       // fondo de tarjetas
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFFD3D3D3),
    onSurface = Color.White
)

@Composable
fun LevelUpGamerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography(
            bodyLarge = LocalTextStyle.current.copy(color = Color.White),
            titleLarge = LocalTextStyle.current.copy(color = Color.White)
        ),
        content = content
    )
}
