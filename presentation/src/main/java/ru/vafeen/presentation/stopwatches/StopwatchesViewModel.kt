package ru.vafeen.presentation.stopwatches

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
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.domain.services.StopwatchManager
import ru.vafeen.domain.utils.launchIO
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.common.TimeConstants
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления списком секундомеров и навигацией на соответствующие экраны.
 *
 * @property stopwatchRepository Репозиторий для работы с данными секундомеров.
 * @property stopwatchManager Менеджер для управления логикой секундомера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = StopwatchesViewModel.Factory::class)
internal class StopwatchesViewModel @AssistedInject constructor(
    private val stopwatchRepository: StopwatchRepository,
    private val stopwatchManager: StopwatchManager,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit
) : ViewModel() {
    private val _state = MutableStateFlow(StopwatchesState())
    val state = _state.asStateFlow()

    /**
     * Job для периодического обновления времени в UI.
     */
    private var realtimeUpdating: Job? = null

    init {
        viewModelScope.launchIO {
            stopwatchRepository.getAll().collect { stopwatches ->
                _state.update { it.copy(stopwatches = stopwatches) }
                updateStopwatchState(stopwatches.any { it.stopTime == null })
            }
        }
    }

    /**
     * Обрабатывает интенты от UI и выполняет соответствующие действия.
     *
     * @param intent Интент, описывающий действие пользователя.
     */
    fun handleIntent(intent: StopwatchesIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is StopwatchesIntent.NavigateTo -> navigateTo(id = intent.id)
                StopwatchesIntent.AddNew -> addNew()
                is StopwatchesIntent.Toggle -> makeSthAndUpdate(
                    stopwatch = intent.stopwatch, sth = stopwatchManager::toggle
                )

                is StopwatchesIntent.Reset -> makeSthAndUpdate(
                    stopwatch = intent.stopwatch, sth = stopwatchManager::reset
                )

                is StopwatchesIntent.ToggleDeletingState -> toggleDeletingState(intent.stopwatch)
                is StopwatchesIntent.ChangeStatusForDeleting -> changeStatusForDeleting(intent.stopwatch)
                StopwatchesIntent.DeleteSelected -> deleteSelected()
                StopwatchesIntent.UndoDeleting -> undoDeleting()
            }
        }
    }

    private fun undoDeleting() {
        _state.update {
            it.copy(
                stopwatchesForDeleting = mapOf(),
            )
        }
    }

    /**
     * Изменяет статус выбора секундомера для удаления: добавляет или убирает из списка.
     *
     * @param stopwatch Секундомер, для которого меняется статус выбора.
     */
    private fun changeStatusForDeleting(stopwatch: Stopwatch) {
        val newStopwatchesForDeleting = _state.value.stopwatchesForDeleting.let {
            if (it.contains(stopwatch.id)) {
                it.minus(stopwatch.id)
            } else {
                it.plus(stopwatch.id to stopwatch)
            }
        }
        _state.update {
            it.copy(
                stopwatchesForDeleting = newStopwatchesForDeleting,
            )
        }
    }

    /**
     * Удаляет все выбранные секундомеры из базы данных и очищает состояние удаления.
     */
    private suspend fun deleteSelected() {
        val stopwatchesForDeleting = _state.value.stopwatchesForDeleting.values
        _state.update {
            it.copy(stopwatchesForDeleting = mapOf())
        }
        stopwatchesForDeleting.forEach {
            stopwatchRepository.delete(it)
        }
    }

    /**
     * Переключает состояние режима удаления: включает или выключает,
     * а также инициализирует список выбранных секундомеров.
     *
     * @param stopwatch Секундомер, с которым связана операция переключения.
     */
    private fun toggleDeletingState(stopwatch: Stopwatch) {
        _state.update {
            it.copy(
                stopwatchesForDeleting = if (it.stopwatchesForDeleting.isNotEmpty()) {
                    mapOf()
                } else {
                    mapOf(stopwatch.id to stopwatch)
                },
            )
        }
    }

    /**
     * Выполняет преобразование секундомера с помощью переданной функции и сохраняет результат.
     *
     * @param stopwatch Текущий объект секундомера.
     * @param sth Функция, изменяющая состояние секундомера.
     */
    private suspend fun makeSthAndUpdate(stopwatch: Stopwatch, sth: (Stopwatch) -> Stopwatch) {
        stopwatchRepository.insert(sth(stopwatch))
        _state.update { it.copy(timeNow = System.currentTimeMillis()) }
    }

    /**
     * Обновляет состояние автоматического обновления времени в UI.
     *
     * @param shouldBeRunning Флаг, указывающий, должен ли секундомер обновляться.
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
     * Инициирует навигацию на экран создания нового секундомера.
     */
    private fun addNew() {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.NewStopwatchData))
    }

    /**
     * Инициирует навигацию на экран данных конкретного секундомера.
     *
     * @param id Идентификатор секундомера для навигации.
     */
    private fun navigateTo(id: Long) {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.StopwatchData(id = id)))
    }

    /**
     * Запускает периодическое обновление времени в UI.
     *
     * @param updating Лямбда-функция, вызываемая каждую секунду с текущим временем в миллисекундах.
     */
    fun startUpdating(updating: (Long) -> Unit) {
        if (realtimeUpdating?.isActive == true) return

        realtimeUpdating = viewModelScope.launchIO {
            while (isActive) {
                updating(System.currentTimeMillis())
                delay(TimeConstants.DELAY_BETWEEN_UI_UPDATES)
            }
        }
    }

    /**
     * Останавливает периодическое обновление времени в UI.
     */
    fun stopUpdating() {
        realtimeUpdating?.cancel()
        realtimeUpdating = null
    }

    /**
     * Фабрика для создания экземпляров [StopwatchesViewModel] с параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создаёт экземпляр [StopwatchesViewModel] с функцией навигации.
         *
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Экземпляр [StopwatchesViewModel].
         */
        fun create(sendRootIntent: (NavRootIntent) -> Unit): StopwatchesViewModel
    }
}
