package ru.vafeen.presentation.new_stopwatch

/**
 * Интенты (намерения) для управления состоянием секундомера.
 */
internal sealed class NewStopwatchDataIntent {
    /**
     * Интент для переключения состояния секундомера (старт/пауза).
     */
    data object AddAndStart : NewStopwatchDataIntent()
    data object ChangeState : NewStopwatchDataIntent()
}
