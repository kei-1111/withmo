package io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.State

sealed interface SelectDisplayModelState : State {
    data object Idle : SelectDisplayModelState

    data object Loading : SelectDisplayModelState

    data class Stable(
        val modelFileName: String = "デフォルトモデル",
        val modelFileThumbnail: Bitmap? = null,
        val isDefaultModel: Boolean = true,
        val isNextButtonEnabled: Boolean = false,
    ) : SelectDisplayModelState

    data class Error(val error: Throwable) : SelectDisplayModelState
}
