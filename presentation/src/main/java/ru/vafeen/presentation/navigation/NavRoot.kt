package ru.vafeen.presentation.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.components.bottom_bar.BottomBar
import ru.vafeen.presentation.common.components.bottom_bar.BottomBarItem
import ru.vafeen.presentation.common.getScreenFromRoute
import ru.vafeen.presentation.new_stopwatch.NewStopwatchDataScreen
import ru.vafeen.presentation.new_timer.NewTimerDataScreen
import ru.vafeen.presentation.stopwatch.StopwatchDataScreen
import ru.vafeen.presentation.stopwatches.StopwatchesScreen
import ru.vafeen.presentation.timer.TimerDataScreen
import ru.vafeen.presentation.timers.TimersScreen
import ru.vafeen.presentation.ui.theme.AppTheme

/**
 * Корневой навигационный компонент приложения, реализованный с использованием Jetpack Compose Navigation.
 *
 * Отвечает за управление навигацией между экранами, отображение нижней навигационной панели (BottomBar),
 * а также обработку эффектов и состояний навигации через ViewModel [NavRootViewModel].
 */
@Composable
internal fun NavRoot() {
    val viewModel: NavRootViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()

    // Отслеживание изменений в стеке навигации и обновление текущего экрана в ViewModel
    LaunchedEffect(null) {
        navController.currentBackStackEntryFlow.collect { backStackEntry ->
            val currentScreen = getScreenFromRoute(backStackEntry) ?: return@collect
            viewModel.handleIntent(NavRootIntent.UpdateCurrentScreen(currentScreen))
        }
    }

    // Обработка эффектов навигации, исходящих из ViewModel
    LaunchedEffect(null) {
        viewModel.effects.collect { effect ->
            when (effect) {
                NavRootEffect.Back -> navController.popBackStack()
                is NavRootEffect.NavigateToScreen -> effect.navigate(navController)
                NavRootEffect.ClearBackStack -> navController.navigateUp()
            }
        }
    }

    Scaffold(
        containerColor = AppTheme.colors.background,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            // Определение элементов нижней навигационной панели
            val bottomBarScreens: List<BottomBarItem> = listOf(
                BottomBarItem(
                    screen = Screen.Stopwatches,
                    icon = painterResource(R.drawable.stop_watch),
                    contentDescription = stringResource(R.string.stop_watch)
                ),
                BottomBarItem(
                    screen = Screen.Timers,
                    icon = painterResource(R.drawable.timer),
                    contentDescription = stringResource(R.string.timer)
                )
            )

            // Отображение BottomBar только на определённых экранах
            if (state.currentScreen in bottomBarScreens.map { it.screen }) {
                BottomBar(
                    containerColor = AppTheme.colors.mainColor,
                    currentScreen = state.currentScreen,
                    screens = bottomBarScreens,
                    navigateTo = { screen ->
                        viewModel.handleIntent(NavRootIntent.NavigateToBottomBarScreen(screen))
                    }
                )
            }
        }
    ) { paddingValues ->
        val tween = tween<Float>(durationMillis = 0)
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = state.startScreen,
            enterTransition = { fadeIn(animationSpec = tween) },
            exitTransition = { fadeOut(animationSpec = tween) },
            popEnterTransition = { fadeIn(animationSpec = tween) },
            popExitTransition = { fadeOut(animationSpec = tween) },
        ) {

            composable<Screen.Timers> {
                TimersScreen(sendRootIntent = viewModel::handleIntent)
            }
            composable<Screen.Stopwatches> {
                StopwatchesScreen(sendRootIntent = viewModel::handleIntent)
            }
            composable<Screen.StopwatchData> {
                StopwatchDataScreen(
                    sendRootIntent = viewModel::handleIntent,
                    stopwatchData = it.toRoute()
                )
            }
            composable<Screen.TimerData> {
                TimerDataScreen(
                    sendRootIntent = viewModel::handleIntent,
                    timerData = it.toRoute()
                )
            }
            composable<Screen.NewStopwatchData> {
                NewStopwatchDataScreen(sendRootIntent = viewModel::handleIntent)
            }
            composable<Screen.NewTimerData> {
                NewTimerDataScreen(sendRootIntent = viewModel::handleIntent)
            }
        }
    }
}
