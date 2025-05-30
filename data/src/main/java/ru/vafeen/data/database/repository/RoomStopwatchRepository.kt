package ru.vafeen.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.converters.toStopWatch
import ru.vafeen.data.converters.toStopWatchEntity
import ru.vafeen.data.database.dao.StopwatchDao
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.domain_models.Stopwatch
import javax.inject.Inject

internal class RoomStopwatchRepository @Inject constructor(private val stopwatchDao: StopwatchDao) :
    StopwatchRepository {
    override fun getAll(): Flow<List<Stopwatch>> = stopwatchDao.getAll().map {
        it.map { entity ->
            entity.toStopWatch()
        }
    }

    override fun insert(stopwatch: Stopwatch) =
        stopwatchDao.insert(stopwatchEntity = stopwatch.toStopWatchEntity())

    override fun delete(stopwatch: Stopwatch) =
        stopwatchDao.delete(stopwatchEntity = stopwatch.toStopWatchEntity())

}