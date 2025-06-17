package ru.vafeen.presentation.timers

import ru.vafeen.domain.domain_models.Timer

/**
 * Состояние экрана со списком таймеров.
 *
 * @property timers Список текущих таймеров.
 * @property timeNow Текущее время в миллисекундах (используется для обновления UI).
 * @property timersForDeleting Словарь таймеров, выбранных для удаления, где ключ — идентификатор таймера.
 */
internal data class TimersState(
    val timers: List<Timer> = listOf(),
    val timeNow: Long = System.currentTimeMillis(),
    val timersForDeleting: Map<Long, Timer> = mapOf(),
)
