package ru.vafeen.data.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.vafeen.data.database.repository.RoomStopwatchRepository
import ru.vafeen.data.database.repository.RoomTimerRepository
import ru.vafeen.domain.database.StopwatchRepository
import ru.vafeen.domain.database.TimerRepository

@Module
@InstallIn(ViewModelComponent::class)
internal class RoomRepositories {
    @Provides
    fun stopwatchesRepository(roomStopwatchesRepository: RoomStopwatchRepository): StopwatchRepository =
        roomStopwatchesRepository

    @Provides
    fun timerRepository(roomTimerRepository: RoomTimerRepository): TimerRepository =
        roomTimerRepository
}