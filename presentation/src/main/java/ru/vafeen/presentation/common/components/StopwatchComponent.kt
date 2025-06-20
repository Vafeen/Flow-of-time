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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.presentation.R
import ru.vafeen.presentation.common.components.button.StopwatchButtons
import ru.vafeen.presentation.common.utils.subtractDuration
import ru.vafeen.presentation.common.utils.toHHMMSS
import ru.vafeen.presentation.ui.theme.AppTheme
import ru.vafeen.presentation.ui.theme.FontSize

/**
 * Компонент для отображения секундомера с визуализацией времени и кнопками управления.
 *
 * @param isAddedToDb Флаг, указывающий на процесс добавления секундомера в базу данных.
 * @param stopwatch Объект секундомера с текущими данными.
 * @param timeNow Текущее системное время в миллисекундах для вычислений.
 * @param renamingDialogValue Значение в диалоге переименования.
 * @param isRenamingDialogShowed Флаг, указывающий на процесс отображения диалога переименования.
 * @param onRenameDialogShow Лямбда для запуска процесса отображения диалога переименования.
 * @param onDismissRequest Лямбда для запуска процесса закрытия диалога.
 * @param onSaveRenaming Лямбда для запуска процесса сохранения нового имени секундомера.
 * @param onToggle Лямбда для запуска процесса переключения состояния секундомера (старт/пауза).
 * @param onReset Опциональная лямбда для запуска процесса сброса секундомера.
 * @param onDelete Опциональная лямбда для запуска процесса удаления секундомера.
 */
@Composable
internal fun StopwatchComponent(
    isAddedToDb: Boolean,
    stopwatch: Stopwatch,
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
    Scaffold(
        containerColor = Color.Transparent, topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onRenameDialogShow),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                TextForThisTheme(
                    text = stopwatch.name,
                    fontSize = FontSize.huge27
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
        }, bottomBar = {
            StopwatchButtons(
                modifier = Modifier.fillMaxWidth(),
                stopwatch = stopwatch,
                onToggleClick = onToggle,
                onResetClick = if (stopwatch.startTime != stopwatch.stopTime) onReset else null,
                onDeleteClick = onDelete,
                color = AppTheme.colors.mainColor,
                mainButtonText = if (!isAddedToDb) stringResource(R.string.add_to_db) else null
            )
        }) { innerPadding ->
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
                    },
                    fontSize = FontSize.huge27
                )
            }

        }
    }
}
