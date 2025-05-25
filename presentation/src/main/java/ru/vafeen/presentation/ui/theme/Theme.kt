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
    mainColor = Color(0xFF0E568D),
    background = Color.White,
    text = Color.Black,
    buttonColor = Color.DarkGray
)

private val baseDarkPalette = basePalette.copy(background = Color.Black)


@Composable
fun MainTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {

    val colors = if (darkTheme) {
        basePalette
    } else {
        baseDarkPalette
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