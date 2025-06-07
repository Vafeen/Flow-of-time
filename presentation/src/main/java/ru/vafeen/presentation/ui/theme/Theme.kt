package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


internal data class AppThemeColors(
    val mainColor: Color,
    val background: Color,
    val text: Color,
    val buttonColor: Color,
    val error: Color,
)

private val basePalette = AppThemeColors(
    mainColor = Color(0xFFECEA0E),
    background = Color.White,
    text = Color.Black,
    buttonColor = Color(0xFFF9F9F9),
    error = Color.Red,
)

private val baseDarkPalette = basePalette.copy(
    background = Color.Black,
    text = Color.White,
    buttonColor = Color(0xFF2D2D31)
)


@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colors = if (darkTheme) {
        baseDarkPalette
    } else {
        basePalette
    }

    CompositionLocalProvider(
        LocalColors provides colors,
        content = content
    )
}

internal object AppTheme {
    val colors: AppThemeColors
        @Composable
        get() = LocalColors.current
}

private val LocalColors = staticCompositionLocalOf<AppThemeColors> {
    error("Composition error")
}