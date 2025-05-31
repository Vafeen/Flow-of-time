package ru.vafeen.data.converters

import ru.vafeen.data.database.entity.StopwatchEntity
import ru.vafeen.domain.domain_models.Stopwatch

internal fun StopwatchEntity.toStopWatch() = Stopwatch(
    id = id,
    startTime = startTime,
    name = name,
    stopTime = stopTime,
)

internal fun Stopwatch.toStopWatchEntity() = StopwatchEntity(
    id = id,
    startTime = startTime,
    name = name,
    stopTime = stopTime
)