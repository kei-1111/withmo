package io.github.kei_1111.withmo.core.data.di

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.common.dispatcher.IoDispatcher
import io.github.kei_1111.withmo.core.data.manager.AppManagerImpl
import io.github.kei_1111.withmo.core.data.manager.ModelFileManagerImpl
import io.github.kei_1111.withmo.core.data.manager.WidgetManagerImpl
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.manager.WidgetManager
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ManagerModule {
    @Provides
    @Singleton
    fun provideModelFileManager(
        @ApplicationContext context: Context,
        @IoDispatcher coroutineDispatcher: CoroutineDispatcher,
    ): ModelFileManager = ModelFileManagerImpl(context, coroutineDispatcher)

    @Provides
    @Singleton
    fun provideWidgetManager(
        @ApplicationContext context: Context,
        appWidgetHost: AppWidgetHost,
        appWidgetManager: AppWidgetManager,
    ): WidgetManager = WidgetManagerImpl(context, appWidgetHost, appWidgetManager)

    @Provides
    @Singleton
    fun provideAppManager(
        @ApplicationContext context: Context,
    ): AppManager = AppManagerImpl(context)
}
