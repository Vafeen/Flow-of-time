package ru.vafeen.domain.services

import ru.vafeen.domain.domain_models.Stopwatch

interface StopwatchManager {

    /**
     * Переключает состояние секундомера: запускает, если он остановлен,
     * или останавливает, если он запущен.
     *
     * @param stopwatch Текущий объект секундомера.
     * @return Обновлённый объект секундомера с изменённым состоянием.
     */
    fun toggle(stopwatch: Stopwatch): Stopwatch

    /**
     * Сбрасывает секундомер в начальное состояние.
     *
     * @param stopwatch Текущий объект секундомера.
     * @return Обновлённый объект секундомера с обнулённым временем.
     */
    fun reset(stopwatch: Stopwatch): Stopwatch
}