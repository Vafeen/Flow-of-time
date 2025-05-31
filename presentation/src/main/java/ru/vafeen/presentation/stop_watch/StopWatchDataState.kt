package ru.vafeen.presentation.stop_watch

import ru.vafeen.domain.domain_models.Stopwatch

internal data class StopWatchDataState(
    val stopWatch: Stopwatch? = null,
    val timeNow: Long,
    val isStopwatchNotFound: Boolean = false,
    val isLoading: Boolean = true,
)
