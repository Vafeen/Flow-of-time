package ru.vafeen.presentation.stopwatches

internal sealed class StopWatchesIntent {
    data class NavigateTo(val id: Int) : StopWatchesIntent()

}