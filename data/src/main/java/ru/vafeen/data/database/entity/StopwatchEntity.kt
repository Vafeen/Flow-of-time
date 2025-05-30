package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "stopwatches")
internal data class StopwatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val ldt: LocalDateTime,
)
