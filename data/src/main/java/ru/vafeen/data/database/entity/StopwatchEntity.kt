package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Сущность базы данных, представляющая таймер (секундомер).
 *
 * Используется для хранения информации о секундомерах в таблице "stopwatches".
 *
 * @property id Уникальный идентификатор секундомера. Генерируется автоматически при вставке в базу.
 * @property name Название или метка секундомера.
 * @property ldt Временная метка (LocalDateTime), связанная с секундомером (например, время создания или последнего обновления).
 */
@Entity(tableName = "stopwatches")
internal data class StopwatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val ldt: LocalDateTime,
)
