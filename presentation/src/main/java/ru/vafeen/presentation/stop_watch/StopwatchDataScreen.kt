package ru.vafeen.presentation.stop_watch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.components.StopwatchComponent
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * Экран отображения данных секундомера.
 *
 * Отвечает за отображение секундомера с возможностью управления его состоянием.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 * @param stopwatchData Объект с параметрами для экрана, включая идентификатор секундомера.
 */
@Composable
internal fun StopwatchDataScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
    stopwatchData: Screen.StopwatchData
) {

    val viewModel =
        hiltViewModel<StopwatchDataViewModel, StopwatchDataViewModel.Factory> { factory ->
            factory.create(
                id = stopwatchData.id,
                sendRootIntent = sendRootIntent
            )
        }

    // Подписка на состояние ViewModel
    val state by viewModel.state.collectAsState()
    state.stopwatch?.let { stopwatch ->
        StopwatchComponent(
            stopwatch = stopwatch,
            timeNow = state.timeNow,
            onToggle = { viewModel.handleIntent(StopwatchDataIntent.Toggle) },
            onReset = { viewModel.handleIntent(StopwatchDataIntent.Reset) },
            onDelete = { viewModel.handleIntent(StopwatchDataIntent.Delete) },
            isAddedToDb = true
        )
    }
}
