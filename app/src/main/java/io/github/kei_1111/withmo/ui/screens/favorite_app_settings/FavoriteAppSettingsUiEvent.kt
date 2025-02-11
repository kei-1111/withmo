package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.base.UiEvent

sealed interface FavoriteAppSettingsUiEvent : UiEvent {
    data class AddFavoriteAppList(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class RemoveFavoriteAppList(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : FavoriteAppSettingsUiEvent
    data object Save : FavoriteAppSettingsUiEvent
    data object SaveSuccess : FavoriteAppSettingsUiEvent
    data object SaveFailure : FavoriteAppSettingsUiEvent
    data object NavigateToSettingsScreen : FavoriteAppSettingsUiEvent
}
