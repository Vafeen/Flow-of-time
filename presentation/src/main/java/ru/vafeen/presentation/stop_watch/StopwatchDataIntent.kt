package ru.vafeen.presentation.stop_watch

internal sealed class StopwatchDataIntent {
    data object ChangeState : StopwatchDataIntent()
}