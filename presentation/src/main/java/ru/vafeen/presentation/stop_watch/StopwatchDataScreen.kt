package ru.vafeen.presentation.stop_watch

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.components.CircularLoading
import ru.vafeen.presentation.common.components.Error
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.utils.subtractDuration
import ru.vafeen.presentation.common.utils.toHHMMSS
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

    @Composable
    fun content() {
        Scaffold(
            floatingActionButton = {
                state.stopWatch?.let { stopwatch ->
                    FloatingActionButton(
                        onClick = { viewModel.handleIntent(StopwatchDataIntent.ChangeState) }) {
                        Icon(
                            painter = painterResource(
                                if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                            ), contentDescription = stringResource(
                                if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                            )
                        )
                    }
                }
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding.calculateTopPadding()),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                state.stopWatch?.let { stopWatch ->
                    TextForThisTheme(stopWatch.name)
                    Spacer(modifier = Modifier.height(20.dp))
                    TextForThisTheme(
                        text = state.stopWatch?.stopTime.let { time ->
                            if (time != null) {
//                             Если секундомер остановлен, считаем разницу между startTime и stopTime
                                stopWatch.startTime.subtractDuration(time).toHHMMSS()
                            } else {
//                             Если секундомер запущен, считаем разницу между startTime и текущим временем
                                stopWatch.startTime.subtractDuration(state.timeNow).toHHMMSS()
                            }
                        })
                }
            }
        }
    }

// Отображение состояний загрузки, ошибки или основного контента
    when {
        state.isLoading -> CircularLoading()
        state.isStopwatchNotFound -> Error(error = "StopWatchNotFound")
        else -> content()
    }
}
