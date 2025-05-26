package ru.vafeen.presentation.navigation

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
import ru.vafeen.presentation.stop_watch.StopWatchDataScreen
import ru.vafeen.presentation.stopwatches.StopwatchesScreen
import ru.vafeen.presentation.timer_data.TimerDataScreen
import ru.vafeen.presentation.timers.TimersScreen

@Composable
internal fun NavRoot() {
    val viewModel: NavRootViewModel = hiltViewModel()
    val state by viewModel.state.collectAsState()
    val navController = rememberNavController()
    LaunchedEffect(null) {
        navController.currentBackStackEntryFlow.collect {
            val currentScreen = getScreenFromRoute(it)
                ?: return@collect
            viewModel.handleIntent(NavRootIntent.UpdateCurrentScreen(currentScreen))
        }
    }
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
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            val bottomBarScreens: List<BottomBarItem> =
                listOf(
                    BottomBarItem(
                        screen = Screen.Stopwatches,
                        icon = painterResource(R.drawable.stop_watch),
                        contentDescription = stringResource(R.string.stop_watch)
                    ), BottomBarItem(
                        screen = Screen.Timers,
                        icon = painterResource(R.drawable.timer),
                        contentDescription = stringResource(R.string.timer)
                    )
                )

            if (state.currentScreen in bottomBarScreens.map { it.screen }) {
                BottomBar(
                    currentScreen = state.currentScreen,
                    screens = bottomBarScreens,
                    navigateTo = { screen ->
                        viewModel.handleIntent(NavRootIntent.NavigateToBottomBarScreen(screen))
                    }
                )
            }
        }) { paddingValues ->
        NavHost(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            navController = navController,
            startDestination = state.startScreen
        ) {
            composable<Screen.Timers> {
                TimersScreen(sendRootIntent = viewModel::handleIntent)
            }
            composable<Screen.Stopwatches> {
                StopwatchesScreen(sendRootIntent = viewModel::handleIntent)
            }
            composable<Screen.StopwatchData> {
                StopWatchDataScreen(
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
        }
    }
}