package io.github.kei_1111.withmo.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

@RequiresApi(Build.VERSION_CODES.O)
data class HomeState(
    val isChangeModelScaleContentShown: Boolean = false,
    val isModelChangeWarningDialogShown: Boolean = false,
    val isModelLoading: Boolean = true,
    val isAppListSheetOpened: Boolean = false,
    val isWidgetListSheetOpened: Boolean = false,
    val widgetList: ImmutableList<WithmoWidgetInfo> = persistentListOf(),
    val initialWidgetList: ImmutableList<WithmoWidgetInfo> = persistentListOf(),
    val pendingWidgetInfo: WidgetInfo? = null,
    val isEditMode: Boolean = false,
    val resizingWidget: WithmoWidgetInfo? = null,
    val isWidgetResizing: Boolean = false,
    val appSearchQuery: String = "",
    val searchedAppList: ImmutableList<AppInfo> = persistentListOf(),
    val favoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val currentPage: PageContent = PageContent.DisplayModel,
    val currentUserSettings: UserSettings = UserSettings(),
) : State

enum class PageContent {
    DisplayModel,
    Widget,
}
