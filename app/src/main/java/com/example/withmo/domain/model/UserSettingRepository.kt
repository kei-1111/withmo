package com.example.withmo.domain.model

import com.example.withmo.domain.model.UserSetting

interface UserSettingRepository {
    val userSetting: UserSetting

    fun getUserSettingData(): UserSetting
    fun setUserSettingData(newUserSetting: UserSetting)
}