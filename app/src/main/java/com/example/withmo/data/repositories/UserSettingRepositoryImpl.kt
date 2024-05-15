package com.example.withmo.data.repositories

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.withmo.domain.model.UserSetting
import com.example.withmo.domain.model.UserSettingRepository
import javax.inject.Inject

class UserSettingRepositoryImpl @Inject constructor() : UserSettingRepository {
    override var userSetting: UserSetting by mutableStateOf(UserSetting())

    override fun getUserSettingData(): UserSetting {
        return userSetting
    }

    override fun setUserSettingData(newUserSetting: UserSetting) {
        userSetting = newUserSetting
    }
}