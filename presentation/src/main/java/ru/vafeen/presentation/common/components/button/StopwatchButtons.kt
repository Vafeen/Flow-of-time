package ru.vafeen.presentation.common.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.utils.suitableColor
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Компонент, отображающий кнопки управления секундомером.
 *
 * @param modifier Модификатор для настройки внешнего вида и поведения компонента.
 * @param stopwatch Объект секундомера, определяющий текущее состояние.
 * @param mainButtonText Текст, который может быть добавлен для основной кнопки
 * @param onToggleClick Лямбда, вызываемая при нажатии на основную кнопку переключения состояния секундомера.
 * @param onResetClick Опциональная лямбда, вызываемая при нажатии на кнопку сброса секундомера.
 * @param onDeleteClick Опциональная лямбда, вызываемая при нажатии на кнопку удаления секундомера.
 * @param color Цвет, используемый для стилизации кнопок и иконок.
 */
@Composable
fun StopwatchButtons(
    modifier: Modifier = Modifier,
    stopwatch: Stopwatch,
    mainButtonText: String?,
    onToggleClick: () -> Unit,
    onResetClick: (() -> Unit)? = null,
    onDeleteClick: (() -> Unit)? = null,
    color: Color,
) {
    val iconSize = 50.dp
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        onResetClick?.let { onResetClick ->
            CustomIconButton(
                modifier = Modifier.size(70.dp),
                onClick = onResetClick, icon = {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(R.drawable.reset),
                        contentDescription = stringResource(R.string.reset),
                        tint = color.suitableColor()
                    )
                })

        }
        Button(
            modifier = Modifier
                .height(70.dp),
            onClick = onToggleClick,
            colors = ButtonDefaults.buttonColors(containerColor = color)
        ) {
            mainButtonText?.let { mainButtonText ->
                Text(
                    text = mainButtonText,
                    color = color.suitableColor(),
                    fontSize = FontSize.big21
                )
            }
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(
                    if (stopwatch.stopTime != null) R.drawable.resume else R.drawable.pause
                ),
                contentDescription = stringResource(
                    if (stopwatch.stopTime != null) R.string.resume else R.string.pause
                ),
                tint = color.suitableColor()
            )
        }
        onDeleteClick?.let { onDeleteClick ->
            CustomIconButton(
                modifier = Modifier.size(70.dp),
                onClick = onDeleteClick, icon = {
                    Icon(
                        modifier = Modifier.size(iconSize),
                        painter = painterResource(R.drawable.delete),
                        contentDescription = stringResource(R.string.delete),
                        tint = color.suitableColor()
                    )
                })

        }

    }
}
