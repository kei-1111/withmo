package com.example.withmo.di

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
    ): AppWidgetHost = AppWidgetHost(context, AppWidgetHostId)

    private const val AppWidgetHostId = 1024
}
