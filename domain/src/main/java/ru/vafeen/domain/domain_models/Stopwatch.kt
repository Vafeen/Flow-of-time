package ru.vafeen.domain.domain_models

import java.time.LocalDateTime

/**
 * Модель доменного слоя, представляющая секундомер.
 *
 * @property id Уникальный идентификатор секундомера.
 * @property ldt Временная метка (LocalDateTime), связанная с секундомером (например, время создания или обновления).
 * @property name Название или метка секундомера.
 */
data class Stopwatch(
    val id: Int,
    val ldt: LocalDateTime,
    val name: String,
)
