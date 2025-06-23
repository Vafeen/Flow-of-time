package ru.vafeen.presentation.timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.components.TimerComponent
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * Экран отображения данных таймера.
 *
 * Отвечает за отображение таймера с возможностью управления его состоянием.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 * @param timerData Объект с параметрами для экрана, включая идентификатор таймера.
 */
@Composable
internal fun TimerDataScreen(
    sendRootIntent: (NavRootIntent) -> Unit,
    timerData: Screen.TimerData
) {
    val viewModel: TimerDataViewModel =
        hiltViewModel<TimerDataViewModel, TimerDataViewModel.Factory> { factory ->
            factory.create(
                id = timerData.id,
                sendRootIntent = sendRootIntent
            )
        }

    val state: TimerDataState by viewModel.state.collectAsState()
    state.timer?.let { timer ->
        TimerComponent(
            timer = timer,
            timeNow = state.timeNow,
            onToggle = { viewModel.handleIntent(TimerDataIntent.Toggle) },
            onReset = { viewModel.handleIntent(TimerDataIntent.Reset) },
            onDelete = { viewModel.handleIntent(TimerDataIntent.Delete) },
            isAddedToDb = true,
            renamingDialogValue = timer.name,
            isRenamingDialogShowed = state.isRenameDialogShowed,
            onRenameDialogShow = {
                viewModel.handleIntent(TimerDataIntent.ToggleShowingRenamingDialog(isShowed = true))
            },
            onDismissRequest = {
                viewModel.handleIntent(TimerDataIntent.ToggleShowingRenamingDialog(isShowed = false))
            },
            onSaveRenaming = {
                viewModel.handleIntent(TimerDataIntent.SaveRenaming(newName = it))
            }
        )
    }
}