package ru.vafeen.domain.domain_models

import java.time.LocalDateTime

data class Timer(
    val id: Int,
    val ldt: LocalDateTime,
    val name: String,
)
