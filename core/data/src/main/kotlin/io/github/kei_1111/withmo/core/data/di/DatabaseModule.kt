package io.github.kei_1111.withmo.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.data.local.database.WithmoDatabase
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

//    UserSettingDataStore
    @Provides
    @Singleton
    @UserSetting
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("user_setting_preference")
        },
    )

//    OneTimeEvenDataStore
    @Provides
    @Singleton
    @OneTimeEvent
    fun provideOneTimeEventPreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("one_time_event_preference")
        },
    )

//    Room Database
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): WithmoDatabase = Room.databaseBuilder(context, WithmoDatabase::class.java, "withmo_database")
        .fallbackToDestructiveMigration()
        .build()

//    WidgetInfoDao
    @Provides
    @Singleton
    fun provideWidgetInfoDao(database: WithmoDatabase) = database.withmoWidgetInfoDao()

    @Provides
    @Singleton
    fun provideFavoriteAppDao(database: WithmoDatabase) = database.favoriteAppDao()

    @Provides
    @Singleton
    fun providePlacedAppDao(database: WithmoDatabase) = database.placedAppDao()
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UserSetting

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class OneTimeEvent
