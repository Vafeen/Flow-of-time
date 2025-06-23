package ru.vafeen.presentation.timer

/**
 * Интенты (намерения) для управления состоянием таймера.
 */
internal sealed class TimerDataIntent {
    /**
     * Интент для переключения состояния таймера (старт/пауза).
     */
    data object Toggle : TimerDataIntent()

    /**
     * Интент для сброса таймера.
     */
    data object Reset : TimerDataIntent()

    /**
     * Интент для удаления таймера.
     */
    data object Delete : TimerDataIntent()

    /**
     * Интент для переключения видимости диалога переименования таймера.
     */
    data class ToggleShowingRenamingDialog(val isShowed: Boolean) : TimerDataIntent()

    /**
     * Интент для сохранения измененного названия таймера.
     * @property newName Новое имя таймера.
     */
    data class SaveRenaming(val newName: String) : TimerDataIntent()
}