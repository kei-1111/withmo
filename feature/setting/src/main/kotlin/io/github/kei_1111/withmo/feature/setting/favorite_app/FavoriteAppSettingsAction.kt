package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.WithmoAppInfo

sealed interface FavoriteAppSettingsAction : Action {
    data class OnAllAppListAppClick(val withmoAppInfo: WithmoAppInfo) : FavoriteAppSettingsAction
    data class OnFavoriteAppListAppClick(val withmoAppInfo: WithmoAppInfo) : FavoriteAppSettingsAction
    data class OnAppSearchQueryChange(val query: String) : FavoriteAppSettingsAction
    data object OnSaveButtonClick : FavoriteAppSettingsAction
    data object OnBackButtonClick : FavoriteAppSettingsAction
}
