package io.github.kei_1111.withmo.feature.onboarding

import android.graphics.Bitmap
import io.github.kei_1111.withmo.core.featurebase.State
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class OnboardingState(
    val currentPage: OnboardingPage = OnboardingPage.Welcome,
    val appSearchQuery: String = "",
    val selectedAppList: ImmutableList<AppInfo> = persistentListOf(),
    val isModelLoading: Boolean = false,
    val modelFilePath: ModelFilePath = ModelFilePath(null),
    val modelFileThumbnail: Bitmap? = null,
) : State

enum class OnboardingPage {
    Welcome,
    SelectFavoriteApp,
    SelectDisplayModel,
    Finish,
}
