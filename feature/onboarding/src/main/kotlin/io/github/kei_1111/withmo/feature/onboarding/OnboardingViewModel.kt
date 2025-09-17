package io.github.kei_1111.withmo.feature.onboarding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    private val saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val markOnboardingShownUseCase: MarkOnboardingShownUseCase,
    private val modelFileManager: ModelFileManager,
) : BaseViewModel<OnboardingViewModelState, OnboardingState, OnboardingAction, OnboardingEffect>() {

    override fun createInitialViewModelState() = OnboardingViewModelState()
    override fun createInitialState(): OnboardingState = OnboardingState.Welcome

    init {
        observeFavoriteAppList()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            getFavoriteAppsUseCase().collect { favoriteAppList ->
                updateViewModelState { copy(selectedAppList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod")
    override fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnAllAppListAppClick -> {
                val favoriteAppInfo = FavoriteAppInfo(
                    info = action.appInfo,
                    favoriteOrder = _viewModelState.value.selectedAppList.size,
                )
                updateViewModelState {
                    if (selectedAppList.size < AppConstants.FavoriteAppListMaxSize &&
                        selectedAppList.none { it.info.packageName == favoriteAppInfo.info.packageName }
                    ) {
                        copy(selectedAppList = (selectedAppList + favoriteAppInfo).toPersistentList())
                    } else {
                        this
                    }
                }
            }

            is OnboardingAction.OnFavoriteAppListAppClick -> {
                updateViewModelState {
                    copy(
                        selectedAppList = selectedAppList
                            .filterNot { it.info.packageName == action.appInfo.packageName }
                            .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
                            .toPersistentList(),
                    )
                }
            }

            is OnboardingAction.OnAppSearchQueryChange -> {
                updateViewModelState { copy(appSearchQuery = action.query) }
            }

            is OnboardingAction.OnSelectDisplayModelAreaClick -> {
                sendEffect(OnboardingEffect.OpenDocument)
            }

            is OnboardingAction.OnNextButtonClick -> {
                val currentPage = _viewModelState.value.currentPage
                val nextPage = currentPage.ordinal + 1
                if (nextPage < OnboardingViewModelState.OnboardingPage.entries.size) {
                    updateViewModelState { copy(currentPage = OnboardingViewModelState.OnboardingPage.entries[nextPage]) }
                } else {
                    val favoriteAppList = _viewModelState.value.selectedAppList.mapIndexed { index, favoriteApp ->
                        favoriteApp.copy(favoriteOrder = index)
                    }
                    viewModelScope.launch {
                        markOnboardingShownUseCase()
                        saveFavoriteAppsUseCase(favoriteAppList.toPersistentList())
                        val modelFilePath = _viewModelState.value.modelFilePath
                        if (modelFilePath.path != null) {
                            saveModelFilePathUseCase(modelFilePath)
                        } else {
                            val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                            saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
                        }
                    }
                    sendEffect(OnboardingEffect.NavigateHome)
                }
            }

            is OnboardingAction.OnPreviousButtonClick -> {
                val currentPage = _viewModelState.value.currentPage
                val previousPage = currentPage.ordinal - 1
                if (previousPage >= 0) {
                    updateViewModelState { copy(currentPage = OnboardingViewModelState.OnboardingPage.entries[previousPage]) }
                }
            }

            is OnboardingAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    updateViewModelState { copy(isModelLoading = true) }
                    if (action.uri == null) {
                        sendEffect(OnboardingEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(OnboardingEffect.ShowToast("ファイルの読み込みに失敗しました"))
                        } else {
                            updateViewModelState { copy(modelFilePath = ModelFilePath(filePath)) }
                            viewModelScope.launch {
                                val thumbnails = ModelFilePath(filePath).path?.let { File(it) }
                                    ?.let { modelFileManager.getVrmThumbnail(it) }
                                updateViewModelState { copy(modelFileThumbnail = thumbnails) }
                            }
                        }
                    }
                    updateViewModelState { copy(isModelLoading = false) }
                }
            }
        }
    }

    private companion object {
        const val TAG = "OnboardingViewModel"
    }
}
