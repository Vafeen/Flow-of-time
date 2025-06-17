package ru.vafeen.presentation.timers

import ru.vafeen.domain.domain_models.Timer

/**
 * Интенты (намерения) для управления действиями на экране списка таймеров.
 */
internal sealed class TimersIntent {

    /**
     * Интент для навигации к конкретному таймеру по его идентификатору.
     *
     * @property id Уникальный идентификатор таймера.
     */
    data class NavigateTo(val id: Long) : TimersIntent()

    /**
     * Интент для добавления нового таймера.
     */
    data object AddNew : TimersIntent()

    /**
     * Интент для переключения состояния таймера (старт/пауза).
     *
     * @property timer Объект таймера, состояние которого нужно переключить.
     */
    data class Toggle(val timer: Timer) : TimersIntent()

    /**
     * Интент для сброса таймера.
     *
     * @property timer Объект таймера, который нужно сбросить.
     */
    data class Reset(val timer: Timer) : TimersIntent()

    /**
     * Интент для переключения режима удаления таймеров с выбором конкретного таймера.
     *
     * @property timer Таймер, для которого переключается режим удаления.
     */
    data class ToggleDeletingState(val timer: Timer) : TimersIntent()

    /**
     * Интент для отмены режима удаления таймеров.
     */
    data object UndoDeleting : TimersIntent()

    /**
     * Интент для изменения статуса выбора таймера для удаления.
     *
     * @property timer Таймер, для которого меняется статус выбора.
     */
    data class ChangeStatusForDeleting(val timer: Timer) : TimersIntent()

    /**
     * Интент для удаления всех выбранных таймеров.
     */
    data object DeleteSelected : TimersIntent()
}
