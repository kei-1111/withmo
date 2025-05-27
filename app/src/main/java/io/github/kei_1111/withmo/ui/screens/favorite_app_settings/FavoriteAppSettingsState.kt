package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsState(
    val favoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val initialFavoriteAppList: ImmutableList<AppInfo> = persistentListOf(),
    val appSearchQuery: String = "",
    val isSaveButtonEnabled: Boolean = false,
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : State
