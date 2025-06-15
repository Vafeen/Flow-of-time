package ru.vafeen.presentation.stopwatches

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.button.CustomIconButton
import ru.vafeen.presentation.common.utils.subtractDuration
import ru.vafeen.presentation.common.utils.suitableColor
import ru.vafeen.presentation.common.utils.toHHMMSS
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Компонент списка для отображения одного элемента секундомера.
 *
 * @param modifier Модификатор для настройки внешнего вида.
 * @param stopwatch Объект секундомера для отображения.
 * @param timeNow Текущее время в миллисекундах для расчёта прошедшего времени.
 * @param isItCandidateForDeleting Флаг, указывающий, выделен ли элемент для удаления.
 * @param isDeletingInProcess Флаг, указывающий, активен ли режим удаления.
 * @param sendStopwatchesIntent Функция для отправки интентов управления секундомерами.
 */
@Composable
internal fun StopwatchListItem(
    modifier: Modifier = Modifier,
    stopwatch: Stopwatch,
    timeNow: Long,
    isItCandidateForDeleting: Boolean,
    isDeletingInProcess: Boolean,
    sendStopwatchesIntent: (StopwatchesIntent) -> Unit
) {

    Card(
        modifier = modifier.padding(3.dp).let {
            if (isItCandidateForDeleting) it.alpha(0.7f) else it
        },
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.buttonColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        border = if (isItCandidateForDeleting) BorderStroke(
            width = 1.dp,
            color = AppTheme.colors.error
        ) else null,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = {
                        sendStopwatchesIntent(
                            if (isDeletingInProcess) {
                                StopwatchesIntent.ChangeStatusForDeleting(stopwatch)
                            } else {
                                StopwatchesIntent.NavigateTo(id = stopwatch.id)
                            }
                        )
                    },
                    onLongClick = {
                        sendStopwatchesIntent(
                            StopwatchesIntent.ToggleDeletingState(stopwatch)
                        )
                    })
                .padding(8.dp)
        ) {
            TextForThisTheme(
                text = stopwatch.name,
                fontSize = FontSize.big21,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))
            TextForThisTheme(text = stopwatch.stopTime.let { stopTime ->
                if (stopTime != null) {
                    // Если секундомер остановлен, считаем разницу между startTime и stopTime
                    stopwatch.startTime.subtractDuration(stopTime).toHHMMSS()
                } else {
                    // Если секундомер запущен, считаем разницу между startTime и текущим временем
                    stopwatch.startTime.subtractDuration(timeNow).toHHMMSS()
                }
            }, fontSize = FontSize.medium19)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                content = {
                    CustomIconButton(onClick = {
                        sendStopwatchesIntent(StopwatchesIntent.Toggle(stopwatch))
                    }, icon = {
                        Icon(
                            painter = painterResource(
                                if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                            ), contentDescription = stringResource(
                                if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                            ),
                            tint = AppTheme.colors.mainColor.suitableColor()
                        )
                    })

                    if (stopwatch.startTime != stopwatch.stopTime) {
                        Spacer(modifier = Modifier.width(5.dp))
                        CustomIconButton(onClick = {
                            sendStopwatchesIntent(StopwatchesIntent.Reset(stopwatch))
                        }, icon = {
                            Icon(
                                painter = painterResource(R.drawable.reset),
                                contentDescription = stringResource(R.string.reset),
                                tint = AppTheme.colors.mainColor.suitableColor()
                            )
                        })
                    }
                })
        }
    }
}
