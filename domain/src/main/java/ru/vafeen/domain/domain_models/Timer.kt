package ru.vafeen.domain.domain_models

import java.time.LocalDateTime

/**
 * Модель доменного слоя, представляющая таймер.
 *
 * @property id Уникальный идентификатор таймера.
 * @property ldt Временная метка (LocalDateTime), связанная с таймером (например, время создания или обновления).
 * @property name Название или описание таймера.
 * @property isWork Флаг, указывающий, находится ли таймер в рабочем состоянии (true) или нет (false).
 */
data class Timer(
    val id: Int,
    val ldt: LocalDateTime,
    val name: String,
    val isWork: Boolean,
)
