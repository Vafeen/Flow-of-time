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
    val id: Long,
    val name: String,
    val startTime: Long = id,
    val stopTime: Long? = null,
) {
    companion object {
        fun newInstance(): Stopwatch {
            val timeNow = System.currentTimeMillis()
            return Stopwatch(
                id = timeNow,
                name = "stopwatch $timeNow",
                startTime = timeNow,
                stopTime = timeNow
            )
        }
    }
}
