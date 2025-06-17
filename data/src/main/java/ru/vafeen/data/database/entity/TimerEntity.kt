package ru.vafeen.data.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Сущность базы данных, представляющая таймер.
 *
 * Используется для хранения информации о состоянии таймеров в таблице "timers".
 *
 * @property id Уникальный идентификатор таймера. Генерируется автоматически при вставке в базу.
 * @property name Название или метка таймера.
 * @property initialDurationMillis Изначально установленная длительность таймера в миллисекундах.
 * @property remainingTimeMillis Текущее оставшееся время таймера в миллисекундах.
 * @property isRunning Флаг, указывающий на процесс работы таймера (true — таймер запущен, false — остановлен или на паузе).
 * @property currentStartTime Временная метка (в миллисекундах с эпохи), отражающая момент последнего запуска таймера, либо null, если таймер не запущен или на паузе.
 */
@Entity(tableName = "timers")
internal data class TimerEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val initialDurationMillis: Long,
    val remainingTimeMillis: Long,
    val isRunning: Boolean,
    val currentStartTime: Long? = null,
)
