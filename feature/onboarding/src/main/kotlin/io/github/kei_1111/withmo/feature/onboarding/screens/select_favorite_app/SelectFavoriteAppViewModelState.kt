package io.github.kei_1111.withmo.feature.onboarding.screens.select_favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal data class SelectFavoriteAppViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val appSearchQuery: String = "",
    val selectedAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val error: Throwable? = null,
) : ViewModelState<SelectFavoriteAppState> {

    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> SelectFavoriteAppState.Idle

        StatusType.LOADING -> SelectFavoriteAppState.Loading

        StatusType.STABLE -> SelectFavoriteAppState.Stable(
            appSearchQuery = appSearchQuery,
            selectedAppList = selectedAppList,
        )

        StatusType.ERROR -> SelectFavoriteAppState.Error(error ?: Throwable("Unknown error"))
    }
}
