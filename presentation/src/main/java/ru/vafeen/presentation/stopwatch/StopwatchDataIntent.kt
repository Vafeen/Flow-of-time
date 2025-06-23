package ru.vafeen.presentation.stopwatch

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

    /**
     * Интент для переключения видимости диалога переименования секундомера.
     */
    data class ToggleShowingRenamingDialog(val isShowed: Boolean) : StopwatchDataIntent()

    /**
     * Интент для сохранения измененного нахвания секундомера
     */
    data class SaveRenaming(val newName: String) : StopwatchDataIntent()
}
