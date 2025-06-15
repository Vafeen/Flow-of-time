package ru.vafeen.presentation.stop_watch

/**
 * Интенты (намерения) для управления состоянием секундомера.
 */
internal sealed class StopwatchDataIntent {

    /**
     * Интент для переключения состояния секундомера (старт/пауза).
     */
    data object Toggle : StopwatchDataIntent()

    /**
     * Интент для сброса секундомера.
     */
    data object Reset : StopwatchDataIntent()

    /**
     * Интент для удаления секундомера.
     */
    data object Delete : StopwatchDataIntent()
}
