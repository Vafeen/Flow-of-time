package ru.vafeen.presentation.stop_watch

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана секундомера.
 *
 * @property stopWatch Текущий объект секундомера, либо null, если не загружен
 * @property timeNow Текущее время в миллисекундах (для обновления UI)
 * @property isStopwatchNotFound Флаг, указывающий, что секундомер с данным id не найден
 * @property isLoading Флаг загрузки данных секундомера
 */
internal data class StopwatchDataState(
    val stopWatch: Stopwatch? = null,
    val timeNow: Long,
    val isStopwatchNotFound: Boolean = false,
    val isLoading: Boolean = true,
)
