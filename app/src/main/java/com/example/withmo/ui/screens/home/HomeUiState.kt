package com.example.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.domain.model.user_settings.UserSettings
import com.example.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@RequiresApi(Build.VERSION_CODES.O)
data class HomeUiState(
    val currentTime: DateTimeInfo = DateTimeInfo(),
    val isFinishSplashScreen: Boolean = false,
    val isShowScaleSlider: Boolean = false,
    val isExpandPopup: Boolean = false,
    val isAppListBottomSheetOpened: Boolean = false,
    val isActionSelectionBottomSheetOpened: Boolean = false,
    val isWidgetListBottomSheetOpened: Boolean = false,
    val displayedWidgetList: ImmutableList<WidgetInfo> = persistentListOf(),
    val pendingWidgetId: Int = 0,
    val pendingWidgetInfo: AppWidgetProviderInfo? = null,
    val isEditMode: Boolean = false,
    val appSearchQuery: String = "",
    val currentUserSettings: UserSettings = UserSettings(),
) : UiState
