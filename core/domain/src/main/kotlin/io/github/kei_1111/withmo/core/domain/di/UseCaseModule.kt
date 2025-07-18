package io.github.kei_1111.withmo.core.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetClockSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetClockSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetModelChangeWarningStatusUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetModelChangeWarningStatusUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetModelFilePathUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetModelSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetModelSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetOnboardingStatusUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetOnboardingStatusUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetPlacedItemsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetPlacedItemsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.GetUserSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetUserSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.MarkModelChangeWarningShownUseCase
import io.github.kei_1111.withmo.core.domain.usecase.MarkModelChangeWarningShownUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCase
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveAppIconSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveClockSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveClockSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SavePlacedItemsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SavePlacedItemsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCaseImpl
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCaseImpl
import javax.inject.Singleton

@Suppress("TooManyFunctions")
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    // UserSettings
    @Provides
    @Singleton
    fun provideGetUserSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetUserSettingsUseCase = GetUserSettingsUseCaseImpl(userSettingsRepository)

    // Notification
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

    // Clock
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

    // AppIcon
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

    // SideButton
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

    // Sort
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

    // Theme
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

    // ModelFilePath
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

    // ModelSettings
    @Provides
    @Singleton
    fun provideGetModelSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): GetModelSettingsUseCase = GetModelSettingsUseCaseImpl(userSettingsRepository)

    @Provides
    @Singleton
    fun provideSaveModelSettingsUseCase(
        userSettingsRepository: UserSettingsRepository,
    ): SaveModelSettingsUseCase = SaveModelSettingsUseCaseImpl(userSettingsRepository)

    // FavoriteApps
    @Provides
    @Singleton
    fun provideGetFavoriteAppsUseCase(
        favoriteAppRepository: FavoriteAppRepository,
    ): GetFavoriteAppsUseCase = GetFavoriteAppsUseCaseImpl(favoriteAppRepository)

    @Provides
    @Singleton
    fun provideSaveFavoriteAppsUseCase(
        favoriteAppRepository: FavoriteAppRepository,
    ): SaveFavoriteAppsUseCase = SaveFavoriteAppsUseCaseImpl(favoriteAppRepository)

    // OneTimeEvent
    @Provides
    @Singleton
    fun provideGetOnboardingStatusUseCase(
        oneTimeEventRepository: OneTimeEventRepository,
    ): GetOnboardingStatusUseCase = GetOnboardingStatusUseCaseImpl(oneTimeEventRepository)

    @Provides
    @Singleton
    fun provideMarkOnboardingShownUseCase(
        oneTimeEventRepository: OneTimeEventRepository,
    ): MarkOnboardingShownUseCase = MarkOnboardingShownUseCaseImpl(oneTimeEventRepository)

    @Provides
    @Singleton
    fun provideGetModelChangeWarningStatusUseCase(
        oneTimeEventRepository: OneTimeEventRepository,
    ): GetModelChangeWarningStatusUseCase = GetModelChangeWarningStatusUseCaseImpl(oneTimeEventRepository)

    @Provides
    @Singleton
    fun provideMarkModelChangeWarningShownUseCase(
        oneTimeEventRepository: OneTimeEventRepository,
    ): MarkModelChangeWarningShownUseCase = MarkModelChangeWarningShownUseCaseImpl(oneTimeEventRepository)

    // PlacedItems
    @Provides
    @Singleton
    fun provideGetPlacedItemsUseCase(
        placedAppRepository: PlacedAppRepository,
        placedWidgetRepository: PlacedWidgetRepository,
    ): GetPlacedItemsUseCase = GetPlacedItemsUseCaseImpl(placedAppRepository, placedWidgetRepository)

    @Provides
    @Singleton
    fun provideSavePlacedItemsUseCase(
        placedAppRepository: PlacedAppRepository,
        placedWidgetRepository: PlacedWidgetRepository,
    ): SavePlacedItemsUseCase = SavePlacedItemsUseCaseImpl(placedAppRepository, placedWidgetRepository)
}
