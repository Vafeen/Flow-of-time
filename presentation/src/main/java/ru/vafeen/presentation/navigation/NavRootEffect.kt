package ru.vafeen.presentation.navigation

import androidx.navigation.NavHostController

/**
 * События (эффекты) навигации, используемые в [NavRootViewModel] для управления навигационным потоком.
 *
 * Представляют действия, которые должны быть выполнены в навигационном контроллере.
 */
internal sealed class NavRootEffect {

    /**
     * Эффект навигации к определённому экрану.
     *
     * @property navigate Лямбда-функция, принимающая [NavHostController] для выполнения навигации.
     */
    data class NavigateToScreen(val navigate: (NavHostController) -> Unit) : NavRootEffect()

    /**
     * Эффект очистки стека навигации.
     */
    data object ClearBackStack : NavRootEffect()

    /**
     * Эффект возврата назад в стеке навигации.
     */
    data object Back : NavRootEffect()
}
