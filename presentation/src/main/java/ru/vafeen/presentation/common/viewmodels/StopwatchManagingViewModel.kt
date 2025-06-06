package ru.vafeen.presentation.common.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.domain.utils.launchIO

/**
 * Базовый ViewModel для управления состоянием секундомера и обновлением времени в UI.
 */
internal open class StopwatchManagingViewModel : ViewModel() {

    /**
     * Job для обработки обновления времени в реальном времени.
     */
    private var realtimeUpdating: Job? = null

    /**
     * Переключает состояние секундомера: запускает, если он остановлен,
     * или останавливает, если он запущен.
     *
     * @param stopwatch Текущий объект секундомера.
     * @return Обновлённый объект секундомера с изменённым состоянием.
     */
    protected fun toggle(stopwatch: Stopwatch): Stopwatch {
        val now = System.currentTimeMillis()
        val stopTime = stopwatch.stopTime

        return if (stopTime != null) {
            // Запуск секундомера с учетом прошедшего времени
            val elapsed = stopTime - stopwatch.startTime
            stopwatch.copy(
                startTime = now - elapsed,
                stopTime = null
            )
        } else {
            // Остановка секундомера с фиксацией времени остановки
            stopwatch.copy(stopTime = now)
        }
    }

    /**
     * Сбрасывает секундомер в начальное состояние.
     *
     * @param stopwatch Текущий объект секундомера.
     * @return Обновлённый объект секундомера с обнулённым временем.
     */
    protected fun reset(stopwatch: Stopwatch): Stopwatch {
        val now = System.currentTimeMillis()
        return stopwatch.copy(
            startTime = now,
            stopTime = now
        )
    }

    /**
     * Запускает периодическое обновление времени в UI.
     * Обновления происходят каждую секунду до остановки.
     *
     * @param updating Лямбда-функция, вызываемая каждую секунду с текущим временем в миллисекундах.
     */
    protected fun startUpdating(updating: (Long) -> Unit) {
        if (realtimeUpdating?.isActive == true) return

        realtimeUpdating = viewModelScope.launchIO {
            while (isActive) {
                updating(System.currentTimeMillis())
                delay(1000)
            }
        }
    }

    /**
     * Останавливает обновление времени в UI.
     */
    protected fun stopUpdating() {
        realtimeUpdating?.cancel()
        realtimeUpdating = null
    }
}
