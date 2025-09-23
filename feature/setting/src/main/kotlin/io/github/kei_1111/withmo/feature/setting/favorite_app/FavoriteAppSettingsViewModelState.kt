package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class FavoriteAppSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val favoriteAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val initialFavoriteAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val isSaveButtonEnabled: Boolean = false,
    val appSearchQuery: String = "",
    val appIconSettings: AppIconSettings = AppIconSettings(),
) : ViewModelState<FavoriteAppSettingsState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> FavoriteAppSettingsState.Idle

        StatusType.LOADING -> FavoriteAppSettingsState.Loading

        StatusType.STABLE -> FavoriteAppSettingsState.Stable(
            favoriteAppList = favoriteAppList,
            appSearchQuery = appSearchQuery,
            isSaveButtonEnabled = favoriteAppList != initialFavoriteAppList,
            appIconSettings = appIconSettings,
        )

        StatusType.ERROR -> FavoriteAppSettingsState.Error(Throwable("An error occurred in FavoriteAppSettingsViewModelState"))
    }
}
