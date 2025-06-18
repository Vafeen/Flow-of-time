package ru.vafeen.presentation.new_timer

import ru.vafeen.domain.domain_models.Timer

/**
 * Состояние экрана создания нового таймера.
 *
 * @property isAddedToDb Флаг, указывающий, был ли таймер добавлен в базу данных.
 * @property timer Текущий объект таймера.
 * @property timeNow Текущее время в миллисекундах (используется для обновления UI).
 * @property isRenameDialogShowed Текущее состояние диалога переименования (показывается или нет)
 */
internal data class NewTimerDataState(
    val isAddedToDb: Boolean = false,
    val timer: Timer,
    val timeNow: Long,
    val isRenameDialogShowed: Boolean = false,
)