package io.github.kei_1111.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.UserSettings
import io.github.kei_1111.withmo.ui.base.State
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@RequiresApi(Build.VERSION_CODES.O)
data class HomeState(
    val isShowScaleSliderButtonShown: Boolean = false,
    val isModelChangeWarningDialogShown: Boolean = false,
    val isModelLoading: Boolean = true,
    val isAppListSheetOpened: Boolean = false,
    val isWidgetListSheetOpened: Boolean = false,
    val widgetList: ImmutableList<WithmoWidgetInfo> = persistentListOf(),
    val initialWidgetList: ImmutableList<WithmoWidgetInfo> = persistentListOf(),
    val pendingWidgetInfo: WidgetInfo? = null,
    val isEditMode: Boolean = false,
    val resizeWidget: WithmoWidgetInfo? = null,
    val isWidgetResizing: Boolean = false,
    val appSearchQuery: String = "",
    val favoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val currentPage: PageContent = PageContent.DisplayModel,
    val currentUserSettings: UserSettings = UserSettings(),
) : State

enum class PageContent {
    DisplayModel,
    Widget,
}
