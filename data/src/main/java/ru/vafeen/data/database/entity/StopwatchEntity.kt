package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных, представляющая секундомер.
 *
 * Используется для хранения информации о секундомерах в таблице "stopwatches".
 *
 * @property id Уникальный идентификатор секундомера. Генерируется автоматически при вставке в базу.
 * @property startTime Временная метка (в миллисекундах с эпохи), когда секундомер был запущен.
 * @property name Название или метка секундомера.
 * @property stopTime Временная метка (в миллисекундах с эпохи), когда секундомер был остановлен, либо null, если секундомер работает.
 */
@Entity(tableName = "stopwatches")
internal data class StopwatchEntity(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val startTime: Long,
    val name: String,
    val stopTime: Long? = null,
)
