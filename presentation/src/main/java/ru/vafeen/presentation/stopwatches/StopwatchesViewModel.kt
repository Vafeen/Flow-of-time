package ru.vafeen.presentation.stopwatches

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
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.viewmodels.StopwatchManagingViewModel
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления списком секундомеров и навигацией на соответствующие экраны.
 *
 * @property stopwatchRepository Репозиторий для работы с данными секундомеров.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = StopwatchesViewModel.Factory::class)
internal class StopwatchesViewModel @AssistedInject constructor(
    private val stopwatchRepository: StopwatchRepository,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit
) : StopwatchManagingViewModel() {

    private val _state = MutableStateFlow(
        StopwatchesState(
            stopwatches = listOf(),
            timeNow = System.currentTimeMillis()
        )
    )
    val state = _state.asStateFlow()

    init {
        viewModelScope.launchIO {
            stopwatchRepository.getAll().collect { stopwatches ->
                _state.update { it.copy(stopwatches = stopwatches) }
                updateStopwatchState(stopwatches.any { it.stopTime == null })
            }
        }
    }

    /**
     * Обрабатывает интенты от UI.
     *
     * @param intent Интент, описывающий действие пользователя.
     */
    fun handleIntent(intent: StopwatchesIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is StopwatchesIntent.NavigateTo -> navigateTo(id = intent.id)
                StopwatchesIntent.AddNew -> addNew()
                is StopwatchesIntent.Toggle -> makeSthAndUpdate(
                    stopwatch = intent.stopwatch,
                    sth = ::toggle
                )
                is StopwatchesIntent.Reset -> makeSthAndUpdate(
                    stopwatch = intent.stopwatch,
                    sth = ::reset
                )
            }
        }
    }

    private suspend fun makeSthAndUpdate(stopwatch: Stopwatch, sth: (Stopwatch) -> Stopwatch) {
        stopwatchRepository.insert(sth(stopwatch))
        _state.update { it.copy(timeNow = System.currentTimeMillis()) }
    }

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
     * Обрабатывает добавление нового секундомера — навигация на экран создания.
     */
    private fun addNew() {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.NewStopWatchData))
    }

    /**
     * Обрабатывает навигацию к экрану конкретного секундомера.
     *
     * @param id Идентификатор секундомера для навигации.
     */
    private fun navigateTo(id: Long) {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.StopwatchData(id = id)))
    }

    /**
     * Фабрика для создания экземпляров [StopwatchesViewModel] с параметрами.
     */
    @AssistedFactory
    interface Factory {
        fun create(sendRootIntent: (NavRootIntent) -> Unit): StopwatchesViewModel
    }
}
