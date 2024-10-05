package com.example.withmo.di

import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortModeUseCase
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortModeUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

//    UserSettings
    @Provides
    @Singleton
    fun provideGetUserSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetUserSettingsUseCase = GetUserSettingsUseCaseImpl(userSettingsRepository)

//    SortMode
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
