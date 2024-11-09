package com.example.withmo.ui.screens.onboarding

import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.FavoriteOrder
import com.example.withmo.domain.model.ModelFile
import com.example.withmo.domain.repository.AppInfoRepository
import com.example.withmo.ui.base.BaseViewModel
import com.example.withmo.ui.theme.UiConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
) : BaseViewModel<OnboardingUiState, OnboardingUiEvent>() {
    override fun createInitialState(): OnboardingUiState = OnboardingUiState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    init {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                _uiState.update {
                    it.copy(
                        selectedAppList = favoriteAppList.toPersistentList(),
                    )
                }
            }
        }
    }

    fun addSelectedAppList(appInfo: AppInfo) {
        _uiState.update { currentState ->
            if (currentState.selectedAppList.size < UiConfig.FavoriteAppListMaxSize &&
                currentState.selectedAppList.none { it.packageName == appInfo.packageName }
            ) {
                currentState.copy(
                    selectedAppList = (currentState.selectedAppList + appInfo).toPersistentList(),
                )
            } else {
                currentState
            }
        }
    }

    fun removeSelectedAppList(appInfo: AppInfo) {
        _uiState.update {
            it.copy(
                selectedAppList = it.selectedAppList.filterNot { it.packageName == appInfo.packageName }
                    .toPersistentList(),
            )
        }
    }

    fun getModelFileList(modelFileList: ImmutableList<ModelFile>) {
        _uiState.update {
            it.copy(
                modelFileList = modelFileList,
            )
        }
    }

    fun selectModelFile(modelFile: ModelFile) {
        _uiState.update {
            it.copy(
                selectedModelFile = modelFile,
            )
        }
    }

    fun onValueChangeAppSearchQuery(query: String) {
        _uiState.update {
            it.copy(appSearchQuery = query)
        }
    }

    fun navigateToNextPage() {
        val currentPage = _uiState.value.currentPage
        val nextPage = currentPage.ordinal + 1
        if (nextPage < OnboardingPage.entries.size) {
            _uiState.update {
                it.copy(currentPage = OnboardingPage.entries[nextPage])
            }
        } else {
            onEvent(OnboardingUiEvent.OnboardingFinished)
        }
    }

    fun navigateToPreviousPage() {
        val currentPage = _uiState.value.currentPage
        val previousPage = currentPage.ordinal - 1
        if (previousPage >= 0) {
            _uiState.update {
                it.copy(currentPage = OnboardingPage.entries[previousPage])
            }
        }
    }

    fun saveFavoriteAppList() {
        val favoriteAppList = _uiState.value.selectedAppList.mapIndexed { index, appInfo ->
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
            appInfoRepository.updateAppInfoList(favoriteAppList)
        }
    }

    companion object {
        private const val TimeoutMillis = 5000L
    }
}
