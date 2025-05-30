package ru.vafeen.domain.database

import kotlinx.coroutines.flow.Flow
import ru.vafeen.domain.domain_models.Stopwatch

interface StopwatchRepository {
    fun getAll(): Flow<List<Stopwatch>>
    fun insert(stopwatch: Stopwatch)
    fun delete(stopwatch: Stopwatch)
}