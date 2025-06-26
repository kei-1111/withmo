package io.github.kei_1111.withmo.core.data.di

import android.appwidget.AppWidgetManager
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.local.dao.FavoriteAppDao
import io.github.kei_1111.withmo.core.data.local.dao.PlacedAppDao
import io.github.kei_1111.withmo.core.data.local.dao.WithmoWidgetInfoDao
import io.github.kei_1111.withmo.core.data.repository.FavoriteAppRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.OneTimeEventRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.PlacedAppRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.UserSettingsRepositoryImpl
import io.github.kei_1111.withmo.core.data.repository.WidgetInfoRepositoryImpl
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
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
    fun provideWidgetInfoRepository(
        withmoWidgetInfoDao: WithmoWidgetInfoDao,
        appWidgetManager: AppWidgetManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): WidgetInfoRepository = WidgetInfoRepositoryImpl(withmoWidgetInfoDao, appWidgetManager, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideFavoriteAppRepository(
        favoriteAppDao: FavoriteAppDao,
        appManager: AppManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): FavoriteAppRepository = FavoriteAppRepositoryImpl(favoriteAppDao, appManager, coroutineDispatcher)

    @Provides
    @Singleton
    fun providePlacedAppRepository(
        placedAppDao: PlacedAppDao,
        appManager: AppManager,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): PlacedAppRepository = PlacedAppRepositoryImpl(placedAppDao, appManager, coroutineDispatcher)
}
