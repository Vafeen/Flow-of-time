package ru.vafeen.data.database.room_database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.vafeen.data.converters.DateTimeConverter
import ru.vafeen.data.database.dao.StopwatchDao
import ru.vafeen.data.database.dao.TimerDao
import ru.vafeen.data.database.entity.StopwatchEntity
import ru.vafeen.data.database.entity.TimerEntity

@Database(
    entities = [StopwatchEntity::class, TimerEntity::class],
    version = 1,
    exportSchema = true,
)
@TypeConverters(value = [DateTimeConverter::class])
internal abstract class AppDatabase : RoomDatabase() {
    abstract fun stopwatchDao(): StopwatchDao
    abstract fun timerDao(): TimerDao

    companion object {
        const val NAME = "FlowOfTimeDatabase.db"
    }
}