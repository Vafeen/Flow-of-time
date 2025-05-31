package ru.vafeen.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.database.entity.StopwatchEntity

/**
 * DAO-интерфейс для работы с сущностями секундомеров в базе данных Room.
 *
 * Предоставляет методы для получения, вставки, удаления и поиска секундомеров.
 */
@Dao
internal interface StopwatchDao {

    /**
     * Получить поток со списком всех секундомеров.
     *
     * @return [Flow] со списком всех [StopwatchEntity] из таблицы "stopwatches".
     */
    @Query("SELECT * FROM stopwatches")
    fun getAll(): Flow<List<StopwatchEntity>>

    /**
     * Вставить или обновить секундомер в базе данных.
     *
     * При конфликте (например, совпадении id) существующая запись будет заменена.
     *
     * @param stopwatchEntity Сущность секундомера для вставки или обновления.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(stopwatchEntity: StopwatchEntity)

    /**
     * Удалить секундомер из базы данных.
     *
     * @param stopwatchEntity Сущность секундомера для удаления.
     */
    @Delete
    suspend fun delete(stopwatchEntity: StopwatchEntity)

    /**
     * Получить секундомер по его уникальному идентификатору.
     *
     * @param id Идентификатор секундомера.
     * @return Поток [Flow], эмитирующий [StopwatchEntity] или null, если запись не найдена.
     */
    @Query("SELECT * FROM stopwatches WHERE id = :id LIMIT 1")
    fun getById(id: Int): Flow<StopwatchEntity?>
}
