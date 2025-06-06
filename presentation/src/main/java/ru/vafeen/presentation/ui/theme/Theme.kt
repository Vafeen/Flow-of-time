package ru.vafeen.presentation.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color


internal data class ScheduleColors(
    val mainColor: Color,
    val background: Color,
    val text: Color,
    val buttonColor: Color,
)

private val basePalette = ScheduleColors(
    mainColor = Color(0xFFECEA0E),
    background = Color.White,
    text = Color.Black,
    buttonColor = Color(0xFFF9F9F9)
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
    val colors: ScheduleColors
        @Composable
        get() = LocalColors.current
}

private val LocalColors = staticCompositionLocalOf<ScheduleColors> {
    error("Composition error")
}