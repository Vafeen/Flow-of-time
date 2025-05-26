package ru.vafeen.presentation.navigation

import ru.vafeen.presentation.common.Screen

internal data class NavRootState(
    val startScreen: Screen,
    val currentScreen: Screen = startScreen,
    val isBottomBarVisible: Boolean = true
)