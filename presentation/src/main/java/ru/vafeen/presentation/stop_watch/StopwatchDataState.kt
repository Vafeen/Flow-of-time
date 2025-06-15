package ru.vafeen.presentation.stop_watch

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана секундомера.
 *
 * @property stopwatch Текущий объект секундомера, либо null, если не загружен
 * @property timeNow Текущее время в миллисекундах (для обновления UI)
 */
internal data class StopwatchDataState(
    val stopwatch: Stopwatch? = null,
    val timeNow: Long,
)
