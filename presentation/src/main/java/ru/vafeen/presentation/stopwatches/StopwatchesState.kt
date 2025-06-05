package ru.vafeen.presentation.stopwatches

import ru.vafeen.domain.domain_models.Stopwatch

internal data class StopwatchesState(
    val stopwatches: List<Stopwatch>,
    val timeNow: Long,
)

