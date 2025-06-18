package ru.vafeen.presentation.timer_data

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
import ru.vafeen.presentation.common.TimeConstants
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления состоянием таймера и взаимодействием с базой данных.
 *
 * @property id Идентификатор таймера.
 * @property timerRepository Репозиторий для работы с данными таймера.
 * @property timerManager Менеджер логики таймера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = TimerDataViewModel.Factory::class)
internal class TimerDataViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
    private val timerRepository: TimerRepository,
    private val timerManager: TimerManager,
) : ViewModel() {

    private val _state = MutableStateFlow(TimerDataState(timeNow = System.currentTimeMillis()))
    val state = _state.asStateFlow()
    private var realtimeUpdating: Job? = null

    init {
        viewModelScope.launchIO {
            initTimer()
        }
    }

    /**
     * Обрабатывает пользовательские интенты для управления таймером.
     *
     * @param intent Тип действия пользователя.
     */
    fun handleIntent(intent: TimerDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                TimerDataIntent.Toggle -> makeSthAndUpdate(sth = timerManager::toggle)
                TimerDataIntent.Reset -> makeSthAndUpdate(sth = timerManager::reset)
                TimerDataIntent.Delete -> delete()
                is TimerDataIntent.SaveRenaming -> saveRenaming(intent.newName)
                TimerDataIntent.ToggleShowingRenamingDialog -> toggleShowingRenamingDialog()
            }
        }
    }

    /**
     * Сохранение нового имени для таймера.
     *
     * @param newName Новое имя таймера.
     */
    private suspend fun saveRenaming(newName: String) {
        val timer: Timer = _state.value.timer ?: return
        timerRepository.insert(timer.copy(name = newName))
    }

    /**
     * Переключение видимости диалога переименования таймера.
     */
    private fun toggleShowingRenamingDialog() {
        _state.update {
            it.copy(isRenameDialogShowed = !it.isRenameDialogShowed)
        }
    }

    /**
     * Выполняет преобразование таймера и обновляет данные в репозитории.
     *
     * @param sth Функция преобразования таймера (например, toggle или reset).
     */
    private suspend fun makeSthAndUpdate(sth: (Timer) -> Timer) {
        val currentState = _state.value
        val timer = currentState.timer ?: return
        val newTimer = sth(timer)
        timerRepository.insert(newTimer)
    }

    /**
     * Инициализирует состояние таймера из базы данных.
     */
    private suspend fun initTimer() {
        timerRepository.getById(id).collect { timer ->
            if (timer != null) {
                _state.update {
                    it.copy(
                        timer = timer,
                        timeNow = System.currentTimeMillis()
                    )
                }
                updateTimerState(timer.isRunning)
            }
        }
    }

    /**
     * Управляет состоянием автоматического обновления таймера.
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
     * Удаляет таймер из репозитория и возвращается назад по навигации.
     */
    private suspend fun delete() {
        val timer = _state.value.timer ?: return
        sendRootIntent(NavRootIntent.Back)
        timerRepository.delete(timer)
    }

    /**
     * Запускает периодическое обновление времени в UI.
     * Обновления происходят каждую секунду до остановки.
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
     * Останавливает обновление времени в UI.
     */
    fun stopUpdating() {
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
         *
         * @param id Идентификатор таймера.
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Новый экземпляр [TimerDataViewModel].
         */
        fun create(
            @Assisted id: Long,
            @Assisted sendRootIntent: (NavRootIntent) -> Unit,
        ): TimerDataViewModel
    }
}