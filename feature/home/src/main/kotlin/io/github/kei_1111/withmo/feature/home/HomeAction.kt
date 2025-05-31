package io.github.kei_1111.withmo.feature.home

import android.appwidget.AppWidgetProviderInfo
import android.net.Uri
import androidx.activity.result.ActivityResult
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo

sealed interface HomeAction : Action {
    data class OnAppClick(val appInfo: AppInfo) : HomeAction
    data class OnAppLongClick(val appInfo: AppInfo) : HomeAction
    data object OnShowScaleSliderButtonClick : HomeAction
    data object OnCloseScaleSliderButtonClick : HomeAction
    data object OnSetDefaultModelButtonClick : HomeAction
    data object OnOpenDocumentButtonClick : HomeAction
    data object OnNavigateSettingsButtonClick : HomeAction
    data object OnModelChangeWarningDialogConfirm : HomeAction
    data object OnModelChangeWarningDialogDismiss : HomeAction
    data class OnAppSearchQueryChange(val query: String) : HomeAction
    data object OnAppListSheetSwipeUp : HomeAction
    data object OnAppListSheetSwipeDown : HomeAction
    data object OnAddWidgetButtonClick : HomeAction
    data object OnWidgetListSheetSwipeDown : HomeAction
    data object OnDisplayModelContentSwipeLeft : HomeAction
    data object OnWidgetContentSwipeRight : HomeAction
    data class OnDisplayModelContentClick(val x: Float, val y: Float) : HomeAction
    data object OnDisplayModelContentLongClick : HomeAction
    data class OnWidgetListSheetItemClick(val widgetInfo: AppWidgetProviderInfo) : HomeAction
    data object OnWidgetContentLongClick : HomeAction
    data object OnCompleteEditButtonClick : HomeAction
    data class OnDeleteWidgetBadgeClick(val withmoWidgetInfo: WithmoWidgetInfo) : HomeAction
    data class OnResizeWidgetBadgeClick(val withmoWidgetInfo: WithmoWidgetInfo) : HomeAction
    data class OnWidgetResizeBottomSheetClose(val withmoWidgetInfo: WithmoWidgetInfo) : HomeAction

    data class OnOpenDocumentLauncherResult(val uri: Uri?) : HomeAction
    data class OnConfigureWidgetLauncherResult(val result: ActivityResult) : HomeAction
    data class OnBindWidgetLauncherResult(val result: ActivityResult) : HomeAction
}
