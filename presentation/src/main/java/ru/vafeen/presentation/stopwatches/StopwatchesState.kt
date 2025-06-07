package ru.vafeen.presentation.stopwatches

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Состояние экрана со списком секундомеров.
 *
 * @property stopwatches Список текущих секундомеров.
 * @property timeNow Текущее время в миллисекундах (используется для обновления UI).
 * @property stopwatchesForDeleting Словарь секундомеров, выбранных для удаления, где ключ — идентификатор секундомера.
 */
internal data class StopwatchesState(
    val stopwatches: List<Stopwatch> = listOf(),
    val timeNow: Long = System.currentTimeMillis(),
    val stopwatchesForDeleting: Map<Long, Stopwatch> = mapOf(),
)
