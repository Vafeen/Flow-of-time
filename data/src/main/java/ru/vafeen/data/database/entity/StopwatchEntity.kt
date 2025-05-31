package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных, представляющая секундомер.
 *
 * Используется для хранения информации о секундомерах в таблице "stopwatches".
 *
 * @property id Уникальный идентификатор секундомера. Генерируется автоматически при вставке в базу.
 * @property name Название или метка секундомера.
 * @property ldt Временная метка (LocalDateTime), связанная с секундомером (например, время создания или последнего обновления).
 * @property isWork Флаг, указывающий, находится ли секундомер в рабочем состоянии (true) или нет (false).
 */
@Entity(tableName = "stopwatches")
internal data class StopwatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val startTime: Long,
    val name: String,
    val stopTime: Long? = null,
)
