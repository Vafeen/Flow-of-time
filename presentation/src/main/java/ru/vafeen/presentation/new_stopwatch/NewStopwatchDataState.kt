package ru.vafeen.presentation.new_stopwatch

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана создания нового секундомера.
 *
 * @property isAddedToDb Флаг, указывающий, был ли секундомер добавлен в базу данных.
 * @property stopwatch Текущий объект секундомера.
 * @property timeNow Текущее время в миллисекундах (используется для обновления UI).
 */
internal data class NewStopwatchDataState(
    val isAddedToDb: Boolean = false,
    val stopwatch: Stopwatch,
    val timeNow: Long,
)
