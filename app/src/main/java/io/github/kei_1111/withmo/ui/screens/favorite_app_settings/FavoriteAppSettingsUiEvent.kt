package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface FavoriteAppSettingsUiEvent : UiEvent {
    data class OnAllAppListAppClick(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class OnAppSearchQueryChange(val query: String) : FavoriteAppSettingsUiEvent
    data object OnSaveButtonClick : FavoriteAppSettingsUiEvent
    data object OnBackButtonClick : FavoriteAppSettingsUiEvent
}
