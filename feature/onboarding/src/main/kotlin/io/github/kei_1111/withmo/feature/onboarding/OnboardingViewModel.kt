package io.github.kei_1111.withmo.feature.onboarding

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteOrder
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.sortAppList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val getSortSettingsUseCase: GetSortSettingsUseCase,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val oneTimeEventRepository: OneTimeEventRepository,
    private val modelFileManager: ModelFileManager,
) : BaseViewModel<OnboardingState, OnboardingAction, OnboardingEffect>() {

    private val currentAppList = mutableListOf<WithmoAppInfo>()

    override fun createInitialState(): OnboardingState = OnboardingState()

    init {
        observeAppList()
        observeFavoriteAppList()
    }

    private fun observeAppList() {
        viewModelScope.launch {
            appInfoRepository.getAllList().collect { appList ->
                currentAppList.clear()
                currentAppList.addAll(appList)
                val filteredAppList = filterAppList(state.value.appSearchQuery, appList).toPersistentList()
                updateState { copy(searchedAppList = filteredAppList) }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteList().collect { favoriteAppList ->
                updateState { copy(selectedAppList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    private suspend fun filterAppList(query: String, appList: List<WithmoAppInfo>): List<WithmoAppInfo> =
        withContext(Dispatchers.Default) {
            val sortType = getSortSettingsUseCase().first().sortType
            val filteredAppList = appList.filter { appInfo ->
                appInfo.info.label.contains(query, ignoreCase = true)
            }
            sortAppList(sortType, filteredAppList)
        }

    private fun addSelectedAppList(withmoAppInfo: WithmoAppInfo) {
        updateState {
            if (selectedAppList.size < AppConstants.FavoriteAppListMaxSize &&
                selectedAppList.none { it.info.packageName == withmoAppInfo.info.packageName }
            ) {
                copy(selectedAppList = (selectedAppList + withmoAppInfo).toPersistentList(),)
            } else {
                this
            }
        }
    }

    private fun removeSelectedAppList(withmoAppInfo: WithmoAppInfo) {
        updateState {
            copy(
                selectedAppList = selectedAppList
                    .filterNot { it.info.packageName == withmoAppInfo.info.packageName }
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
        val favoriteAppList = state.value.selectedAppList.mapIndexed { index, withmoAppInfo ->
            withmoAppInfo.copy(
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
            appInfoRepository.updateList(favoriteAppList)
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
            is OnboardingAction.OnAllAppListAppClick -> addSelectedAppList(action.withmoAppInfo)

            is OnboardingAction.OnFavoriteAppListAppClick -> removeSelectedAppList(action.withmoAppInfo)

            is OnboardingAction.OnAppSearchQueryChange -> {
                updateState { copy(appSearchQuery = action.query) }
                viewModelScope.launch {
                    val filteredAppList = filterAppList(action.query, currentAppList).toPersistentList()
                    updateState { copy(searchedAppList = filteredAppList) }
                }
            }

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
