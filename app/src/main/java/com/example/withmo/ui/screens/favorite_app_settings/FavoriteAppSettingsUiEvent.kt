package com.example.withmo.ui.screens.favorite_app_settings

import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.base.UiEvent

sealed interface FavoriteAppSettingsUiEvent : UiEvent {
    data class AddFavoriteAppList(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class RemoveFavoriteAppList(val appInfo: AppInfo) : FavoriteAppSettingsUiEvent
    data class OnValueChangeAppSearchQuery(val query: String) : FavoriteAppSettingsUiEvent
    data object Save : FavoriteAppSettingsUiEvent
    data object SaveSuccess : FavoriteAppSettingsUiEvent
    data object SaveFailure : FavoriteAppSettingsUiEvent
    data object NavigateToSettingsScreen : FavoriteAppSettingsUiEvent
}
