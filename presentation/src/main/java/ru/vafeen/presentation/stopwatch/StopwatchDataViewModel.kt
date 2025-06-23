package ru.vafeen.presentation.stopwatch

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
import ru.vafeen.presentation.common.TimeConstants
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления состоянием секундомера и взаимодействием с базой данных.
 *
 * @property id Идентификатор секундомера.
 * @property stopwatchRepository Репозиторий для работы с данными секундомера.
 * @property stopwatchManager Менеджер логики секундомера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = StopwatchDataViewModel.Factory::class)
internal class StopwatchDataViewModel @AssistedInject constructor(
    @Assisted private val id: Long,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
    private val stopwatchRepository: StopwatchRepository,
    private val stopwatchManager: StopwatchManager,
) : ViewModel() {

    private val _state = MutableStateFlow(StopwatchDataState(timeNow = System.currentTimeMillis()))
    val state = _state.asStateFlow()
    private var realtimeUpdating: Job? = null

    init {
        viewModelScope.launchIO {
            initStopwatch()
        }
    }

    /**
     * Обрабатывает пользовательские интенты для управления секундомером.
     *
     * @param intent Тип действия пользователя.
     */
    fun handleIntent(intent: StopwatchDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                StopwatchDataIntent.Toggle -> makeSthAndUpdate(sth = stopwatchManager::toggle)
                StopwatchDataIntent.Reset -> makeSthAndUpdate(sth = stopwatchManager::reset)
                StopwatchDataIntent.Delete -> delete()
                is StopwatchDataIntent.SaveRenaming -> saveRenaming(intent.newName)
                is StopwatchDataIntent.ToggleShowingRenamingDialog -> toggleShowingRenamingDialog(
                    intent.isShowed
                )
            }
        }
    }

    /**
     * Сохранение нового имени для секундомера
     *
     * @property newName новое имя секундомера
     */
    private suspend fun saveRenaming(newName: String) {
        val stopwatch: Stopwatch = _state.value.stopwatch ?: return
        stopwatchRepository.insert(stopwatch.copy(name = newName))
    }

    /**
     * Переключает видимость диалога переименования секундомера.
     *
     * @param isShowed Флаг, указывающий, должен ли диалог отображаться.
     */
    private fun toggleShowingRenamingDialog(isShowed: Boolean) {
        _state.update {
            it.copy(isRenameDialogShowed = isShowed)
        }
    }

    /**
     * Выполняет преобразование секундомера и обновляет данные в репозитории.
     *
     * @param sth Функция преобразования секундомера (например, toggle или reset).
     */
    private suspend fun makeSthAndUpdate(sth: (Stopwatch) -> Stopwatch) {
        val currentState = _state.value
        val stopwatch = currentState.stopwatch ?: return
        val newStopwatch = sth(stopwatch)
        stopwatchRepository.insert(newStopwatch)
    }

    /**
     * Инициализирует состояние секундомера из базы данных.
     */
    private suspend fun initStopwatch() {
        stopwatchRepository.getById(id).collect { stopwatch ->
            if (stopwatch != null) {
                _state.update {
                    it.copy(
                        stopwatch = stopwatch,
                        timeNow = System.currentTimeMillis()
                    )
                }
                updateStopwatchState(stopwatch.stopTime == null)
            }
        }
    }

    /**
     * Управляет состоянием автоматического обновления таймера.
     *
     * @param shouldBeRunning Флаг, указывающий, должен ли таймер обновляться.
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
     * Удаляет секундомер из репозитория и возвращается назад по навигации.
     */
    private suspend fun delete() {
        val stopwatch = _state.value.stopwatch ?: return
        sendRootIntent(NavRootIntent.Back)
        stopwatchRepository.delete(stopwatch)
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
         * @param id Идентификатор секундомера.
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Новый экземпляр [StopwatchDataViewModel].
         */
        fun create(
            @Assisted id: Long,
            @Assisted sendRootIntent: (NavRootIntent) -> Unit,
        ): StopwatchDataViewModel
    }
}
