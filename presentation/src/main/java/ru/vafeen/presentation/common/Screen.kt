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
     * @property name Название таймера, передаваемое для отображения или редактирования.
     */
    @Serializable
    data class TimerData(val name: String) : Screen

    /**
     * Экран с деталями секундомера.
     *
     * @property id Идентификатор секундомера, передаваемый для загрузки данных.
     */
    @Serializable
    data class StopwatchData(val id: Int) : Screen
}

/**
 * Список экранов, для которых отображается нижняя навигационная панель (BottomBar).
 */
internal val screenWithBottomBar = listOf(Screen.Stopwatches, Screen.Timers)

/**
 * Функция для определения экрана [Screen] по маршруту навигации [NavBackStackEntry].
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
        else -> null
    }
}
