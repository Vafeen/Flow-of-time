package ru.vafeen.domain.database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Интерфейс репозитория для работы с секундомерами.
 *
 * Определяет набор операций для получения, добавления и удаления секундомеров.
 */
interface StopwatchRepository {

    /**
     * Получить поток со списком всех секундомеров.
     *
     * @return [Flow] со списком моделей секундомеров [Stopwatch].
     */
    fun getAll(): Flow<List<Stopwatch>>

    /**
     * Вставить новый секундомер.
     *
     * @param stopwatch Модель секундомера для вставки.
     */
    fun insert(stopwatch: Stopwatch)

    /**
     * Удалить секундомер.
     *
     * @param stopwatch Модель секундомера для удаления.
     */
    fun delete(stopwatch: Stopwatch)
}
