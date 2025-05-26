package ru.vafeen.presentation.navigation

import androidx.navigation.NavHostController

internal sealed class NavRootEffect {
    data class NavigateToScreen(val navigate: (NavHostController) -> Unit) : NavRootEffect()
    data object ClearBackStack : NavRootEffect()
    data object Back : NavRootEffect()
}