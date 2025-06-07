package ru.vafeen.presentation.stopwatches

import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Интенты (намерения) для управления действиями на экране списка секундомеров.
 */
internal sealed class StopwatchesIntent {

    /**
     * Интент для навигации к конкретному секундомеру по его идентификатору.
     *
     * @property id Уникальный идентификатор секундомера.
     */
    data class NavigateTo(val id: Long) : StopwatchesIntent()

    /**
     * Интент для добавления нового секундомера.
     */
    data object AddNew : StopwatchesIntent()

    /**
     * Интент для переключения состояния секундомера (старт/пауза).
     *
     * @property stopwatch Объект секундомера, состояние которого нужно переключить.
     */
    data class Toggle(val stopwatch: Stopwatch) : StopwatchesIntent()

    /**
     * Интент для сброса секундомера.
     *
     * @property stopwatch Объект секундомера, который нужно сбросить.
     */
    data class Reset(val stopwatch: Stopwatch) : StopwatchesIntent()

    /**
     * Интент для переключения режима удаления секундомеров с выбором конкретного секундомера.
     *
     * @property stopwatch Секундомер, для которого переключается режим удаления.
     */
    data class ToggleDeletingState(val stopwatch: Stopwatch) : StopwatchesIntent()

    /**
     * Интент для отмены режима удаления секундомеров.
     */
    data object UndoDeleting : StopwatchesIntent()

    /**
     * Интент для изменения статуса выбора секундомера для удаления.
     *
     * @property stopwatch Секундомер, для которого меняется статус выбора.
     */
    data class ChangeStatusForDeleting(val stopwatch: Stopwatch) : StopwatchesIntent()

    /**
     * Интент для удаления всех выбранных секундомеров.
     */
    data object DeleteSelected : StopwatchesIntent()
}
