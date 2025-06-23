package ru.vafeen.presentation.stopwatch

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана секундомера.
 *
 * @property stopwatch Текущий объект секундомера, либо null, если не загружен
 * @property timeNow Текущее время в миллисекундах (для обновления UI)
 * @property isRenameDialogShowed Текущее состояние диалога переименования (показывается или нет)
 */
internal data class StopwatchDataState(
    val stopwatch: Stopwatch? = null,
    val timeNow: Long,
    val isRenameDialogShowed: Boolean = false,
)
