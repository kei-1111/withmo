package io.github.kei_1111.withmo.core.data.di

import android.appwidget.AppWidgetManager
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.AppInfoDao
import io.github.kei_1111.withmo.core.data.local.dao.WidgetInfoDao
import io.github.kei_1111.withmo.core.data.repository.AppInfoRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.OneTimeEventRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.UserSettingsRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.WidgetInfoRepositoryImpl
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.domain.repository.WidgetInfoRepository
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserSettingsRepository(
        @UserSetting dataStore: DataStore<Preferences>,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
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
