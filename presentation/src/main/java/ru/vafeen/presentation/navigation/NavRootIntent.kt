package ru.vafeen.presentation.navigation

import ru.vafeen.presentation.common.Screen

/**
 * Интенты (намерения) навигации, используемые в [NavRootViewModel] для управления навигационными действиями.
 *
 * Представляют команды или запросы на изменение состояния навигации.
 */
internal sealed class NavRootIntent {

    /**
     * Интент для очистки стека навигации.
     */
    data object ClearBackStack : NavRootIntent()

    /**
     * Интент для навигации на экран, связанный с нижней навигационной панелью (BottomBar).
     *
     * @property screen Целевой экран для навигации.
     */
    data class NavigateToBottomBarScreen(val screen: Screen) : NavRootIntent()

    /**
     * Интент для навигации на произвольный экран.
     *
     * @property screen Целевой экран для навигации.
     */
    data class NavigateToScreen(val screen: Screen) : NavRootIntent()

    /**
     * Интент для обновления текущего активного экрана.
     *
     * @property screen Экран, который стал текущим.
     */
    data class UpdateCurrentScreen(val screen: Screen) : NavRootIntent()

    /**
     * Интент для возврата назад в стеке навигации.
     */
    data object Back : NavRootIntent()
}
