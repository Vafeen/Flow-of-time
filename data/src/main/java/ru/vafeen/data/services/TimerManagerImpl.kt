package ru.vafeen.data.services

import ru.vafeen.domain.domain_models.Timer
import ru.vafeen.domain.services.TimerManager
import javax.inject.Inject

/**
 * Реализация интерфейса [TimerManager], отвечающая за управление состоянием таймера.
 *
 * Предоставляет методы для переключения состояния таймера (старт/пауза)
 * и сброса таймера в начальное состояние с учётом изначальной длительности и оставшегося времени.
 */
internal class TimerManagerImpl @Inject constructor() : TimerManager {

    /**
     * Переключение состояния таймера: запуск при остановленном состоянии
     * или остановка при запущенном состоянии с пересчётом оставшегося времени.
     *
     * @param timer Текущий объект таймера.
     * @return Обновлённый объект таймера с изменённым состоянием и временем.
     */
    override fun toggle(timer: Timer): Timer {
        val now = System.currentTimeMillis()

        return if (timer.isRunning) {
            // Остановка таймера: пересчитываем оставшееся время и сбрасываем время запуска
            val elapsed = now - (timer.startTime ?: now)
            timer.copy(
                isRunning = false,
                remainingTimeMillis = (timer.remainingTimeMillis - elapsed).coerceAtLeast(0),
                startTime = null
            )
        } else {
            // Запуск таймера: фиксируем время запуска
            timer.copy(
                isRunning = true,
                startTime = now
            )
        }
    }

    /**
     * Сброс таймера в изначальное состояние: останавливает таймер и восстанавливает оставшееся время.
     *
     * @param timer Текущий объект таймера.
     * @return Обновлённый объект таймера с изначальной длительностью и остановленным состоянием.
     */
    override fun reset(timer: Timer): Timer {
        return timer.copy(
            isRunning = false,
            remainingTimeMillis = timer.initialDurationMillis,
            startTime = null
        )
    }
}
