package ru.vafeen.presentation.new_stopwatch

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.common.components.StopwatchComponent
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * Компонент экрана создания нового секундомера.
 *
 * Отображает секундомер с кнопкой действия:
 * - если секундомер уже добавлен в базу данных, отображается кнопка паузы/возобновления;
 * - если секундомер ещё не добавлен, отображается кнопка добавления в базу.
 *
 * @param sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@Composable
internal fun NewStopwatchDataScreen(sendRootIntent: (NavRootIntent) -> Unit) {
    val viewModel: NewStopwatchDataViewModel =
        hiltViewModel<NewStopwatchDataViewModel, NewStopwatchDataViewModel.Factory>(creationCallback = { factory ->
            factory.create(sendRootIntent)
        })
    val state: NewStopwatchDataState by viewModel.state.collectAsState()

    StopwatchComponent(
        stopwatch = state.stopwatch,
        timeNow = state.timeNow,
        onToggle = {
            viewModel.handleIntent(
                if (state.isAddedToDb) NewStopwatchDataIntent.Toggle
                else NewStopwatchDataIntent.AddAndStart
            )
        },
        onReset = if (state.isAddedToDb) {
            { viewModel.handleIntent(NewStopwatchDataIntent.Reset) }
        } else null,
        onDelete = if (state.isAddedToDb) {
            { viewModel.handleIntent(NewStopwatchDataIntent.Delete) }
        } else null,
        isAddedToDb = state.isAddedToDb,
        renamingDialogValue = state.stopwatch.name,
        onRenameDialogShow = {
            Log.e("toggle", "onRenameDialogShow")
            viewModel.handleIntent(NewStopwatchDataIntent.ToggleShowingRenamingDialog(isShowed = true))
        },
        isRenamingDialogShowed = state.isRenameDialogShowed,
        onDismissRequest = {
            Log.e("toggle", "onDismissRequest")
            viewModel.handleIntent(NewStopwatchDataIntent.ToggleShowingRenamingDialog(isShowed = false))
        },
        onSaveRenaming = {
            viewModel.handleIntent(NewStopwatchDataIntent.SaveRenaming(it))
        }
    )
}
