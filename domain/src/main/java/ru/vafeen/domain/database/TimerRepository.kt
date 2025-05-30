package ru.vafeen.domain.database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.domain_models.Timer

interface TimerRepository {
    fun getAll(): Flow<List<Timer>>
    fun insert(timer: Timer)
    fun delete(timer: Timer)
}