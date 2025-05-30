package ru.vafeen.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.converters.toTimer
import ru.vafeen.data.converters.toTimerEntity
import ru.vafeen.data.database.dao.TimerDao
import ru.vafeen.domain.database.TimerRepository
import ru.vafeen.domain.domain_models.Timer
import javax.inject.Inject

internal class RoomTimerRepository @Inject constructor(private val timerDao: TimerDao) :
    TimerRepository {
    override fun getAll(): Flow<List<Timer>> = timerDao.getAll().map {
        it.map { entity -> entity.toTimer() }
    }

    override fun insert(timer: Timer) = timerDao.insert(timer.toTimerEntity())

    override fun delete(timer: Timer) = timerDao.delete(timer.toTimerEntity())
}