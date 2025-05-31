package ru.vafeen.presentation.common.components.bottom_bar

import androidx.compose.ui.graphics.painter.Painter
import ru.vafeen.presentation.common.Screen

/**
 * Модель данных для элемента нижней навигационной панели (Bottom Bar).
 *
 * @property screen Экран, связанный с этим элементом
 * @property icon Иконка для отображения в Bottom Bar
 * @property contentDescription Описание иконки для accessibility (экранных читалок)
 */
internal data class BottomBarItem(
    val screen: Screen,
    val icon: Painter,
    val contentDescription: String,
)
