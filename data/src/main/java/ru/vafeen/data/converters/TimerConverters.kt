package ru.vafeen.data.converters

import ru.vafeen.data.database.entity.TimerEntity
import ru.vafeen.domain.domain_models.Timer

internal fun TimerEntity.toTimer() = Timer(
    id = id,
    ldt = ldt,
    name = name
)

internal fun Timer.toTimerEntity() = TimerEntity(
    id = id,
    ldt = ldt,
    name = name
)