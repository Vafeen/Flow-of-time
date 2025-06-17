package ru.vafeen.data.services.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.vafeen.data.services.StopwatchManagerImpl
import ru.vafeen.data.services.TimerManagerImpl
import ru.vafeen.domain.services.StopwatchManager
import ru.vafeen.domain.services.TimerManager

/**
 * Модуль Dagger для предоставления зависимостей сервисов управления секундомером и таймером.
 */
@Module
@InstallIn(ViewModelComponent::class)
internal class ServicesModule {

    /**
     * Предоставление зависимости StopwatchManager.
     *
     * @param impl Реализация StopwatchManagerImpl.
     * @return Экземпляр StopwatchManager.
     */
    @Provides
    fun provideStopwatchManager(impl: StopwatchManagerImpl): StopwatchManager = impl

    /**
     * Предоставление зависимости TimerManager.
     *
     * @param impl Реализация TimerManagerImpl.
     * @return Экземпляр TimerManager.
     */
    @Provides
    fun provideTimerManager(impl: TimerManagerImpl): TimerManager = impl
}
