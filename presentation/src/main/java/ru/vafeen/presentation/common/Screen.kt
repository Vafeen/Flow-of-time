package ru.vafeen.presentation.common

import androidx.navigation.NavBackStackEntry
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

/**
 * Секалированный интерфейс, описывающий экраны приложения.
 *
 * Используется для навигации между различными экранами с возможностью передачи данных.
 */
internal sealed interface Screen {

    /**
     * Экран со списком секундомеров.
     */
    @Serializable
    data object Stopwatches : Screen

    /**
     * Экран со списком таймеров.
     */
    @Serializable
    data object Timers : Screen

    /**
     * Экран с деталями таймера.
     *
     * @property id Идентификатор таймера, передаваемый для загрузки данных.
     */
    @Serializable
    data class TimerData(val id: Long) : Screen

    /**
     * Экран с деталями секундомера.
     *
     * @property id Идентификатор секундомера, передаваемый для загрузки данных.
     */
    @Serializable
    data class StopwatchData(val id: Long) : Screen

    /**
     * Экран для создания нового секундомера.
     */
    @Serializable
    data object NewStopwatchData : Screen

    /**
     * Экран для создания нового таймера.
     */
    @Serializable
    data object NewTimerData : Screen
}

/**
 * Список экранов, для которых отображается нижняя навигационная панель (BottomBar).
 */
internal val screenWithBottomBar = listOf(Screen.Stopwatches, Screen.Timers)

/**
 * Определяет экран [Screen] по маршруту навигации из [NavBackStackEntry].
 *
 * @param navBackStackEntry Текущая запись в стеке навигации.
 * @return Соответствующий экран [Screen] или null, если маршрут не распознан.
 */
internal fun getScreenFromRoute(navBackStackEntry: NavBackStackEntry): Screen? {
    val route = navBackStackEntry.destination.route ?: return null
    return when {
        route == Screen.Stopwatches::class.qualifiedName -> Screen.Stopwatches
        route == Screen.Timers::class.qualifiedName -> Screen.Timers
        route.startsWith("${Screen.TimerData::class.qualifiedName}") -> navBackStackEntry.toRoute<Screen.TimerData>()
        route.startsWith("${Screen.StopwatchData::class.qualifiedName}") -> navBackStackEntry.toRoute<Screen.StopwatchData>()
        route == Screen.NewStopwatchData::class.qualifiedName -> Screen.NewStopwatchData
        route == Screen.NewTimerData::class.qualifiedName -> Screen.NewTimerData
        else -> null
    }
}
