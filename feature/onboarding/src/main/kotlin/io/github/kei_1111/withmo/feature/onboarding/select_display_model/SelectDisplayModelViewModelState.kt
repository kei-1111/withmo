package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath

data class SelectDisplayModelViewModelState(
    val isModelLoading: Boolean = false,
    val modelFilePath: ModelFilePath = ModelFilePath(null),
    val modelFileThumbnail: Bitmap? = null,
) : ViewModelState<SelectDisplayModelState> {
    override fun toState() = SelectDisplayModelState(
        modelFilePath = modelFilePath,
        modelFileThumbnail = modelFileThumbnail,
        isNextButtonEnabled = !isModelLoading,
    )
}
