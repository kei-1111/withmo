package com.example.withmo.ui.screens.favorite_app_settings

import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.user_settings.AppIconSettings
import com.example.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsUiState(
    val favoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val initialFavoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val appSearchQuery: String = "",
    val isSaveButtonEnabled: Boolean = false,
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : UiState
