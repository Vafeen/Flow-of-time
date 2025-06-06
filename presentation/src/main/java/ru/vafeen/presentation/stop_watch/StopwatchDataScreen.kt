package ru.vafeen.presentation.stop_watch

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.components.StopwatchComponent
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * Экран отображения данных секундомера.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор
 * @param stopwatchData Объект с параметрами для экрана, включая id секундомера
 */
@Composable
internal fun StopwatchDataScreen(
    sendRootIntent: (NavRootIntent) -> Unit, stopwatchData: Screen.StopwatchData
) {

    val viewModel =
        hiltViewModel<StopwatchDataViewModel, StopwatchDataViewModel.Factory> { factory ->
            factory.create(stopwatchData.id)
        }

    // Подписка на состояние ViewModel
    val state by viewModel.state.collectAsState()
    StopwatchComponent(
        stopwatch = state.stopwatch,
        fab = {
            state.stopwatch?.let { stopwatch ->
                FloatingActionButton(
                    onClick = { viewModel.handleIntent(StopwatchDataIntent.Toggle) }
                ) {
                    Icon(
                        painter = painterResource(
                            if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                        ), contentDescription = stringResource(
                            if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                        )
                    )
                }
            }
        },
        timeNow = state.timeNow,
        isLoading = state.isLoading,
        isStopwatchNotFound = state.isStopwatchNotFound
    )
}
