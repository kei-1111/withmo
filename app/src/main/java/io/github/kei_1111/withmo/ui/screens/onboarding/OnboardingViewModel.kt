package io.github.kei_1111.withmo.ui.screens.onboarding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.FavoriteOrder
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.SaveModelFilePathUseCase
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val oneTimeEventRepository: OneTimeEventRepository,
    private val modelFileManager: ModelFileManager,
) : BaseViewModel<OnboardingState, OnboardingAction, OnboardingEffect>() {
    override fun createInitialState(): OnboardingState = OnboardingState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    init {
        observeFavoriteAppList()
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                updateState { copy(selectedAppList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    private fun addSelectedAppList(appInfo: AppInfo) {
        updateState {
            if (selectedAppList.size < AppConstants.FavoriteAppListMaxSize &&
                selectedAppList.none { it.packageName == appInfo.packageName }
            ) {
                copy(
                    selectedAppList = (selectedAppList + appInfo).toPersistentList(),
                )
            } else {
                this
            }
        }
    }

    private fun removeSelectedAppList(appInfo: AppInfo) {
        updateState {
            copy(
                selectedAppList = selectedAppList
                    .filterNot { it.packageName == appInfo.packageName }
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
        val favoriteAppList = state.value.selectedAppList.mapIndexed { index, appInfo ->
            appInfo.copy(
                favoriteOrder = when (index) {
                    0 -> FavoriteOrder.First
                    1 -> FavoriteOrder.Second
                    2 -> FavoriteOrder.Third
                    3 -> FavoriteOrder.Fourth
                    else -> FavoriteOrder.NotFavorite
                },
            )
        }.toPersistentList()

        viewModelScope.launch {
            oneTimeEventRepository.markOnboardingFirstShown()
            appInfoRepository.updateAppInfoList(favoriteAppList)
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
