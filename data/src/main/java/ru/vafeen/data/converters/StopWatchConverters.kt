package ru.vafeen.data.converters

import ru.vafeen.data.database.entity.StopwatchEntity
import ru.vafeen.domain.domain_models.Stopwatch

internal fun StopwatchEntity.toStopWatch() = Stopwatch(
    id = id,
    ldt = ldt,
    name = name
)

internal fun Stopwatch.toStopWatchEntity() = StopwatchEntity(
    id = id,
    ldt = ldt,
    name = name
)