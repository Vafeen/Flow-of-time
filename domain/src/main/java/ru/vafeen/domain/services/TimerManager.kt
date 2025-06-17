package ru.vafeen.domain.services

import ru.vafeen.domain.domain_models.Timer

interface TimerManager {

    /**
     * Переключение состояния таймера: запуск при остановленном состоянии
     * или остановка при запущенном состоянии.
     *
     * @param timer Текущий объект таймера.
     * @return Обновлённый объект таймера с изменённым состоянием.
     */
    fun toggle(timer: Timer): Timer

    /**
     * Сброс таймера в начальное состояние.
     *
     * @param timer Текущий объект таймера.
     * @return Обновлённый объект таймера с обнулённым временем.
     */
    fun reset(timer: Timer): Timer
}
