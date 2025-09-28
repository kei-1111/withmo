package io.github.kei_1111.withmo.core.service.di

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object WidgetModule {

    @Provides
    fun provideAppWidgetManager(
        @ApplicationContext context: Context,
    ): AppWidgetManager = AppWidgetManager.getInstance(context)

    @Provides
    fun provideAppWidgetHost(
        @ApplicationContext context: Context,
    ): AppWidgetHost = AppWidgetHost(context, APP_WIDGET_HOST_ID)

    private const val APP_WIDGET_HOST_ID = 1024
}
