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
        1f - (remainingMillis.toFloat() / timer.initialDurationMillis.toFloat())
    } else 0f

    val timeColor = if (isTimeNegative) AppTheme.colors.error else AppTheme.colors.text
    val arcColor = if (isTimeNegative) AppTheme.colors.error else AppTheme.colors.mainColor

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
                onResetClick = if (timer.remainingTimeMillis != timer.initialDurationMillis) onReset else null,
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
                // Фоновый круг (полная длительность)
                Canvas(modifier = Modifier.matchParentSize()) {
                    val radius = size.minDimension / 2 - 10
                    drawCircle(
                        color = Color.Gray.copy(alpha = 0.3f),
                        radius = radius,
                        style = Stroke(width = 10f)
                    )
                }

                // Прогресс таймера (заполняющаяся дуга)
                Canvas(modifier = Modifier.matchParentSize()) {
                    val strokeWidth = 10f
                    val radius = size.minDimension / 2 - 10 - strokeWidth / 2

                    withTransform({
                        rotate(-90f, center) // Начинаем с верхней точки
                    }) {
                        drawArc(
                            color = arcColor,
                            startAngle = 0f,
                            sweepAngle = 360f * progress.coerceIn(0f, 1f),
                            useCenter = false,
                            topLeft = center - Offset(radius, radius),
                            size = Size(radius * 2, radius * 2),
                            style = Stroke(width = strokeWidth)
                        )
                    }
                }

                // Оставшееся время
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