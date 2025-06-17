package ru.vafeen.domain.domain_models

/**
 * Модель доменного слоя, представляющая таймер с установленной длительностью и текущим состоянием.
 *
 * @property id Уникальный идентификатор таймера.
 * @property name Название или описание таймера.
 * @property initialDurationMillis Изначально установленная длительность таймера в миллисекундах.
 * @property remainingTimeMillis Текущее оставшееся время таймера в миллисекундах.
 * @property isRunning Флаг, указывающий на процесс работы таймера (true — таймер запущен).
 * @property startTime Временная метка запуска таймера в миллисекундах с эпохи, или null если таймер не запущен.
 */
data class Timer(
    val id: Long,
    val name: String,
    val initialDurationMillis: Long,
    val remainingTimeMillis: Long,
    val isRunning: Boolean,
    val startTime: Long? = null,
)
