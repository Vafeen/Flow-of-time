package ru.vafeen.domain.database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.domain_models.Timer

/**
 * Интерфейс репозитория для работы с таймерами.
 *
 * Определяет операции для получения, добавления и удаления таймеров.
 */
interface TimerRepository {

    /**
     * Получить поток со списком всех таймеров.
     *
     * @return [Flow] со списком моделей таймеров [Timer].
     */
    fun getAll(): Flow<List<Timer>>

    /**
     * Вставить новый таймер.
     *
     * @param timer Модель таймера для вставки.
     */
    fun insert(timer: Timer)

    /**
     * Удалить таймер.
     *
     * @param timer Модель таймера для удаления.
     */
    fun delete(timer: Timer)
}
