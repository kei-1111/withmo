package io.github.kei_1111.withmo.feature.onboarding

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

sealed interface OnboardingState : State {
    data object Welcome : OnboardingState

    data class SelectFavoriteApp(
        val appSearchQuery: String = "",
        val selectedAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    ) : OnboardingState

    data class SelectDisplayModel(
        val isModelLoading: Boolean = false,
        val modelFilePath: ModelFilePath = ModelFilePath(null),
        val modelFileThumbnail: Bitmap? = null,
    ) : OnboardingState

    data object Finish : OnboardingState
}
