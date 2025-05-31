package ru.vafeen.presentation.stop_watch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.utils.launchIO

@HiltViewModel(assistedFactory = StopwatchDataViewModel.Factory::class)
internal class StopwatchDataViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    private val stopwatchRepository: StopwatchRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StopWatchDataState(timeNow = System.currentTimeMillis()))
    val state = _state.asStateFlow()
    private var realtimeUpdating: Job? = null

    fun handleIntent(intent: StopwatchDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                StopwatchDataIntent.ChangeState -> changeState()
            }
        }
    }

    init {
        viewModelScope.launchIO {
            initStopwatch()
        }
    }

    private suspend fun changeState() {
        val currentState = _state.value
        val stopwatch = currentState.stopWatch ?: return
        val now = System.currentTimeMillis()
        val stopTime = stopwatch.stopTime
        val updatedStopwatch = if (stopTime != null) {
            // Запуск секундомера
            val elapsed = stopTime - stopwatch.startTime
            stopwatch.copy(
                startTime = now - elapsed,
                stopTime = null
            ).apply {
                _state.update { it.copy(stopWatch = this) }
            }

        } else {
            // Остановка секундомера
            stopwatch.copy(stopTime = now).apply {
                _state.update { it.copy(stopWatch = this) }
            }
        }

        stopwatchRepository.insert(updatedStopwatch)
        updateTimerState(updatedStopwatch.stopTime == null)
    }

    private suspend fun initStopwatch() {
        _state.update { it.copy(isLoading = true) }
        delay(2000)
        stopwatchRepository.getById(id).collect { stopwatch ->
            if (stopwatch != null) {
                _state.update {
                    it.copy(
                        stopWatch = stopwatch,
                        isLoading = false,
                        timeNow = System.currentTimeMillis()
                    )
                }
                updateTimerState(stopwatch.stopTime == null)
            } else {
                _state.update { it.copy(isLoading = false, isStopwatchNotFound = true) }
            }
        }
    }

    private fun updateTimerState(shouldBeRunning: Boolean) {
        if (shouldBeRunning) {
            startUpdating()
        } else {
            stopUpdating()
        }
    }

    private fun startUpdating() {
        if (realtimeUpdating?.isActive == true) return

        realtimeUpdating = viewModelScope.launchIO {
            while (isActive) {
                _state.update {
                    it.copy(timeNow = System.currentTimeMillis())
                }
                delay(1000)
            }
        }
    }

    private fun stopUpdating() {
        realtimeUpdating?.cancel()
        realtimeUpdating = null
    }

    @AssistedFactory
    interface Factory {
        fun create(@Assisted id: Int): StopwatchDataViewModel
    }
}
