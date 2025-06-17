package ru.vafeen.presentation.new_stopwatch

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
import ru.vafeen.presentation.navigation.NavRootIntent

/**
 * ViewModel для управления состоянием создания нового секундомера и взаимодействием с базой данных.
 *
 * @property stopwatchRepository Репозиторий для работы с данными секундомера.
 * @property stopwatchManager Менеджер логики секундомера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = NewStopwatchDataViewModel.Factory::class)
internal class NewStopwatchDataViewModel @AssistedInject constructor(
    private val stopwatchRepository: StopwatchRepository,
    private val stopwatchManager: StopwatchManager,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
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
     * Обработка пользовательских интентов для управления секундомером.
     *
     * @param intent Интент, определяющий действие пользователя.
     */
    fun handleIntent(intent: NewStopwatchDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                NewStopwatchDataIntent.AddAndStart -> addAndStart()
                NewStopwatchDataIntent.Toggle -> toggle()
                NewStopwatchDataIntent.Reset -> makeSthAndUpdate(sth = stopwatchManager::reset)
                NewStopwatchDataIntent.Delete -> delete()
                NewStopwatchDataIntent.ToggleShowingRenamingDialog -> toggleShowingRenamingDialog()
                is NewStopwatchDataIntent.SaveRenaming -> saveRenaming(intent.newName)
            }
        }
    }
    /**
     * Сохранение нового имени для секундомера.
     * Если уже сохранен в бд, то обновление там, если нет, только на UI.
     *
     * @property newName новое имя секундомера.
     */
    private suspend fun saveRenaming(newName: String) {
        val state = _state.value
        if (state.isAddedToDb) {
            stopwatchRepository.insert(state.stopwatch.copy(name = newName))
        } else {
            _state.update {
                it.copy(stopwatch = it.stopwatch.copy(name = newName))
            }
        }
    }

    /**
     * Переключение видимости диалога переименования секундомера.
     */
    private fun toggleShowingRenamingDialog() {
        _state.update {
            it.copy(isRenameDialogShowed = !it.isRenameDialogShowed)
        }
    }


    /**
     * Удаление секундомера из репозитория и возврат назад по навигации.
     */
    private suspend fun delete() {
        val stopwatch = _state.value.stopwatch
        sendRootIntent(NavRootIntent.Back)
        stopwatchRepository.delete(stopwatch)
    }

    /**
     * Выполнение преобразования секундомера и обновление данных в состоянии.
     *
     * @param sth Функция преобразования секундомера (например, toggle или reset).
     */
    private fun makeSthAndUpdate(sth: (Stopwatch) -> Stopwatch) {
        _state.update { it.copy(stopwatch = sth(it.stopwatch)) }
    }

    /**
     * Добавление нового секундомера в базу данных и запуск его.
     */
    private suspend fun addAndStart() {
        val state = _state.value
        val stopwatch = state.stopwatch
        stopwatchRepository.insert(stopwatch)
        viewModelScope.launchIO {
            stopwatchRepository.getById(stopwatch.id).collect { stopwatch ->
                _state.update { it.copy(stopwatch = stopwatch ?: return@collect) }
            }
        }
        _state.update { it.copy(isAddedToDb = true) }
        toggle()
    }

    /**
     * Переключение состояния секундомера (старт/пауза) и обновление данных в базе.
     */
    private suspend fun toggle() {
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
            )
        } else {
            // Остановка секундомера с фиксацией времени остановки
            stopwatch.copy(stopTime = now)
        }

        stopwatchRepository.insert(updatedStopwatch)
        updateTimerState(updatedStopwatch.stopTime == null)
    }

    /**
     * Управление состоянием автоматического обновления таймера.
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
     * Запуск периодического обновления времени в UI.
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
     * Остановка обновления времени в UI.
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
         * Создание экземпляра ViewModel с функцией отправки навигационных интентов.
         *
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Новый экземпляр [NewStopwatchDataViewModel].
         */
        fun create(@Assisted sendRootIntent: (NavRootIntent) -> Unit): NewStopwatchDataViewModel
    }
}
