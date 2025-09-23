package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.stateful.State
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath

data class SelectDisplayModelState(
    val modelFilePath: ModelFilePath = ModelFilePath(null),
    val modelFileThumbnail: Bitmap? = null,
    val isNextButtonEnabled: Boolean = false,
) : State
