package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.ViewModelState
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.util.FileUtils
import java.io.File

data class SelectDisplayModelViewModelState(
    val isModelLoading: Boolean = false,
    val modelFilePath: ModelFilePath = ModelFilePath(null),
    val modelFileThumbnail: Bitmap? = null,
) : ViewModelState<SelectDisplayModelState> {
    override fun toState() = SelectDisplayModelState(
        modelFileName = modelFilePath.path?.let { File(it).name } ?: "デフォルトモデル",
        modelFileThumbnail = modelFileThumbnail,
        isDefaultModel = modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: true,
        isNextButtonEnabled = !isModelLoading,
    )
}
