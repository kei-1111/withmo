package io.github.kei_1111.withmo.feature.onboarding.screens.select_favorite_app

import io.github.kei_1111.withmo.core.featurebase.Effect

internal sealed interface SelectFavoriteAppEffect : Effect {
    data object NavigateBack : SelectFavoriteAppEffect
    data object NavigateSelectDisplayModel : SelectFavoriteAppEffect
}
