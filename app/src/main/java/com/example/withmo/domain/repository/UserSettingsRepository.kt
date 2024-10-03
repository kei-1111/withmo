package com.example.withmo.domain.repository

import com.example.withmo.domain.model.SortMode
import com.example.withmo.domain.model.UserSettings
import kotlinx.coroutines.flow.Flow

interface UserSettingsRepository {
    val userSettings: Flow<UserSettings>

    suspend fun saveSortMode(sortMode: SortMode)

    suspend fun saveUserSetting(userSettings: UserSettings)
}
