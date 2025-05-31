package io.github.kei_1111.withmo.feature.setting.favorite_app

import io.github.kei_1111.withmo.core.featurebase.Effect

sealed interface FavoriteAppSettingsEffect : Effect {
    data object NavigateBack : FavoriteAppSettingsEffect
    data class ShowToast(val message: String) : FavoriteAppSettingsEffect
}
