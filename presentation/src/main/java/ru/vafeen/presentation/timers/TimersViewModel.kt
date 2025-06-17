package ru.vafeen.presentation.timers

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
import ru.vafeen.domain.database.TimerRepository
import ru.vafeen.domain.domain_models.Timer
import ru.vafeen.domain.services.TimerManager
import ru.vafeen.domain.utils.launchIO
import ru.vafeen.presentation.common.Screen
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления списком таймеров и навигацией на соответствующие экраны.
 *
 * Управляет процессами запуска, остановки, сброса таймеров и обновления UI с учетом оставшегося времени.
 *
 * @property timerRepository Репозиторий для работы с данными таймеров.
 * @property timerManager Менеджер для управления логикой таймера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = TimersViewModel.Factory::class)
internal class TimersViewModel @AssistedInject constructor(
    private val timerRepository: TimerRepository,
    private val timerManager: TimerManager,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit
) : ViewModel() {
    private val _state = MutableStateFlow(TimersState())
    val state = _state.asStateFlow()

    /**
     * Job для периодического обновления времени в UI.
     */
    private var realtimeUpdating: Job? = null

    init {
        viewModelScope.launchIO {
//            val testTimer = Timer(
//                id = 1L,
//                name = "Тестовый таймер 30 секунд",
//                initialDurationMillis = 30_000L,      // 30 секунд в миллисекундах
//                remainingTimeMillis = 30_000L,        // Изначально осталось 30 секунд
//                isRunning = false,                    // Таймер изначально остановлен
//                startTime = null                      // Время запуска отсутствует, так как таймер не запущен
//            )
//
//            timerRepository.insert(testTimer)
            timerRepository.getAll().collect { timers ->
                _state.update { it.copy(timers = timers) }
                updateTimerState(timers.any { it.isRunning })
            }
        }
    }

    /**
     * Обрабатывает интенты от UI и выполняет соответствующие процессы.
     *
     * @param intent Интент, описывающий действие пользователя.
     */
    fun handleIntent(intent: TimersIntent) {
        viewModelScope.launchIO {
            when (intent) {
                is TimersIntent.NavigateTo -> navigateTo(id = intent.id)
                TimersIntent.AddNew -> addNew()
                is TimersIntent.Toggle -> toggleTimer(intent.timer)
                is TimersIntent.Reset -> resetTimer(intent.timer)
                is TimersIntent.ToggleDeletingState -> toggleDeletingState(intent.timer)
                is TimersIntent.ChangeStatusForDeleting -> changeStatusForDeleting(intent.timer)
                TimersIntent.DeleteSelected -> deleteSelected()
                TimersIntent.UndoDeleting -> undoDeleting()
            }
        }
    }

    private fun undoDeleting() {
        _state.update {
            it.copy(
                timersForDeleting = mapOf(),
            )
        }
    }

    /**
     * Изменяет статус выбора таймера для удаления: добавляет или убирает из списка.
     *
     * @param timer Таймер, для которого меняется статус выбора.
     */
    private fun changeStatusForDeleting(timer: Timer) {
        val newTimersForDeleting = _state.value.timersForDeleting.let {
            if (it.contains(timer.id)) {
                it.minus(timer.id)
            } else {
                it.plus(timer.id to timer)
            }
        }
        _state.update {
            it.copy(
                timersForDeleting = newTimersForDeleting,
            )
        }
    }

    /**
     * Удаляет все выбранные таймеры из базы данных и очищает состояние удаления.
     */
    private suspend fun deleteSelected() {
        val timersForDeleting = _state.value.timersForDeleting.values
        _state.update {
            it.copy(timersForDeleting = mapOf())
        }
        timersForDeleting.forEach {
            timerRepository.delete(it)
        }
    }

    /**
     * Переключает состояние режима удаления: включает или выключает,
     * а также инициализирует список выбранных таймеров.
     *
     * @param timer Таймер, с которым связана операция переключения.
     */
    private fun toggleDeletingState(timer: Timer) {
        _state.update {
            it.copy(
                timersForDeleting = if (it.timersForDeleting.isNotEmpty()) {
                    mapOf()
                } else {
                    mapOf(timer.id to timer)
                },
            )
        }
    }

    /**
     * Переключает состояние таймера: запускает, если он остановлен,
     * или останавливает, если он запущен, с обновлением оставшегося времени.
     *
     * @param timer Текущий объект таймера.
     */
    private suspend fun toggleTimer(timer: Timer) {
        val now = System.currentTimeMillis()
        val updatedTimer = if (timer.isRunning) {
            // Остановка таймера с пересчетом оставшегося времени
            val elapsed = now - (timer.startTime ?: now)
            timer.copy(
                isRunning = false,
                remainingTimeMillis = (timer.remainingTimeMillis - elapsed).coerceAtLeast(0),
                startTime = null
            )
        } else {
            // Запуск таймера с фиксацией времени старта
            timer.copy(
                isRunning = true,
                startTime = now
            )
        }
        timerRepository.insert(updatedTimer)
        _state.update { it.copy(timeNow = now) }
    }

    /**
     * Сбрасывает таймер в изначально установленное состояние.
     *
     * @param timer Текущий объект таймера.
     */
    private suspend fun resetTimer(timer: Timer) {
        val resetTimer = timer.copy(
            isRunning = false,
            remainingTimeMillis = timer.initialDurationMillis,
            startTime = null
        )
        timerRepository.insert(resetTimer)
        _state.update { it.copy(timeNow = System.currentTimeMillis()) }
    }

    /**
     * Обновляет состояние автоматического обновления времени в UI.
     *
     * @param shouldBeRunning Флаг, указывающий, должен ли таймер обновляться.
     */
    private fun updateTimerState(shouldBeRunning: Boolean) {
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
     * Инициирует навигацию на экран создания нового таймера.
     */
    private fun addNew() {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.NewTimerData))
    }

    /**
     * Инициирует навигацию на экран данных конкретного таймера.
     *
     * @param id Идентификатор таймера для навигации.
     */
    private fun navigateTo(id: Long) {
        sendRootIntent(NavRootIntent.NavigateToScreen(Screen.TimerData(id = id)))
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
                delay(1000)
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
     * Фабрика для создания экземпляров [TimersViewModel] с параметрами.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создаёт экземпляр [TimersViewModel] с функцией навигации.
         *
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Экземпляр [TimersViewModel].
         */
        fun create(sendRootIntent: (NavRootIntent) -> Unit): TimersViewModel
    }
}
