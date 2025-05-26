package ru.vafeen.presentation.common

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

internal sealed interface Screen {
    @Serializable
    data object Stopwatches : Screen

    @Serializable
    data object Timers : Screen

    @Serializable
    data class TimerData(val name: String) : Screen

    @Serializable
    data class StopwatchData(val name: String) : Screen
}

internal val screenWithBottomBar = listOf(Screen.Stopwatches, Screen.Timers)

internal fun getScreenFromRoute(navBackStackEntry: NavBackStackEntry): Screen? {
    val route = navBackStackEntry.destination.route ?: return null
    return when {
        route == Screen.Stopwatches::class.qualifiedName -> Screen.Stopwatches
        route == Screen.Timers::class.qualifiedName -> Screen.Timers
        route.startsWith("${Screen.TimerData::class.qualifiedName}") -> navBackStackEntry.toRoute<Screen.TimerData>()
        route.startsWith("${Screen.StopwatchData::class.qualifiedName}") -> navBackStackEntry.toRoute<Screen.StopwatchData>()
        else -> null
    }
}


