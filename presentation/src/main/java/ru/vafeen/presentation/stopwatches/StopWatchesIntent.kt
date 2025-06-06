package ru.vafeen.presentation.stopwatches

/**
 * Интенты (намерения) для управления действиями на экране списка секундомеров.
 */
internal sealed class StopWatchesIntent {

    /**
     * Интент для навигации к конкретному секундомеру по его идентификатору.
     *
     * @property id Уникальный идентификатор секундомера.
     */
    data class NavigateTo(val id: Long) : StopWatchesIntent()

    /**
     * Интент для добавления нового секундомера.
     */
    data object AddNew : StopWatchesIntent()
}
