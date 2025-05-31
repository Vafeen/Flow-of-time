package ru.vafeen.domain.domain_models

/**
 * Модель доменного слоя, представляющая секундомер.
 *
 * @property id Уникальный идентификатор секундомера.
 * @property ldt Временная метка (LocalDateTime), связанная с секундомером (например, время создания или обновления).
 * @property name Название или метка секундомера.
 * @property isWork Флаг, указывающий, находится ли секундомер в рабочем состоянии (true) или нет (false).
 */
data class Stopwatch(
    val id: Int = 0,
    val startTime: Long,
    val name: String,
    val stopTime: Long? = null,
)
