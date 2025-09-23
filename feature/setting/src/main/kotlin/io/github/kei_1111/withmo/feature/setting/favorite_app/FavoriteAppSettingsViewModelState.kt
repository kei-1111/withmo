package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsViewModelState(
    val favoriteAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val initialFavoriteAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val appSearchQuery: String = "",
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : ViewModelState<FavoriteAppSettingsState> {
    override fun toState() = FavoriteAppSettingsState(
        favoriteAppList = favoriteAppList,
        appSearchQuery = appSearchQuery,
        isSaveButtonEnabled = favoriteAppList != initialFavoriteAppList,
        appIconSettings = appIconSettings,
    )
}
