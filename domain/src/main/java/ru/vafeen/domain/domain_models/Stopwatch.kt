package ru.vafeen.domain.domain_models

/**
 * Модель доменного слоя, представляющая секундомер.
 *
 * @property id Уникальный идентификатор секундомера.
 * @property startTime Временная метка (в миллисекундах с эпохи), когда секундомер был запущен.
 * @property name Название или метка секундомера.
 * @property stopTime Временная метка (в миллисекундах с эпохи), когда секундомер был остановлен, либо null, если секундомер работает.
 */
data class Stopwatch(
    val id: Int = 0,
    val startTime: Long,
    val name: String,
    val stopTime: Long? = null,
)
