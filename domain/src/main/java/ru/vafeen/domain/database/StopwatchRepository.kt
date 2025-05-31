package ru.vafeen.domain.database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.domain_models.Stopwatch

/**
 * Интерфейс репозитория для работы с секундомерами.
 *
 * Определяет набор операций для получения, добавления, удаления и поиска секундомеров.
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
    suspend fun insert(stopwatch: Stopwatch)

    /**
     * Удалить секундомер.
     *
     * @param stopwatch Модель секундомера для удаления.
     */
    suspend fun delete(stopwatch: Stopwatch)

    /**
     * Получить секундомер по его уникальному идентификатору.
     *
     * @param id Идентификатор секундомера.
     * @return Модель секундомера [Stopwatch], если найден, или null, если отсутствует.
     */
    suspend fun getById(id: Int): Flow<Stopwatch?>
}
