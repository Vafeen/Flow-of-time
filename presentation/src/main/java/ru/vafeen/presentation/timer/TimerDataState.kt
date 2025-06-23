package ru.vafeen.presentation.timer

import ru.vafeen.domain.domain_models.Timer

/**
 * Состояние экрана таймера.
 *
 * @property timer Текущий объект таймера, либо null, если не загружен.
 * @property timeNow Текущее время в миллисекундах (для обновления UI).
 * @property isRenameDialogShowed Текущее состояние диалога переименования (показывается или нет).
 */
internal data class TimerDataState(
    val timer: Timer? = null,
    val timeNow: Long,
    val isRenameDialogShowed: Boolean = false,
)