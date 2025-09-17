package io.github.kei_1111.withmo.feature.onboarding

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.ViewModelState
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingViewModelState(
    val currentPage: OnboardingPage = OnboardingPage.Welcome,
    val appSearchQuery: String = "",
    val selectedAppList: ImmutableList<FavoriteAppInfo> = persistentListOf(),
    val isModelLoading: Boolean = false,
    val modelFilePath: ModelFilePath = ModelFilePath(null),
    val modelFileThumbnail: Bitmap? = null,
) : ViewModelState<OnboardingState> {

    enum class OnboardingPage {
        Welcome,
        SelectFavoriteApp,
        SelectDisplayModel,
        Finish,
    }

    override fun toState() = when (currentPage) {
        OnboardingPage.Welcome -> OnboardingState.Welcome

        OnboardingPage.SelectFavoriteApp -> OnboardingState.SelectFavoriteApp(
            appSearchQuery = appSearchQuery,
            selectedAppList = selectedAppList,
        )

        OnboardingPage.SelectDisplayModel -> OnboardingState.SelectDisplayModel(
            isModelLoading = isModelLoading,
            modelFilePath = modelFilePath,
            modelFileThumbnail = modelFileThumbnail,
        )

        OnboardingPage.Finish -> OnboardingState.Finish
    }
}
