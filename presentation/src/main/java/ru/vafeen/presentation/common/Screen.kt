package ru.vafeen.presentation.common

import androidx.navigation.NavBackStackEntry
import kotlinx.serialization.Serializable

internal sealed interface Screen {
    @Serializable
    data object Stopwatches : Screen

    @Serializable
    data object Timers : Screen
}

internal val screenWithBottomBar = listOf(Screen.Stopwatches, Screen.Timers)
internal fun getScreenFromRoute(navBackStackEntry: NavBackStackEntry): Screen? {
    val route = navBackStackEntry.destination.route ?: return null
    return when {
        route == Screen.Stopwatches::class.qualifiedName -> Screen.Stopwatches
        route == Screen.Timers::class.qualifiedName -> Screen.Timers
        else -> null
    }
}


