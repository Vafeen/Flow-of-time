package ru.vafeen.presentation.navigation

import ru.vafeen.presentation.common.Screen

internal sealed class NavRootIntent {
    data object ClearBackStack : NavRootIntent()
    data class NavigateToBottomBarScreen(val screen: Screen) : NavRootIntent()
    data class NavigateToScreen(val screen: Screen) : NavRootIntent()
    data class UpdateCurrentScreen(val screen: Screen) : NavRootIntent()
    data object Back : NavRootIntent()

}