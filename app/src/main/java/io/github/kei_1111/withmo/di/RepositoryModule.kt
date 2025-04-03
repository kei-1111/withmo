package io.github.kei_1111.withmo.di

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.data.local.dao.AppInfoDao
import io.github.kei_1111.withmo.data.local.dao.WidgetInfoDao
import io.github.kei_1111.withmo.data.repositories.AppInfoRepositoryImpl
import io.github.kei_1111.withmo.data.repositories.OneTimeEventRepositoryImpl
import io.github.kei_1111.withmo.data.repositories.UserSettingsRepositoryImpl
import io.github.kei_1111.withmo.data.repositories.WidgetInfoRepositoryImpl
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.domain.repository.WidgetInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserSettingsRepository(
        @UserSetting dataStore: DataStore<Preferences>,
        @io.github.kei_1111.withmo.di.IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): UserSettingsRepository = UserSettingsRepositoryImpl(dataStore, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideOneTimeEventRepository(
        @OneTimeEvent dataStore: DataStore<Preferences>,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): OneTimeEventRepository = OneTimeEventRepositoryImpl(dataStore, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideAppInfoRepository(
        appInfoDao: AppInfoDao,
        @ApplicationContext context: Context,
        @io.github.kei_1111.withmo.di.IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): AppInfoRepository = AppInfoRepositoryImpl(appInfoDao, context, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideWidgetInfoRepository(
        widgetInfoDao: WidgetInfoDao,
        appWidgetManager: AppWidgetManager,
        @io.github.kei_1111.withmo.di.IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): WidgetInfoRepository = WidgetInfoRepositoryImpl(widgetInfoDao, appWidgetManager, coroutineDispatcher)
}
