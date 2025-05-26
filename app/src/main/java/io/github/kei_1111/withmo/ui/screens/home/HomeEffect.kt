package io.github.kei_1111.withmo.ui.screens.home

import android.content.Intent
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.Effect

sealed interface HomeEffect : Effect {
    data object OpenDocument : HomeEffect
    data class ConfigureWidget(val intent: Intent) : HomeEffect
    data class BindWidget(val intent: Intent) : HomeEffect
    data class LaunchApp(val appInfo: AppInfo) : HomeEffect
    data class DeleteApp(val appInfo: AppInfo) : HomeEffect
    data object ShowAppListSheet : HomeEffect
    data object HideAppListSheet : HomeEffect
    data object ShowWidgetListSheet : HomeEffect
    data object HideWidgetListSheet : HomeEffect
    data object NavigateSettings : HomeEffect
    data class ShowToast(val message: String) : HomeEffect
}
