package io.github.kei_1111.withmo.feature.onboarding.select_favorite_app

import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class SelectFavoriteAppState(
    val appSearchQuery: String = "",
    val selectedAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
) : State
