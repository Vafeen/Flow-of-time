package ru.vafeen.presentation.new_stopwatch

/**
 * Интенты (намерения) для управления состоянием нового секундомера.
 */
internal sealed class NewStopwatchDataIntent {

    /**
     * Интент для добавления нового секундомера и его запуска.
     */
    data object AddAndStart : NewStopwatchDataIntent()

    /**
     * Интент для переключения состояния секундомера (старт/пауза).
     */
    data object Toggle : NewStopwatchDataIntent()

    /**
     * Интент для сброса секундомера.
     */
    data object Reset : NewStopwatchDataIntent()

    /**
     * Интент для удаления секундомера.
     */
    data object Delete : NewStopwatchDataIntent()

    /**
     * Интент для переключения видимости диалога переименования секундомера.
     */
    data object ToggleShowingRenamingDialog : NewStopwatchDataIntent()

    /**
     * Интент для сохранения измененного нахвания секундомера
     */
    data class SaveRenaming(val newName: String) : NewStopwatchDataIntent()
}
