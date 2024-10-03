package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.UserSettings
import java.time.ZonedDateTime

@RequiresApi(Build.VERSION_CODES.O)
data class HomeUiState(
    val currentTime: ZonedDateTime = ZonedDateTime.now(),
    val isFinishSplashScreen: Boolean = false,
    val isShowScaleSlider: Boolean = false,
    val isExpandPopup: Boolean = false,
    val appSearchQuery: String = "",
    val currentUserSettings: UserSettings = UserSettings(),
)
