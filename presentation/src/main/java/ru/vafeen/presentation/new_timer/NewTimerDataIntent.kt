package ru.vafeen.presentation.new_timer

/**
 * Интенты (намерения) для управления состоянием нового таймера.
 */
internal sealed class NewTimerDataIntent {

    /**
     * Интент для добавления нового таймера и его запуска.
     */
    data object AddAndStart : NewTimerDataIntent()

    /**
     * Интент для переключения состояния таймера (старт/пауза).
     */
    data object Toggle : NewTimerDataIntent()

    /**
     * Интент для сброса таймера.
     */
    data object Reset : NewTimerDataIntent()

    /**
     * Интент для удаления таймера.
     */
    data object Delete : NewTimerDataIntent()

    /**
     * Интент для переключения видимости диалога переименования таймера.
     */
    data class ToggleShowingRenamingDialog(val isShowed: Boolean) : NewTimerDataIntent()

    /**
     * Интент для сохранения измененного названия таймера
     */
    data class SaveRenaming(val newName: String) : NewTimerDataIntent()
}