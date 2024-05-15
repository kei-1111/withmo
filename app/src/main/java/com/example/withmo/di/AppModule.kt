package com.example.withmo.di

import com.example.withmo.data.repositories.UserSettingRepositoryImpl
import com.example.withmo.domain.model.UserSettingRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun providesUserSettingRepository(): UserSettingRepository = UserSettingRepositoryImpl()
}