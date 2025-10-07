package io.github.kei_1111.withmo.feature.setting.screen.favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal sealed interface FavoriteAppSettingsState : State {
    data object Idle : FavoriteAppSettingsState

    data object Loading : FavoriteAppSettingsState

    data class Stable(
        val favoriteAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
        val appSearchQuery: String = "",
        val isSaveButtonEnabled: Boolean = false,
        val appIconSettings: AppIconSettings = AppIconSettings(),
    ) : FavoriteAppSettingsState

    data class Error(val error: Throwable) : FavoriteAppSettingsState
}
