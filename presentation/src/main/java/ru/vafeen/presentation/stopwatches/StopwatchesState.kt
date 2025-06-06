package ru.vafeen.presentation.stopwatches

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана со списком секундомеров.
 *
 * @property stopwatches Список текущих секундомеров.
 * @property timeNow Текущее время в миллисекундах (используется для обновления UI).
 */
internal data class StopwatchesState(
    val stopwatches: List<Stopwatch>,
    val timeNow: Long,
)
