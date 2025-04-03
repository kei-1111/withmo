package io.github.kei_1111.withmo.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.GetUserSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.GetAppIconSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.app_icon.SaveAppIconSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.GetClockSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.clock.SaveClockSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.GetModelFilePathUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.GetModelFilePathUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.SaveModelFilePathUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.GetNotificationSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.notification.SaveNotificationSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.GetSideButtonSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.side_button.SaveSideButtonSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.GetSortSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.GetSortSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.SaveSortSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCaseImpl
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.SaveThemeSettingsUseCaseImpl
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

//    Sort
    @Provides
    @Singleton
    fun provideGetSortSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetSortSettingsUseCase = GetSortSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveSortSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveSortSettingsUseCase = SaveSortSettingsUseCaseImpl(userSettingsRepository)

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

//    ModelFilePath
    @Provides
    @Singleton
    fun provideGetModelFilePathUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetModelFilePathUseCase = GetModelFilePathUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveModelFilePathUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveModelFilePathUseCase = SaveModelFilePathUseCaseImpl(userSettingsRepository)
}
