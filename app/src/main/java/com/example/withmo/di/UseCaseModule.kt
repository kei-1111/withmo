package com.example.withmo.di

import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.domain.usecase.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.GetUserSettingsUseCaseImpl
import com.example.withmo.domain.usecase.SaveSortModeUseCase
import com.example.withmo.domain.usecase.SaveSortModeUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    @Singleton
    fun provideGetUserSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetUserSettingsUseCase = GetUserSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveSortModeUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveSortModeUseCase = SaveSortModeUseCaseImpl(userSettingsRepository)
}
