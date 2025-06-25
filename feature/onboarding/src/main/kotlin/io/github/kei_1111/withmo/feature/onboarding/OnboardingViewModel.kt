package io.github.kei_1111.withmo.feature.onboarding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteApp
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val favoriteAppRepository: FavoriteAppRepository,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val oneTimeEventRepository: OneTimeEventRepository,
    private val modelFileManager: ModelFileManager,
) : BaseViewModel<OnboardingState, OnboardingAction, OnboardingEffect>() {

    override fun createInitialState(): OnboardingState = OnboardingState()

    init {
        observeFavoriteAppList()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            favoriteAppRepository.favoriteApps.collect { favoriteAppList ->
                updateState { copy(selectedAppList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    private fun addSelectedAppList(appInfo: AppInfo) {
        val favoriteApp = FavoriteApp(
            info = appInfo,
            favoriteOrder = state.value.selectedAppList.size,
        )
        updateState {
            if (selectedAppList.size < AppConstants.FavoriteAppListMaxSize &&
                selectedAppList.none { it.info.packageName == favoriteApp.info.packageName }
            ) {
                copy(selectedAppList = (selectedAppList + favoriteApp).toPersistentList())
            } else {
                this
            }
        }
    }

    private fun removeSelectedAppList(appInfo: AppInfo) {
        updateState {
            copy(
                selectedAppList = selectedAppList
                    .filterNot { it.info.packageName == appInfo.packageName }
                    .mapIndexed { index, favoriteApp -> favoriteApp.copy(favoriteOrder = index) }
                    .toPersistentList(),
            )
        }
    }

    private fun setModelFileThumbnail(modelFilePath: ModelFilePath) {
        viewModelScope.launch {
            val thumbnails = modelFilePath.path?.let { File(it) }
                ?.let { modelFileManager.getVrmThumbnail(it) }
            updateState { copy(modelFileThumbnail = thumbnails) }
        }
    }

    private fun navigateToNextPage() {
        val currentPage = state.value.currentPage
        val nextPage = currentPage.ordinal + 1
        if (nextPage < OnboardingPage.entries.size) {
            updateState { copy(currentPage = OnboardingPage.entries[nextPage]) }
        } else {
            saveSetting()
            sendEffect(OnboardingEffect.NavigateHome)
        }
    }

    private fun navigateToPreviousPage() {
        val currentPage = state.value.currentPage
        val previousPage = currentPage.ordinal - 1
        if (previousPage >= 0) {
            updateState { copy(currentPage = OnboardingPage.entries[previousPage]) }
        }
    }

    private fun saveSetting() {
        val favoriteAppList = state.value.selectedAppList.mapIndexed { index, favoriteApp ->
            favoriteApp.copy(favoriteOrder = index)
        }

        viewModelScope.launch {
            oneTimeEventRepository.markOnboardingFirstShown()
            favoriteAppRepository.updateFavoriteApps(favoriteAppList.toPersistentList())
            val modelFilePath = state.value.modelFilePath
            if (modelFilePath.path != null) {
                saveModelFilePathUseCase(modelFilePath)
            } else {
                val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
            }
        }
    }

    override fun onAction(action: OnboardingAction) {
        when (action) {
            is OnboardingAction.OnAllAppListAppClick -> addSelectedAppList(action.appInfo)

            is OnboardingAction.OnFavoriteAppListAppClick -> removeSelectedAppList(action.appInfo)

            is OnboardingAction.OnAppSearchQueryChange -> updateState { copy(appSearchQuery = action.query) }

            is OnboardingAction.OnSelectDisplayModelAreaClick -> sendEffect(OnboardingEffect.OpenDocument)

            is OnboardingAction.OnNextButtonClick -> navigateToNextPage()

            is OnboardingAction.OnPreviousButtonClick -> navigateToPreviousPage()

            is OnboardingAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    updateState { copy(isModelLoading = true) }
                    if (action.uri == null) {
                        sendEffect(OnboardingEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(OnboardingEffect.ShowToast("ファイルの読み込みに失敗しました"))
                        } else {
                            updateState { copy(modelFilePath = ModelFilePath(filePath)) }
                            setModelFileThumbnail(ModelFilePath(filePath))
                        }
                    }
                    updateState { copy(isModelLoading = false) }
                }
            }
        }
    }

    private companion object {
        const val TimeoutMillis = 5000L

        const val TAG = "OnboardingViewModel"
    }
}
