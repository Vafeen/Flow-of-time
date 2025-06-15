package ru.vafeen.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
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
            containerColor = Color.Transparent,
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
                // Обернем текст в Box с Canvas для рисования окружности
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp) // Размер области для круга
                ) {
                    Canvas(modifier = Modifier.matchParentSize()) {
                        drawCircle(
                            color = Color.Gray.copy(alpha = 0.3f),
                            radius = size.minDimension / 2 - 10, // Радиус с отступом
                            style = Stroke(width = 10f) // Контур круга
                        )
                    }
                    TextForThisTheme(
                        text = stopwatch.stopTime.let { stopTime ->
                            if (stopTime != null) {
                                stopwatch.startTime.subtractDuration(stopTime).toHHMMSS()
                            } else {
                                stopwatch.startTime.subtractDuration(timeNow).toHHMMSS()
                            }
                        }
                    )
                }
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