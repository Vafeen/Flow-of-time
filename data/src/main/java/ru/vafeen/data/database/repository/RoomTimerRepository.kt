package ru.vafeen.data.database.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.vafeen.data.converters.toTimer
import ru.vafeen.data.converters.toTimerEntity
import ru.vafeen.data.database.dao.TimerDao
import ru.vafeen.domain.database.TimerRepository
import ru.vafeen.domain.domain_models.Timer
import javax.inject.Inject

/**
 * Репозиторий для управления таймерами, реализующий интерфейс [TimerRepository].
 *
 * Использует DAO [TimerDao] для взаимодействия с базой данных Room.
 *
 * @property timerDao DAO для доступа к данным таймеров.
 */
internal class RoomTimerRepository @Inject constructor(
    private val timerDao: TimerDao
) : TimerRepository {

    /**
     * Получить поток всех таймеров из базы данных.
     *
     * @return [Flow] со списком моделей таймеров [Timer].
     */
    override fun getAll(): Flow<List<Timer>> = timerDao.getAll().map { entities ->
        entities.map { entity -> entity.toTimer() }
    }

    /**
     * Вставить новый таймер в базу данных.
     *
     * @param timer Модель таймера для вставки.
     */
    override fun insert(timer: Timer) = timerDao.insert(timer.toTimerEntity())

    /**
     * Удалить таймер из базы данных.
     *
     * @param timer Модель таймера для удаления.
     */
    override fun delete(timer: Timer) = timerDao.delete(timer.toTimerEntity())
}
