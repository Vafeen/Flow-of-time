package ru.vafeen.presentation.navigation

import ru.vafeen.presentation.common.Screen

/**
 * Состояние навигационного корня (NavRoot), хранящее текущие параметры навигации.
 *
 * @property startScreen Экран, с которого начинается навигация.
 * @property currentScreen Текущий активный экран. По умолчанию равен [startScreen].
 * @property isBottomBarVisible Флаг, указывающий, видима ли нижняя навигационная панель (BottomBar).
 */
internal data class NavRootState(
    val startScreen: Screen,
    val currentScreen: Screen = startScreen,
    val isBottomBarVisible: Boolean = true
)
