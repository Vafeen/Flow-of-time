package ru.vafeen.presentation.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.presentation.common.utils.subtractDuration
import ru.vafeen.presentation.common.utils.toHHMMSS

@Composable
internal fun StopwatchComponent(
    stopwatch: Stopwatch?,
    fab: (@Composable () -> Unit)? = null,
    timeNow: Long,
    isLoading: Boolean? = null,
    isStopwatchNotFound: Boolean? = null,
) {

    @Composable
    fun content(stopwatch: Stopwatch) {
        Scaffold(
            floatingActionButton = {
                fab?.invoke()
            }) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding.calculateTopPadding()),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                TextForThisTheme(stopwatch.name)
                Spacer(modifier = Modifier.height(20.dp))
                TextForThisTheme(
                    text = stopwatch.stopTime.let { stopTime ->
                        if (stopTime != null) {
//                             Если секундомер остановлен, считаем разницу между startTime и stopTime
                            stopwatch.startTime.subtractDuration(stopTime).toHHMMSS()
                        } else {
//                             Если секундомер запущен, считаем разницу между startTime и текущим временем
                            stopwatch.startTime.subtractDuration(timeNow).toHHMMSS()
                        }
                    })
            }
        }
    }

    // Отображение состояний загрузки, ошибки или основного контента
    when {
        isLoading == true -> CircularLoading()
        isStopwatchNotFound == true -> Error(error = "StopWatchNotFound")
        else -> stopwatch?.let { content(it) }
    }
}