package ru.vafeen.presentation.navigation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavGraph.Companion.findStartDestination
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


@HiltViewModel
internal class NavRootViewModel @Inject constructor() : ViewModel() {
    private val _effects = MutableSharedFlow<NavRootEffect>()
    val effects = _effects.asSharedFlow()
    private val _state = MutableStateFlow(NavRootState(startScreen = Screen.Stopwatches))
    val state = _state.asStateFlow()
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

    private suspend fun navigateToScreen(screen: Screen) {
        _effects.emit(NavRootEffect.NavigateToScreen { navHostController ->
            navHostController.navigate(screen) {
                launchSingleTop = true
            }
        })
        _state.update {
            it.copy(isBottomBarVisible = screen in screenWithBottomBar)
        }
    }

    private suspend fun navigateToBottomBarScreen(screen: Screen) {
        _effects.emit(
            NavRootEffect.NavigateToScreen { navHostController ->
                navHostController.navigate(screen) {
                    popUpTo(navHostController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            }
        )
        _state.update {
            it.copy(isBottomBarVisible = screen in screenWithBottomBar)
        }
    }

    private suspend fun back() = _effects.emit(NavRootEffect.Back)

}