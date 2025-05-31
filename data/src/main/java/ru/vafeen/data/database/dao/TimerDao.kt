package ru.vafeen.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.database.entity.TimerEntity

/**
 * DAO-интерфейс для работы с сущностями таймеров в базе данных Room.
 *
 * Предоставляет методы для получения, вставки и удаления таймеров.
 */
@Dao
internal interface TimerDao {

    /**
     * Получить поток со списком всех таймеров.
     *
     * @return [Flow] со списком всех [TimerEntity] из таблицы "timers".
     */
    @Query("SELECT * FROM timers")
    fun getAll(): Flow<List<TimerEntity>>

    /**
     * Вставить или обновить таймер в базе данных.
     *
     * При конфликте (например, совпадении id) существующая запись будет заменена.
     *
     * @param timerEntity Сущность таймера для вставки или обновления.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(timerEntity: TimerEntity)

    /**
     * Удалить таймер из базы данных.
     *
     * @param timerEntity Сущность таймера для удаления.
     */
    @Delete
    suspend fun delete(timerEntity: TimerEntity)
}
