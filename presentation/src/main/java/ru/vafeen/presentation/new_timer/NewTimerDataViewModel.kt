package ru.vafeen.presentation.new_timer

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
 * ViewModel для управления состоянием создания нового таймера и взаимодействием с базой данных.
 * Использует TimerManager для управления логикой таймера (переключение, сброс).
 * Обеспечивает добавление, запуск, паузу, сброс и удаление таймера,
 * а также обновление UI в режиме реального времени.
 *
 * @property timerRepository Репозиторий для операций с данными таймера.
 * @property timerManager Менеджер, реализующий логику работы таймера.
 * @property sendRootIntent Функция для отправки навигационных интентов в корневой навигатор.
 */
@HiltViewModel(assistedFactory = NewTimerDataViewModel.Factory::class)
internal class NewTimerDataViewModel @AssistedInject constructor(
    private val timerRepository: TimerRepository,
    private val timerManager: TimerManager,
    @Assisted private val sendRootIntent: (NavRootIntent) -> Unit,
) : ViewModel() {

    private val _state = MutableStateFlow(
        NewTimerDataState(
            timeNow = System.currentTimeMillis(),
            timer = Timer.newInstance()
        )
    )
    val state = _state.asStateFlow()

    private var realtimeUpdating: Job? = null

    /**
     * Обрабатывает пользовательские интенты, связанные с управлением таймером.
     * Выполняет соответствующие действия: добавление и запуск, переключение состояния,
     * сброс, удаление, управление диалогом переименования и сохранение нового имени.
     *
     * @param intent Интент, представляющий действие пользователя.
     */
    fun handleIntent(intent: NewTimerDataIntent) {
        viewModelScope.launchIO {
            when (intent) {
                NewTimerDataIntent.AddAndStart -> addAndStart()
                NewTimerDataIntent.Toggle -> makeSthAndUpdate(timerManager::toggle)
                NewTimerDataIntent.Reset -> makeSthAndUpdate(timerManager::reset)
                NewTimerDataIntent.Delete -> delete()
                is NewTimerDataIntent.ToggleShowingRenamingDialog ->
                    toggleShowingRenamingDialog(intent.isShowed)

                is NewTimerDataIntent.SaveRenaming -> saveRenaming(intent.newName)
            }
        }
    }

    /**
     * Сохраняет новое имя таймера.
     * Если таймер уже добавлен в базу, обновляет запись в репозитории,
     * иначе обновляет локальное состояние.
     *
     * @param newName Новое имя таймера.
     */
    private suspend fun saveRenaming(newName: String) {
        val state = _state.value
        if (state.isAddedToDb) {
            timerRepository.insert(state.timer.copy(name = newName))
        } else {
            _state.update {
                it.copy(timer = it.timer.copy(name = newName))
            }
        }
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
     * Удаляет текущий таймер из базы данных и инициирует возврат к предыдущему экрану.
     */
    private suspend fun delete() {
        val timer = _state.value.timer
        sendRootIntent(NavRootIntent.Back)
        timerRepository.delete(timer)
    }

    /**
     * Применяет функцию преобразования к текущему таймеру через TimerManager,
     * сохраняет обновлённый таймер в репозиторий и обновляет состояние ViewModel.
     *
     * @param sth Функция преобразования таймера (toggle, reset и т.п.).
     */
    private suspend fun makeSthAndUpdate(sth: (Timer) -> Timer) {
        val currentTimer = _state.value.timer
        val updatedTimer = sth(currentTimer)
        timerRepository.insert(updatedTimer)
        _state.update { it.copy(timer = updatedTimer) }
        updateTimerState(updatedTimer.isRunning)
    }

    /**
     * Добавляет текущий таймер в базу данных и запускает его.
     * После добавления подписывается на обновления таймера из репозитория.
     */
    private suspend fun addAndStart() {
        val timer = _state.value.timer
        timerRepository.insert(timer)
        viewModelScope.launchIO {
            timerRepository.getById(timer.id).collect { timerFromDb ->
                if (timerFromDb != null) {
                    _state.update { it.copy(timer = timerFromDb) }
                    updateTimerState(timerFromDb.isRunning)
                }
            }
        }
        _state.update { it.copy(isAddedToDb = true) }
        makeSthAndUpdate(timerManager::toggle)
    }

    /**
     * Управляет запуском или остановкой обновления UI в зависимости от состояния таймера.
     *
     * @param shouldBeRunning Флаг, указывающий, должен ли таймер быть запущен.
     */
    private fun updateTimerState(shouldBeRunning: Boolean) {
        if (shouldBeRunning) {
            startUpdating()
        } else {
            stopUpdating()
        }
    }

    /**
     * Запускает корутину, которая периодически обновляет текущее время в состоянии,
     * обеспечивая обновление UI в реальном времени.
     * Если обновление уже запущено — не запускает новое.
     */
    private fun startUpdating() {
        if (realtimeUpdating?.isActive == true) return

        realtimeUpdating = viewModelScope.launchIO {
            while (isActive) {
                _state.update {
                    it.copy(timeNow = System.currentTimeMillis())
                }
                delay(TimeConstants.DELAY_BETWEEN_UI_UPDATES)
            }
        }
    }

    /**
     * Останавливает обновление времени в UI, отменяя корутину обновления.
     */
    private fun stopUpdating() {
        realtimeUpdating?.cancel()
        realtimeUpdating = null
    }

    /**
     * Фабрика для создания экземпляров NewTimerDataViewModel с передачей параметра sendRootIntent.
     */
    @AssistedFactory
    interface Factory {
        /**
         * Создает экземпляр NewTimerDataViewModel.
         *
         * @param sendRootIntent Функция для отправки навигационных интентов.
         * @return Новый экземпляр NewTimerDataViewModel.
         */
        fun create(@Assisted sendRootIntent: (NavRootIntent) -> Unit): NewTimerDataViewModel
    }
}
