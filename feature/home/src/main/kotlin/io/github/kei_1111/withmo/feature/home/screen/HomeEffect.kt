package io.github.kei_1111.withmo.feature.home.screen

import android.content.Intent
import io.github.kei_1111.withmo.core.featurebase.Effect
import io.github.kei_1111.withmo.core.model.AppInfo

internal sealed interface HomeEffect : Effect {
    data object OpenDocument : HomeEffect
    data class ConfigureWidget(val intent: Intent) : HomeEffect
    data class BindWidget(val intent: Intent) : HomeEffect
    data class LaunchApp(val appInfo: AppInfo) : HomeEffect
    data class DeleteApp(val appInfo: AppInfo) : HomeEffect
    data object ShowAppListSheet : HomeEffect
    data object HideAppListSheet : HomeEffect
    data object ShowPlaceableItemListSheet : HomeEffect
    data object HidePlaceableItemListSheet : HomeEffect
    data object NavigateSettings : HomeEffect
    data class ShowToast(val message: String) : HomeEffect
}
