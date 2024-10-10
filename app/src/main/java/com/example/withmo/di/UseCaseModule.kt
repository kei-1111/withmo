package com.example.withmo.di

import com.example.withmo.domain.repository.UserSettingsRepository
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortModeUseCase
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortModeUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCaseImpl
import com.example.withmo.domain.usecase.user_settings.theme.SaveThemeSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.theme.SaveThemeSettingsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Suppress("TooManyFunctions")
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

//    Clock
    @Provides
    @Singleton
    fun provideGetClockSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetClockSettingsUseCase = GetClockSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveClockSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveClockSettingsUseCase = SaveClockSettingsUseCaseImpl(userSettingsRepository)

//    AppIcon
    @Provides
    @Singleton
    fun provideGetAppIconSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetAppIconSettingsUseCase = GetAppIconSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveAppIconSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveAppIconSettingsUseCase = SaveAppIconSettingsUseCaseImpl(userSettingsRepository)

//    SideButton
    @Provides
    @Singleton
    fun provideGetSideButtonSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetSideButtonSettingsUseCase = GetSideButtonSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveSideButtonSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveSideButtonSettingsUseCase = SaveSideButtonSettingsUseCaseImpl(userSettingsRepository)

//    Theme
    @Provides
    @Singleton
    fun provideGetThemeSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetThemeSettingsUseCase = GetThemeSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveThemeSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveThemeSettingsUseCase = SaveThemeSettingsUseCaseImpl(userSettingsRepository)
}
