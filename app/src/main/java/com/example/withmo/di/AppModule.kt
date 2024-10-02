package com.example.withmo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.example.withmo.data.repositories.UserSettingRepositoryImpl
import com.example.withmo.domain.repository.UserSettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private const val UserSettingPreferenceName = "user_setting_preference"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = UserSettingPreferenceName,
)

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesUserSettingRepository(
        @ApplicationContext context: Context,
    ): UserSettingRepository = UserSettingRepositoryImpl(dataStore = context.dataStore)
}
