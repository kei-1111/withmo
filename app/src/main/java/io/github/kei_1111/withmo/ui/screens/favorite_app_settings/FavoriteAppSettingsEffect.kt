package io.github.kei_1111.withmo.ui.screens.favorite_app_settings

import io.github.kei_1111.withmo.ui.base.Effect

sealed interface FavoriteAppSettingsEffect : Effect {
    data object NavigateBack : FavoriteAppSettingsEffect
    data class ShowToast(val message: String) : FavoriteAppSettingsEffect
}
