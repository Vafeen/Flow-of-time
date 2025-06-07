package ru.vafeen.data.services

import ru.vafeen.domain.domain_models.Stopwatch
import ru.vafeen.domain.services.StopwatchManager
import javax.inject.Inject

/**
 * Реализация интерфейса [StopwatchManager], отвечающая за управление состоянием секундомера.
 *
 * Предоставляет методы для переключения состояния секундомера (старт/пауза)
 * и сброса секундомера в начальное состояние.
 */
internal class StopwatchManagerImpl @Inject constructor() : StopwatchManager {

    /**
     * Переключает состояние секундомера: запускает, если он остановлен,
     * или останавливает, если он запущен.
     *
     * @param stopwatch Текущий объект секундомера.
     * @return Обновлённый объект секундомера с изменённым состоянием.
     */
    override fun toggle(stopwatch: Stopwatch): Stopwatch {
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
    override fun reset(stopwatch: Stopwatch): Stopwatch {
        val now = System.currentTimeMillis()
        return stopwatch.copy(
            startTime = now,
            stopTime = now
        )
    }
}
