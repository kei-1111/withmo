package io.github.kei_1111.withmo.feature.setting.screen.favorite_app

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.AppInfo

internal sealed interface FavoriteAppSettingsAction : Action {
    data class OnAllAppListAppClick(val appInfo: AppInfo) : FavoriteAppSettingsAction
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : FavoriteAppSettingsAction
    data class OnAppSearchQueryChange(val query: String) : FavoriteAppSettingsAction
    data object OnSaveButtonClick : FavoriteAppSettingsAction
    data object OnBackButtonClick : FavoriteAppSettingsAction
}
