package ru.vafeen.presentation.new_stopwatch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.domain.utils.launchIO
import javax.inject.Inject

/**
 * ViewModel для управления состоянием создания нового секундомера и взаимодействием с базой данных.
 *
 * @property stopwatchRepository Репозиторий для работы с данными секундомера.
 */
@HiltViewModel
internal class NewStopwatchDataViewModel @Inject constructor(
    private val stopwatchRepository: StopwatchRepository
) : ViewModel() {

    private val _state = MutableStateFlow(
        NewStopwatchDataState(
            timeNow = System.currentTimeMillis(),
            stopwatch = Stopwatch.newInstance()
        )
    )
    val state = _state.asStateFlow()

    /**
     * Job для обработки обновления времени в реальном времени.
     */
    private var realtimeUpdating: Job? = null

    /**
     * Обрабатывает пользовательские интенты для управления секундомером.
     *
     * @param intent Интент, определяющий действие пользователя.
     */
    fun handleIntent(intent: NewStopwatchDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                NewStopwatchDataIntent.AddAndStart -> addAndStart()
                NewStopwatchDataIntent.ChangeState -> changeState()
            }
        }
    }

    /**
     * Добавляет новый секундомер в базу данных и запускает его.
     */
    private suspend fun addAndStart() {
        val state = _state.value
        stopwatchRepository.insert(state.stopwatch)
        _state.update { it.copy(isAddedToDb = true) }
        changeState()
    }

    /**
     * Переключает состояние секундомера (старт/пауза) и обновляет данные в базе.
     */
    private suspend fun changeState() {
        val currentState = _state.value
        val stopwatch = currentState.stopwatch
        val now = System.currentTimeMillis()
        val stopTime = stopwatch.stopTime
        val updatedStopwatch = if (stopTime != null) {
            // Запуск секундомера с учётом прошедшего времени
            val elapsed = stopTime - stopwatch.startTime
            stopwatch.copy(
                startTime = now - elapsed,
                stopTime = null
            ).apply {
                _state.update { it.copy(stopwatch = this) }
            }
        } else {
            // Остановка секундомера с фиксацией времени остановки
            stopwatch.copy(stopTime = now).apply {
                _state.update { it.copy(stopwatch = this) }
            }
        }

        stopwatchRepository.insert(updatedStopwatch)
        updateTimerState(updatedStopwatch.stopTime == null)
    }

    /**
     * Управляет состоянием автоматического обновления таймера.
     *
     * @param shouldBeRunning Флаг, указывающий, должен ли таймер обновляться.
     */
    private fun updateTimerState(shouldBeRunning: Boolean) {
        if (shouldBeRunning) {
            startUpdating()
        } else {
            stopUpdating()
        }
    }

    /**
     * Запускает периодическое обновление времени в UI.
     * Обновления происходят каждую секунду до остановки.
     */
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

    /**
     * Останавливает обновление времени в UI.
     */
    private fun stopUpdating() {
        realtimeUpdating?.cancel()
        realtimeUpdating = null
    }
}
