package io.github.kei_1111.withmo.ui.screens.home

import android.content.Context
import android.net.Uri
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.common.unity.UnityToAndroidMessenger
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.domain.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.domain.repository.WidgetInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.model_file_path.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import io.github.kei_1111.withmo.utils.FileUtils
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

@Suppress("TooManyFunctions")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val appInfoRepository: AppInfoRepository,
    private val widgetInfoRepository: WidgetInfoRepository,
    private val oneTimeEventRepository: OneTimeEventRepository,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
) : BaseViewModel<HomeUiState, HomeUiEvent>(), UnityToAndroidMessenger.MessageReceiverFromUnity {

    override fun createInitialState(): HomeUiState = HomeUiState()

    override fun onMessageReceivedFromUnity(message: String) {
        when (message) {
            ModelLoadState.LoadingSuccess.name -> {
                _uiState.update {
                    it.copy(isModelLoading = false)
                }
            }
            ModelLoadState.LoadingFailure.name -> {
                _uiState.update {
                    it.copy(isModelLoading = false)
                }
            }
            else -> {
                Log.d(TAG, "Unknown message from Unity: $message")
            }
        }
    }

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    val isModelChangeWarningFirstShown = oneTimeEventRepository.isModelChangeWarningFirstShown
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = false)

    init {
        UnityToAndroidMessenger.receiver = WeakReference(this)

        observeUserSettings()
        observeFavoriteAppList()
        observeWidgetList()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                _uiState.update {
                    it.copy(currentUserSettings = userSettings)
                }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                _uiState.update {
                    it.copy(
                        favoriteAppList = favoriteAppList.toPersistentList(),
                    )
                }
            }
        }
    }

    private fun observeWidgetList() {
        viewModelScope.launch {
            widgetInfoRepository.getAllWidgetList().collect { widgetList ->
                _uiState.update {
                    it.copy(
                        widgetList = widgetList.toPersistentList(),
                        initialWidgetList = widgetList.toPersistentList(),
                    )
                }
            }
        }
    }

    fun setIsShowScaleSliderButtonShown(show: Boolean) {
        _uiState.update {
            it.copy(isShowScaleSliderButtonShown = show)
        }
    }

    suspend fun getVrmFilePath(context: Context, uri: Uri): String? {
        return FileUtils.copyVrmFileFromUri(context, uri)?.absolutePath
    }

    fun deleteCopiedCacheFiles(context: Context) {
        viewModelScope.launch {
            FileUtils.deleteCopiedCacheFiles(context)
        }
    }

    fun setIsModelChangeWarningDialogShown(isModelChangeWarningDialogShown: Boolean) {
        _uiState.update {
            it.copy(isModelChangeWarningDialogShown = isModelChangeWarningDialogShown)
        }
    }

    fun markModelChangeWarningFirstShown() {
        viewModelScope.launch {
            oneTimeEventRepository.markModelChangeWarningFirstShown()
        }
    }

    fun saveModelFilePath(modelFilePath: ModelFilePath) {
        viewModelScope.launch {
            saveModelFilePathUseCase(modelFilePath)
        }
    }

    fun setIsModelLoading(isModelLoading: Boolean) {
        _uiState.update {
            it.copy(isModelLoading = isModelLoading)
        }
    }

    fun setAppSearchQuery(query: String) {
        _uiState.update {
            it.copy(appSearchQuery = query)
        }
    }

    fun changeIsEditMode(isEditMode: Boolean) {
        _uiState.update {
            it.copy(isEditMode = isEditMode)
        }
    }

    fun changeIsAppListBottomSheetOpened(isAppListBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isAppListSheetOpened = isAppListBottomSheetOpened)
        }
    }

    fun changeIsWidgetListBottomSheetOpened(isWidgetListBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isWidgetListSheetOpened = isWidgetListBottomSheetOpened)
        }
    }

    fun setPendingWidget(widgetInfo: WidgetInfo) {
        _uiState.update {
            it.copy(
                pendingWidgetInfo = widgetInfo,
            )
        }
    }

    fun addDisplayedWidgetList(withmoWidgetInfo: WithmoWidgetInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                widgetList = (currentState.widgetList + withmoWidgetInfo).toPersistentList(),
            )
        }
    }

    fun deleteWidget(withmoWidgetInfo: WithmoWidgetInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                widgetList = currentState.widgetList.filterNot { it.widgetInfo.id == withmoWidgetInfo.widgetInfo.id }
                    .toPersistentList(),
            )
        }
    }

    fun saveWidgetList() {
        val currentWidgetList = _uiState.value.widgetList
        val initialWidgetList = _uiState.value.initialWidgetList

        val addedWidgetList = currentWidgetList.filterNot { currentWidget ->
            initialWidgetList.any { initialWidget -> initialWidget.widgetInfo.id == currentWidget.widgetInfo.id }
        }

        val updatedWidgetList = currentWidgetList.filter { currentWidget ->
            initialWidgetList.any { initialWidget -> initialWidget.widgetInfo.id == currentWidget.widgetInfo.id }
        }

        val deletedWidgetList = initialWidgetList.filterNot { initialWidget ->
            currentWidgetList.any { currentWidget -> initialWidget.widgetInfo.id == currentWidget.widgetInfo.id }
        }

        viewModelScope.launch {
            widgetInfoRepository.insertWidget(addedWidgetList)
            widgetInfoRepository.deleteWidget(deletedWidgetList)
            widgetInfoRepository.updateWidget(updatedWidgetList)
        }
    }

    fun changeResizingWidget(withmoWidgetInfo: WithmoWidgetInfo?) {
        _uiState.update {
            it.copy(resizeWidget = withmoWidgetInfo)
        }
    }

    fun changeIsWidgetResizing(isWidgetResizing: Boolean) {
        _uiState.update {
            it.copy(isWidgetResizing = isWidgetResizing)
        }
    }

    fun setCurrentPage(page: PageContent) {
        _uiState.update {
            it.copy(currentPage = page)
        }
    }

    private companion object {
        const val TimeoutMillis = 5000L

        const val TAG = "HomeViewModel"
    }
}

enum class ModelLoadState {
    LoadingSuccess,
    LoadingFailure,
}
