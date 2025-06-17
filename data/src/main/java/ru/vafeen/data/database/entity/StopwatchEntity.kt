package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных, представляющая секундомер.
 *
 * Используется для хранения информации о процессах работы секундомера в таблице "stopwatches".
 *
 * @property id Уникальный идентификатор секундомера. Генерируется автоматически при вставке в базу.
 * @property startTime Временная метка (в миллисекундах с эпохи), отражающая момент запуска секундомера.
 * @property name Название или метка секундомера.
 * @property stopTime Временная метка (в миллисекундах с эпохи), отражающая момент остановки секундомера, либо null, если секундомер находится в состоянии работы.
 */
@Entity(tableName = "stopwatches")
internal data class StopwatchEntity(
    @PrimaryKey val id: Long,
    val startTime: Long,
    val name: String,
    val stopTime: Long? = null,
)
