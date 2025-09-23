package io.github.kei_1111.withmo.feature.onboarding.select_display_model

import android.net.Uri
import io.github.kei_1111.withmo.core.featurebase.Action

sealed interface SelectDisplayModelAction : Action {
    data object OnSelectDisplayModelAreaClick : SelectDisplayModelAction
    data object OnBackButtonClick : SelectDisplayModelAction
    data object OnNextButtonClick : SelectDisplayModelAction
    data class OnOpenDocumentLauncherResult(val uri: Uri?) : SelectDisplayModelAction
}
