package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsState(
    val favoriteAppList: ImmutableList<WithmoAppInfo> = persistentListOf(),
    val initialFavoriteAppList: ImmutableList<WithmoAppInfo> = persistentListOf(),
    val appSearchQuery: String = "",
    val searchedAppList: ImmutableList<WithmoAppInfo> = persistentListOf(),
    val isSaveButtonEnabled: Boolean = false,
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : State
