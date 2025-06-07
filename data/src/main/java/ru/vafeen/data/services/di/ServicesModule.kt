package ru.vafeen.data.services.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.vafeen.data.services.StopwatchManagerImpl
import ru.vafeen.domain.services.StopwatchManager

@Module
@InstallIn(ViewModelComponent::class)
internal class ServicesModule {
    @Provides
    fun provideStopwatchManager(impl: StopwatchManagerImpl): StopwatchManager = impl

}