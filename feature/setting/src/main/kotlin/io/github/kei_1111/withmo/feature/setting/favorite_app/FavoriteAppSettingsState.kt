package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsState(
    val favoriteAppInfoList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val initialFavoriteAppInfoList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val appSearchQuery: String = "",
    val isSaveButtonEnabled: Boolean = false,
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : State
