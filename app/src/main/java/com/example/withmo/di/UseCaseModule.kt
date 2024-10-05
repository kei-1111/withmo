package com.example.withmo.di

import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.domain.usecase.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.GetUserSettingsUseCaseImpl
import com.example.withmo.domain.usecase.SaveSortModeUseCase
import com.example.withmo.domain.usecase.SaveSortModeUseCaseImpl
import com.example.withmo.domain.usecase.notification_usecase.GetNotificationSettingsUseCase
import com.example.withmo.domain.usecase.notification_usecase.GetNotificationSettingsUseCaseImpl
import com.example.withmo.domain.usecase.notification_usecase.SaveNotificationSettingsUseCase
import com.example.withmo.domain.usecase.notification_usecase.SaveNotificationSettingsUseCaseImpl
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

//    Notification
    @Provides
    @Singleton
    fun provideGetNotificationSettingUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetNotificationSettingsUseCase = GetNotificationSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveNotificationSettingUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveNotificationSettingsUseCase = SaveNotificationSettingsUseCaseImpl(userSettingsRepository)
}
