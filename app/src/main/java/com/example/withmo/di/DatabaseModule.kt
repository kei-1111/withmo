package com.example.withmo.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.room.Room
import com.example.withmo.data.local.database.WithmoDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

//    DataStore
    @Provides
    @Singleton
    fun providePreferencesDataStore(
        @ApplicationContext context: Context,
    ): DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = {
            context.preferencesDataStoreFile("user_setting_preference")
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

//    AppInfoDao
    @Provides
    @Singleton
    fun provideAppInfoDao(database: WithmoDatabase) = database.appInfoDao()
}
