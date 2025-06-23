package ru.vafeen.presentation.new_timer

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.common.components.TimerComponent
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * Компонент экрана создания нового таймера.
 *
 * Отображает таймер с кнопкой действия:
 * - если таймер уже добавлен в базу данных, отображается кнопка паузы/возобновления;
 * - если таймер ещё не добавлен, отображается кнопка добавления в базу.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@Composable
internal fun NewTimerDataScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: NewTimerDataViewModel =
        hiltViewModel<NewTimerDataViewModel, NewTimerDataViewModel.Factory> { factory ->
            factory.create(sendRootIntent)
        }
    val state: NewTimerDataState by viewModel.state.collectAsState()

    TimerComponent(
        timer = state.timer,
        timeNow = state.timeNow,
        onToggle = {
            viewModel.handleIntent(
                if (state.isAddedToDb) NewTimerDataIntent.Toggle
                else NewTimerDataIntent.AddAndStart
            )
        },
        onReset = if (state.isAddedToDb) {
            { viewModel.handleIntent(NewTimerDataIntent.Reset) }
        } else null,
        onDelete = if (state.isAddedToDb) {
            { viewModel.handleIntent(NewTimerDataIntent.Delete) }
        } else null,
        isAddedToDb = state.isAddedToDb,
        renamingDialogValue = state.timer.name,
        onRenameDialogShow = {
            viewModel.handleIntent(NewTimerDataIntent.ToggleShowingRenamingDialog(isShowed = true))
        },
        isRenamingDialogShowed = state.isRenameDialogShowed,
        onDismissRequest = {
            viewModel.handleIntent(NewTimerDataIntent.ToggleShowingRenamingDialog(isShowed = false))
        },
        onSaveRenaming = {
            viewModel.handleIntent(NewTimerDataIntent.SaveRenaming(newName = it))
        }
    )
}