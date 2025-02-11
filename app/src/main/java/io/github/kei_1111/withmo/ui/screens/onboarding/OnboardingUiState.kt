package io.github.kei_1111.withmo.ui.screens.onboarding

import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.ModelFile
import io.github.kei_1111.withmo.ui.base.UiState
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingUiState(
    val currentPage: OnboardingPage = OnboardingPage.Welcome,
    val appSearchQuery: String = "",
    val selectedAppList: ImmutableList<AppInfo> = persistentListOf(),
    val modelFileList: ImmutableList<ModelFile> = persistentListOf(),
    val selectedModelFile: ModelFile? = null,
) : UiState

enum class OnboardingPage {
    Welcome,
    SelectFavoriteApp,
    SelectDisplayModel,
    Finish,
}
