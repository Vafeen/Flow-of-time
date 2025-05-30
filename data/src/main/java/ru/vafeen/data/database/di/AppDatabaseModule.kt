package ru.vafeen.data.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.vafeen.data.database.room_database.AppDatabase

@Module
@InstallIn(SingletonComponent::class)
internal class AppDatabaseModule {
    @Provides
    fun db(@ApplicationContext context: Context): AppDatabase = Room.databaseBuilder(
        context = context, name = AppDatabase.NAME, klass = AppDatabase::class.java
    ).build()

    @Provides
    fun stopWatchDao(db: AppDatabase) = db.stopwatchDao()

    @Provides
    fun timerDao(db: AppDatabase) = db.timerDao()

}

