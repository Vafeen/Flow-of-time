package ru.vafeen.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.database.entity.TimerEntity

@Dao
internal interface TimerDao {
    @Query("select * from timers")
    fun getAll(): Flow<List<TimerEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(timerEntity: TimerEntity)

    @Delete
    fun delete(timerEntity: TimerEntity)
}