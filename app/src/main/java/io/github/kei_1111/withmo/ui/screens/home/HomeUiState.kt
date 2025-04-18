package io.github.kei_1111.withmo.ui.screens.home

import android.appwidget.AppWidgetProviderInfo
import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.UserSettings
import io.github.kei_1111.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@RequiresApi(Build.VERSION_CODES.O)
data class HomeUiState(
    val isShowScaleSliderButtonShown: Boolean = false,
    val isModelChangeWarningDialogShown: Boolean = false,
    val isModelLoading: Boolean = true,
    val isAppListSheetOpened: Boolean = false,
    val isWidgetListSheetOpened: Boolean = false,
    val widgetList: ImmutableList<WidgetInfo> = persistentListOf(),
    val initialWidgetList: ImmutableList<WidgetInfo> = persistentListOf(),
    val pendingWidgetId: Int = 0,
    val pendingWidgetInfo: AppWidgetProviderInfo? = null,
    val isEditMode: Boolean = false,
    val resizeWidget: WidgetInfo? = null,
    val isWidgetResizing: Boolean = false,
    val appSearchQuery: String = "",
    val favoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val currentPage: PageContent = PageContent.DisplayModel,
    val currentUserSettings: UserSettings = UserSettings(),
) : UiState

enum class PageContent {
    DisplayModel,
    Widget,
}
