package ru.vafeen.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.vafeen.data.database.entity.StopwatchEntity

@Dao
internal interface StopwatchDao {
    @Query("select * from stopwatches")
    fun getAll(): Flow<List<StopwatchEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(stopwatchEntity: StopwatchEntity)

    @Delete
    fun delete(stopwatchEntity: StopwatchEntity)
}