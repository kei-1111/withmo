package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.user_settings.UserSettings
import com.example.withmo.ui.base.UiState

@RequiresApi(Build.VERSION_CODES.O)
data class HomeUiState(
    val currentTime: DateTimeInfo = DateTimeInfo(),
    val isFinishSplashScreen: Boolean = false,
    val isShowScaleSlider: Boolean = false,
    val isExpandPopup: Boolean = false,
    val isBottomSheetOpened: Boolean = false,
    val appSearchQuery: String = "",
    val currentUserSettings: UserSettings = UserSettings(),
) : UiState
