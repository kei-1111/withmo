package io.github.kei_1111.withmo.domain.repository

import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val userSettings: Flow<UserSettings>

    suspend fun saveSortSettings(sortSettings: SortSettings)

    suspend fun saveNotificationSettings(notificationSettings: NotificationSettings)

    suspend fun saveClockSettings(clockSettings: ClockSettings)

    suspend fun saveAppIconSettings(appIconSettings: AppIconSettings)

    suspend fun saveSideButtonSettings(sideButtonSettings: SideButtonSettings)

    suspend fun saveThemeSettings(themeSettings: ThemeSettings)

    suspend fun saveModelFilePath(modelFilePath: ModelFilePath)
}
