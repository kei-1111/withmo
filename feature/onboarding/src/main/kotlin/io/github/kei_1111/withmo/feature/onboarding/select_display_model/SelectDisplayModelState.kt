package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.State

data class SelectDisplayModelState(
    val modelFileName: String = "デフォルトモデル",
    val modelFileThumbnail: Bitmap? = null,
    val isDefaultModel: Boolean = true,
    val isNextButtonEnabled: Boolean = false,
) : State
