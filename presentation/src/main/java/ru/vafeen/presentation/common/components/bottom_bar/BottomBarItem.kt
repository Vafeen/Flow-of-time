package ru.vafeen.presentation.common.components.bottom_bar

import androidx.compose.ui.graphics.painter.Painter
import ru.vafeen.presentation.common.Screen

internal data class BottomBarItem(
    val screen: Screen,
    val icon: Painter,
    val contentDescription: String,
)
