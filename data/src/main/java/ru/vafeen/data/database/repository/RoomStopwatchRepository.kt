package ru.vafeen.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.converters.toStopWatch
import ru.vafeen.data.converters.toStopWatchEntity
import ru.vafeen.data.database.dao.StopwatchDao
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.domain_models.Stopwatch
import javax.inject.Inject

/**
 * Репозиторий для работы с секундомерами, реализующий интерфейс [StopwatchRepository].
 *
 * Использует DAO [StopwatchDao] для взаимодействия с базой данных Room.
 *
 * @property stopwatchDao DAO для доступа к данным секундомеров.
 */
internal class RoomStopwatchRepository @Inject constructor(
    private val stopwatchDao: StopwatchDao
) : StopwatchRepository {

    /**
     * Получить поток всех секундомеров из базы данных.
     *
     * @return [Flow] со списком моделей секундомеров [Stopwatch].
     */
    override fun getAll(): Flow<List<Stopwatch>> = stopwatchDao.getAll().map { entities ->
        entities.map { entity ->
            entity.toStopWatch()
        }
    }

    /**
     * Вставить новый секундомер в базу данных.
     *
     * @param stopwatch Модель секундомера для вставки.
     */
    override suspend fun insert(stopwatch: Stopwatch) =
        stopwatchDao.insert(stopwatchEntity = stopwatch.toStopWatchEntity())

    /**
     * Удалить секундомер из базы данных.
     *
     * @param stopwatch Модель секундомера для удаления.
     */
    override suspend fun delete(stopwatch: Stopwatch) =
        stopwatchDao.delete(stopwatchEntity = stopwatch.toStopWatchEntity())

    override suspend fun getById(id: Long): Flow<Stopwatch?> = stopwatchDao.getById(id).map {
        it?.toStopWatch()
    }
}
