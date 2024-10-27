package com.example.withmo.ui.screens.onboarding

import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.ui.base.UiState
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
