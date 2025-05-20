package io.github.kei_1111.withmo.ui.screens.onboarding

import android.content.Context
import android.net.Uri
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.FavoriteOrder
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import io.github.kei_1111.withmo.utils.FileUtils
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val appInfoRepository: AppInfoRepository,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val oneTimeEventRepository: OneTimeEventRepository,
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
                _state.update {
                    it.copy(
                        selectedAppList = favoriteAppList.toPersistentList(),
                    )
                }
            }
        }
    }

    fun addSelectedAppList(appInfo: AppInfo) {
        _state.update { currentState ->
            if (currentState.selectedAppList.size < AppConstants.FavoriteAppListMaxSize &&
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
        _state.update {
            it.copy(
                selectedAppList = it.selectedAppList.filterNot { it.packageName == appInfo.packageName }
                    .toPersistentList(),
            )
        }
    }

    fun onValueChangeAppSearchQuery(query: String) {
        _state.update {
            it.copy(appSearchQuery = query)
        }
    }

    suspend fun getVrmFilePath(context: Context, uri: Uri): String? {
        return FileUtils.copyVrmFileFromUri(context, uri)?.absolutePath
    }

    fun setIsModelLoading(isLoading: Boolean) {
        _state.update {
            it.copy(isModelLoading = isLoading)
        }
    }

    fun setModelFilePath(modelFilePath: ModelFilePath) {
        _state.update {
            it.copy(modelFilePath = modelFilePath)
        }
    }

    fun setModelFileThumbnail(modelFilePath: ModelFilePath) {
        viewModelScope.launch {
            val thumbnails = modelFilePath.path?.let { File(it) }
                ?.let { FileUtils.getVrmThumbnail(it) }
            _state.update {
                it.copy(modelFileThumbnail = thumbnails)
            }
        }
    }

    fun navigateToNextPage(
        onFinish: () -> Unit,
    ) {
        val currentPage = _state.value.currentPage
        val nextPage = currentPage.ordinal + 1
        if (nextPage < OnboardingPage.entries.size) {
            _state.update {
                it.copy(currentPage = OnboardingPage.entries[nextPage])
            }
        } else {
            onFinish()
        }
    }

    fun navigateToPreviousPage() {
        val currentPage = _state.value.currentPage
        val previousPage = currentPage.ordinal - 1
        if (previousPage >= 0) {
            _state.update {
                it.copy(currentPage = OnboardingPage.entries[previousPage])
            }
        }
    }

    fun saveSetting(context: Context) {
        val favoriteAppList = _state.value.selectedAppList.mapIndexed { index, appInfo ->
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
            val modelFilePath = _state.value.modelFilePath
            if (modelFilePath.path != null) {
                saveModelFilePathUseCase(modelFilePath)
            } else {
                val defaultModelFilePath = FileUtils.copyVrmFileFromAssets(context)?.absolutePath
                saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
            }
        }
    }

    private companion object {
        const val TimeoutMillis = 5000L

        const val TAG = "OnboardingViewModel"
    }
}
