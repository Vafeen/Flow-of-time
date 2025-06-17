package ru.vafeen.presentation.timers

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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Timer
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.TextForThisTheme
import ru.vafeen.presentation.common.components.button.CustomIconButton
import ru.vafeen.presentation.common.utils.withoutMillis
import ru.vafeen.presentation.common.utils.toHHMMSS
import ru.vafeen.presentation.common.utils.suitableColor
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Компонент списка для отображения одного элемента таймера.
 *
 * Отвечает за визуальное представление таймера с отображением названия,
 * оставшегося времени с подсветкой при отрицательном значении,
 * а также кнопок управления состоянием таймера и режимом удаления.
 *
 * @param modifier Модификатор для настройки внешнего вида компонента.
 * @param timer Объект таймера, данные которого отображаются.
 * @param timeNow Текущее время в миллисекундах, используется для расчёта оставшегося времени.
 * @param isItCandidateForDeleting Флаг, указывающий, выделен ли таймер для удаления (отображается с прозрачностью и рамкой).
 * @param isDeletingInProcess Флаг, указывающий, активен ли режим удаления таймеров.
 * @param sendTimersIntent Функция для отправки интентов управления таймерами (например, переключение, сброс, навигация).
 */
@Composable
internal fun TimerListItem(
    modifier: Modifier = Modifier,
    timer: Timer,
    timeNow: Long,
    isItCandidateForDeleting: Boolean,
    isDeletingInProcess: Boolean,
    sendTimersIntent: (TimersIntent) -> Unit
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
                        sendTimersIntent(
                            if (isDeletingInProcess) {
                                TimersIntent.ChangeStatusForDeleting(timer)
                            } else {
                                TimersIntent.NavigateTo(id = timer.id)
                            }
                        )
                    },
                    onLongClick = {
                        sendTimersIntent(
                            TimersIntent.ToggleDeletingState(timer)
                        )
                    })
                .padding(8.dp)
        ) {
            TextForThisTheme(
                text = timer.name,
                fontSize = FontSize.big21,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
            Spacer(modifier = Modifier.height(20.dp))

            // Вычисление оставшегося времени с возможным отрицательным значением
            val remainingMillis = timer.startTime?.let { startTime ->
                if (timer.isRunning) {
                    timeNow - startTime
                } else {
                    0L
                }
            } ?: 0L
            val displayMillis = (timer.remainingTimeMillis - remainingMillis).withoutMillis()

            Text(
                text =  if (displayMillis < 0) {
                    "-" + (-displayMillis).toHHMMSS()
                } else {
                    displayMillis.toHHMMSS()
                },
                fontSize = FontSize.medium19,
                color = if (displayMillis < 0) AppTheme.colors.error else AppTheme.colors.text
            )

            Spacer(modifier = Modifier.height(20.dp))
            Row {
                CustomIconButton(onClick = {
                    sendTimersIntent(TimersIntent.Toggle(timer))
                }, icon = {
                    Icon(
                        painter = painterResource(
                            if (timer.isRunning) R.drawable.pause else R.drawable.resume
                        ), contentDescription = stringResource(
                            if (timer.isRunning) R.string.pause else R.string.resume
                        ),
                        tint = AppTheme.colors.mainColor.suitableColor()
                    )
                })

                if (timer.isRunning || timer.remainingTimeMillis != timer.initialDurationMillis) {
                    Spacer(modifier = Modifier.width(5.dp))
                    CustomIconButton(onClick = {
                        sendTimersIntent(TimersIntent.Reset(timer))
                    }, icon = {
                        Icon(
                            painter = painterResource(R.drawable.reset),
                            contentDescription = stringResource(R.string.reset),
                            tint = AppTheme.colors.mainColor.suitableColor()
                        )
                    })
                }

            }
        }
    }
}
