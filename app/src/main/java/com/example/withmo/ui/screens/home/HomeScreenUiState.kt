package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.UserSetting
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class HomeScreenUiState(
    val currentTime: ZonedDateTime = ZonedDateTime.now(),
    val finishSplashScreen: Boolean = false,
    val showScaleSlider: Boolean = false,
    val currentUserSetting: UserSetting = UserSetting(),
    val popupExpanded: Boolean = false,
)
