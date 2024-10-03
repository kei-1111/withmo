package com.example.withmo.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.withmo.data.repositories.UserSettingsRepositoryImpl
import com.example.withmo.domain.repository.UserSettingsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserSettingsRepository(
        dataStore: DataStore<Preferences>,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UserSettingsRepository = UserSettingsRepositoryImpl(dataStore, coroutineDispatcher)
}
