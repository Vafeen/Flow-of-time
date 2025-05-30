package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

/**
 * Сущность базы данных, представляющая таймер.
 *
 * Используется для хранения информации о таймерах в таблице "timers".
 *
 * @property id Уникальный идентификатор таймера. Автоматически генерируется при добавлении в базу данных.
 * @property name Название или описание таймера.
 * @property ldt Временная метка (LocalDateTime), связанная с таймером (например, время создания или последнего обновления).
 */
@Entity(tableName = "timers")
data class TimerEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val name: String,
    val ldt: LocalDateTime,
)
