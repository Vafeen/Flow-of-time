package ru.vafeen.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.vafeen.domain.utils.launchIO
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.screenWithBottomBar
import javax.inject.Inject

/**
 * ViewModel для управления навигацией в корневом компоненте приложения.
 *
 * Обрабатывает навигационные интенты ([NavRootIntent]), обновляет состояние навигации ([NavRootState])
 * и испускает эффекты навигации ([NavRootEffect]) для выполнения переходов.
 */
@HiltViewModel
internal class NavRootViewModel @Inject constructor() : ViewModel() {

    // Поток эффектов навигации для подписчиков (например, UI)
    private val _effects = MutableSharedFlow<NavRootEffect>()
    val effects = _effects.asSharedFlow()

    // Состояние навигации, доступное для подписки
    private val _state = MutableStateFlow(NavRootState(startScreen = Screen.Stopwatches))
    val state = _state.asStateFlow()

    /**
     * Обработка навигационных интентов.
     *
     * @param intent Навигационное намерение для обработки.
     */
    fun handleIntent(intent: NavRootIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is NavRootIntent.NavigateToScreen -> navigateToScreen(intent.screen)
                is NavRootIntent.NavigateToBottomBarScreen -> navigateToBottomBarScreen(intent.screen)
                NavRootIntent.Back -> back()
                is NavRootIntent.UpdateCurrentScreen -> _state.update { it.copy(currentScreen = intent.screen) }
                NavRootIntent.ClearBackStack -> _effects.emit(NavRootEffect.ClearBackStack)
            }
        }
    }

    /**
     * Навигация на произвольный экран.
     *
     * @param screen Целевой экран для навигации.
     */
    private suspend fun navigateToScreen(screen: Screen) {
        _effects.emit(NavRootEffect.NavigateToScreen { navHostController ->
            navHostController.navigate(screen) {
                launchSingleTop = true
//                restoreState = false
            }
        })
        _state.update {
            it.copy(isBottomBarVisible = screen in screenWithBottomBar)
        }
    }

    /**
     * Навигация на экран нижней панели с очисткой стека до стартового экрана.
     *
     * @param screen Целевой экран для навигации.
     */
    private suspend fun navigateToBottomBarScreen(screen: Screen) {
        _effects.emit(
            NavRootEffect.NavigateToScreen { navHostController ->
                navHostController.navigate(screen) {
//                    popUpTo(navHostController.graph.findStartDestination().id)
                    launchSingleTop = true
//                    restoreState = false
                }
            }
        )
        _state.update {
            it.copy(isBottomBarVisible = screen in screenWithBottomBar)
        }
    }

    /**
     * Выполнить действие возврата назад в навигации.
     */
    private suspend fun back() = _effects.emit(NavRootEffect.Back)
}
