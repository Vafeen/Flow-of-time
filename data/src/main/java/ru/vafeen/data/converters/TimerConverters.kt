package ru.vafeen.data.converters

import ru.vafeen.data.database.entity.TimerEntity
import ru.vafeen.domain.domain_models.Timer

/**
 * Преобразование сущности базы данных [TimerEntity] в доменную модель [Timer].
 *
 * @return Новый экземпляр [Timer] с данными из [TimerEntity].
 */
internal fun TimerEntity.toTimer() = Timer(
    id = id,
    name = name,
    initialDurationMillis = initialDurationMillis,
    remainingTimeMillis = remainingTimeMillis,
    isRunning = isRunning,
    startTime = currentStartTime
)

/**
 * Преобразование доменной модели [Timer] в сущность базы данных [TimerEntity].
 *
 * @return Новый экземпляр [TimerEntity] с данными из [Timer].
 */
internal fun Timer.toTimerEntity() = TimerEntity(
    id = id,
    name = name,
    initialDurationMillis = initialDurationMillis,
    remainingTimeMillis = remainingTimeMillis,
    isRunning = isRunning,
    currentStartTime = startTime
)
