package com.example.withmo.domain.model

import com.example.withmo.domain.model.UserSetting
import kotlinx.coroutines.flow.Flow

interface UserSettingRepository {
    val userSetting: Flow<UserSetting>
    suspend fun saveUserSetting(userSetting: UserSetting)
}