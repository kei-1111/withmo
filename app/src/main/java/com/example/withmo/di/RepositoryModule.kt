package com.example.withmo.di

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.example.withmo.data.local.dao.AppInfoDao
import com.example.withmo.data.local.dao.WidgetInfoDao
import com.example.withmo.data.repositories.AppInfoRepositoryImpl
import com.example.withmo.data.repositories.UserSettingsRepositoryImpl
import com.example.withmo.data.repositories.WidgetInfoRepositoryImpl
import com.example.withmo.domain.repository.AppInfoRepository
import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.domain.repository.WidgetInfoRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
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

    @Provides
    @Singleton
    fun provideAppInfoRepository(
        appInfoDao: AppInfoDao,
        @ApplicationContext context: Context,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): AppInfoRepository = AppInfoRepositoryImpl(appInfoDao, context, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideWidgetInfoRepository(
        widgetInfoDao: WidgetInfoDao,
        appWidgetManager: AppWidgetManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): WidgetInfoRepository = WidgetInfoRepositoryImpl(widgetInfoDao, appWidgetManager, coroutineDispatcher)
}
