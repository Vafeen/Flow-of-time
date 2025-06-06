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

/**
 * ViewModel для управления состоянием секундомера и взаимодействием с базой данных.
 *
 * @property id Идентификатор секундомера
 * @property stopwatchRepository Репозиторий для работы с данными секундомера
 */
@HiltViewModel(assistedFactory = StopwatchDataViewModel.Factory::class)
internal class StopwatchDataViewModel @AssistedInject constructor(
    @Assisted private val id: Int,
    private val stopwatchRepository: StopwatchRepository,
) : ViewModel() {
    private val _state = MutableStateFlow(StopwatchDataState(timeNow = System.currentTimeMillis()))
    val state = _state.asStateFlow()

    /**
     * Job для обработки обновления в реальном времени
     */
    private var realtimeUpdating: Job? = null

    /**
     * Обрабатывает пользовательские интенты для управления секундомером.
     * @param intent Тип действия пользователя
     */
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

    /**
     * Переключает состояние секундомера (старт/пауза) и обновляет данные в БД.
     */
    private suspend fun changeState() {
        val currentState = _state.value
        val stopwatch = currentState.stopWatch ?: return
        val now = System.currentTimeMillis()
        val stopTime = stopwatch.stopTime
        val updatedStopwatch = if (stopTime != null) {
            // Запуск секундомера с учетом прошедшего времени
            val elapsed = stopTime - stopwatch.startTime
            stopwatch.copy(
                startTime = now - elapsed,
                stopTime = null
            ).apply {
                _state.update { it.copy(stopWatch = this) }
            }
        } else {
            // Остановка секундомера с фиксацией времени остановки
            stopwatch.copy(stopTime = now).apply {
                _state.update { it.copy(stopWatch = this) }
            }
        }

        stopwatchRepository.insert(updatedStopwatch)
        updateTimerState(updatedStopwatch.stopTime == null)
    }

    /**
     * Инициализирует состояние секундомера из базы данных.
     * При первом запуске добавляет искусственную задержку для демонстрации загрузки.
     */
    private suspend fun initStopwatch() {
        _state.update { it.copy(isLoading = true) }
//        delay(2000) // Искусственная задержка для демонстрации
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

    /**
     * Управляет состоянием автоматического обновления таймера.
     * @param shouldBeRunning Флаг, указывающий должен ли таймер обновляться
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

    /**
     * Фабрика для создания экземпляра ViewModel с внедрением зависимостей.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр ViewModel с заданным идентификатором.
         * @param id Идентификатор секундомера
         */
        fun create(@Assisted id: Int): StopwatchDataViewModel
    }
}
