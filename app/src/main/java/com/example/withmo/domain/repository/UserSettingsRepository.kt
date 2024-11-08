package com.example.withmo.domain.repository

import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.domain.model.user_settings.ClockSettings
import com.example.withmo.domain.model.user_settings.NotificationSettings
import com.example.withmo.domain.model.user_settings.SideButtonSettings
import com.example.withmo.domain.model.user_settings.SortSettings
import com.example.withmo.domain.model.user_settings.ThemeSettings
import com.example.withmo.domain.model.user_settings.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val userSettings: Flow<UserSettings>

    suspend fun saveSortSettings(sortSettings: SortSettings)

    suspend fun saveNotificationSettings(notificationSettings: NotificationSettings)

    suspend fun saveClockSettings(clockSettings: ClockSettings)

    suspend fun saveAppIconSettings(appIconSettings: AppIconSettings)

    suspend fun saveSideButtonSettings(sideButtonSettings: SideButtonSettings)

    suspend fun saveThemeSettings(themeSettings: ThemeSettings)
}
