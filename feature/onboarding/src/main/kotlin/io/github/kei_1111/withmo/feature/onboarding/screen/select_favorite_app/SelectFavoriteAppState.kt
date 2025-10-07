package io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

internal sealed interface SelectFavoriteAppState : State {
    data object Idle : SelectFavoriteAppState

    data object Loading : SelectFavoriteAppState

    data class Stable(
        val appSearchQuery: String = "",
        val selectedAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    ) : SelectFavoriteAppState

    data class Error(val error: Throwable) : SelectFavoriteAppState
}
