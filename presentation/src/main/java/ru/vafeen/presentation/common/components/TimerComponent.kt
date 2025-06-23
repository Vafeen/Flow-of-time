package ru.vafeen.presentation.common.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Timer
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.button.TimerButtons
import ru.vafeen.presentation.common.utils.toHHMMSS
import ru.vafeen.presentation.common.utils.withoutMillis
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize
import kotlin.math.absoluteValue

/**
 * Компонент для отображения и управления таймером с визуализацией прогресса.
 *
 * Отображает:
 * - Круговую шкалу прогресса (цветная часть - оставшееся время, серая - прошедшее)
 * - Оставшееся время в формате HH:MM:SS
 * - Кнопки управления (старт/пауза, сброс, удаление)
 * - Возможность переименования по клику на название
 *
 * Особенности:
 * - При отрицательном времени весь круг становится красным
 * - Прогресс отображается в виде убывающей шкалы
 * - Поддерживает режимы работы (активный/на паузе)
 *
 * @param isAddedToDb Флаг, указывающий добавлен ли таймер в базу данных
 * @param timer Объект таймера с текущим состоянием
 * @param timeNow Текущее системное время в миллисекундах
 * @param renamingDialogValue Текущее значение в диалоге переименования
 * @param isRenamingDialogShowed Флаг видимости диалога переименования
 * @param onRenameDialogShow Обработчик открытия диалога переименования
 * @param onDismissRequest Обработчик закрытия диалога
 * @param onSaveRenaming Обработчик сохранения нового имени
 * @param onToggle Обработчик запуска/паузы таймера
 * @param onReset Опциональный обработчик сброса таймера
 * @param onDelete Опциональный обработчик удаления таймера
 */
@Composable
internal fun TimerComponent(
    isAddedToDb: Boolean,
    timer: Timer,
    timeNow: Long,
    renamingDialogValue: String,
    isRenamingDialogShowed: Boolean,
    onRenameDialogShow: () -> Unit,
    onDismissRequest: () -> Unit,
    onSaveRenaming: (String) -> Unit,
    onToggle: () -> Unit,
    onReset: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
) {
    if (isRenamingDialogShowed) {
        RenamingDialog(
            value = renamingDialogValue,
            onDismissRequest = onDismissRequest,
            onSave = onSaveRenaming
        )
    }

    // Вычисление оставшегося времени
    val elapsedMillis = timer.startTime?.let { startTime ->
        if (timer.isRunning) timeNow - startTime else 0L
    } ?: 0L

    val remainingMillis = (timer.remainingTimeMillis - elapsedMillis).withoutMillis()
    val isTimeNegative = remainingMillis < 0
    val progress = if (timer.initialDurationMillis > 0) {
        (remainingMillis.toFloat() / timer.initialDurationMillis.toFloat()).coerceIn(0f, 1f)
    } else 0f

    val timeColor = if (isTimeNegative) AppTheme.colors.error else AppTheme.colors.text
    val activeArcColor = AppTheme.colors.mainColor
    val backgroundArcColor =
        if (isTimeNegative) AppTheme.colors.error else Color.Gray.copy(alpha = 0.3f)

    Scaffold(
        containerColor = Color.Transparent,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRenameDialogShow),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = timer.name,
                    fontSize = FontSize.huge27,
                    color = AppTheme.colors.text
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        },
        bottomBar = {
            TimerButtons(
                modifier = Modifier.fillMaxWidth(),
                timer = timer,
                onToggleClick = onToggle,
                onResetClick = if (timer.isRunning || timer.remainingTimeMillis != timer.initialDurationMillis) onReset else null,
                onDeleteClick = onDelete,
                color = AppTheme.colors.mainColor,
                mainButtonText = if (!isAddedToDb) stringResource(R.string.add_to_db) else null
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 12f
                    val radius = size.minDimension / 2 - strokeWidth / 2

                    withTransform({
                        rotate(-90f, center) // Начинаем отрисовку сверху
                    }) {
                        // Фон (серая часть) - показывает прошедшее время
                        drawArc(
                            color = backgroundArcColor,
                            startAngle = 0f,
                            sweepAngle = 360f * (1f - progress),
                            useCenter = false,
                            topLeft = center - Offset(radius, radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth)
                        )

                        // Активная часть (цветная/красная) - показывает оставшееся время
                        drawArc(
                            color = activeArcColor,
                            startAngle = 360f * (1f - progress),
                            sweepAngle = 360f * progress,
                            useCenter = false,
                            topLeft = center - Offset(radius, radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth)
                        )
                    }
                }

                Text(
                    text = buildString {
                        if (isTimeNegative) append("-")
                        append(remainingMillis.absoluteValue.toHHMMSS())
                    },
                    fontSize = FontSize.huge27,
                    color = timeColor
                )
            }
        }
    }
}