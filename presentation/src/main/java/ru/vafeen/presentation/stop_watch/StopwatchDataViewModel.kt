package ru.vafeen.presentation.stop_watch

import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.domain.utils.launchIO
import ru.vafeen.presentation.common.viewmodels.StopwatchManagingViewModel

/**
 * ViewModel для управления состоянием секундомера и взаимодействием с базой данных.
 *
 * @property id Идентификатор секундомера
 * @property stopwatchRepository Репозиторий для работы с данными секундомера
 */
@HiltViewModel(assistedFactory = StopwatchDataViewModel.Factory::class)
internal class StopwatchDataViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    private val stopwatchRepository: StopwatchRepository,
) : StopwatchManagingViewModel() {
    private val _state = MutableStateFlow(StopwatchDataState(timeNow = System.currentTimeMillis()))
    val state = _state.asStateFlow()


    /**
     * Обрабатывает пользовательские интенты для управления секундомером.
     * @param intent Тип действия пользователя
     */
    fun handleIntent(intent: StopwatchDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                StopwatchDataIntent.Toggle -> makeSthAndUpdate(sth = ::toggle)
                StopwatchDataIntent.Reset -> makeSthAndUpdate(sth = ::reset)
            }
        }
    }

    init {
        viewModelScope.launchIO {
            initStopwatch()
        }
    }

    private suspend fun makeSthAndUpdate(sth: (Stopwatch) -> Stopwatch) {
        val currentState = _state.value
        val stopwatch = currentState.stopwatch ?: return
        val newStopwatch = sth(stopwatch)
        stopwatchRepository.insert(newStopwatch)
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
                        stopwatch = stopwatch,
                        isLoading = false,
                        timeNow = System.currentTimeMillis()
                    )
                }
                updateStopwatchState(stopwatch.stopTime == null)
            } else {
                _state.update { it.copy(isLoading = false, isStopwatchNotFound = true) }
            }
        }
    }

    /**
     * Управляет состоянием автоматического обновления таймера.
     * @param shouldBeRunning Флаг, указывающий должен ли таймер обновляться
     */
    private fun updateStopwatchState(shouldBeRunning: Boolean) {
        if (shouldBeRunning) {
            startUpdating { currentTimeMillis ->
                _state.update {
                    it.copy(timeNow = currentTimeMillis)
                }
            }
        } else {
            stopUpdating()
        }
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
        fun create(@Assisted id: Long): StopwatchDataViewModel
    }
}
