package io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app

import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.model.AppInfo

internal sealed interface SelectFavoriteAppAction : Action {
    data class OnAppSearchQueryChange(val query: String) : SelectFavoriteAppAction
    data class OnAllAppListAppClick(val appInfo: AppInfo) : SelectFavoriteAppAction
    data class OnFavoriteAppListAppClick(val appInfo: AppInfo) : SelectFavoriteAppAction
    data object OnBackButtonClick : SelectFavoriteAppAction
    data object OnNextButtonClick : SelectFavoriteAppAction
}
