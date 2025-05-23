package io.github.kei_1111.withmo.ui.screens.home

import android.app.Activity.RESULT_OK
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.common.unity.UnityMethod
import io.github.kei_1111.withmo.common.unity.UnityObject
import io.github.kei_1111.withmo.common.unity.UnityToAndroidMessenger
import io.github.kei_1111.withmo.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.domain.manager.WidgetManager
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
import kotlinx.coroutines.flow.first
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
    private val modelFileManager: ModelFileManager,
    private val widgetManager: WidgetManager,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
) : BaseViewModel<HomeState, HomeAction, HomeEffect>(), UnityToAndroidMessenger.MessageReceiverFromUnity {

    override fun createInitialState(): HomeState = HomeState()

    override fun onMessageReceivedFromUnity(message: String) {
        when (message) {
            ModelLoadState.LoadingSuccess.name -> {
                _state.update {
                    it.copy(isModelLoading = false)
                }
            }
            ModelLoadState.LoadingFailure.name -> {
                _state.update {
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

    init {
        UnityToAndroidMessenger.receiver = WeakReference(this)

        observeUserSettings()
        observeFavoriteAppList()
        observeWidgetList()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                _state.update {
                    it.copy(currentUserSettings = userSettings)
                }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                _state.update {
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
                _state.update {
                    it.copy(
                        widgetList = widgetList.toPersistentList(),
                        initialWidgetList = widgetList.toPersistentList(),
                    )
                }
            }
        }
    }

    private fun setIsShowScaleSliderButtonShown(show: Boolean) {
        _state.update {
            it.copy(isCloseScaleSliderButtonShown = show)
        }
    }

    private fun setIsModelChangeWarningDialogShown(isModelChangeWarningDialogShown: Boolean) {
        _state.update {
            it.copy(isModelChangeWarningDialogShown = isModelChangeWarningDialogShown)
        }
    }

    private fun setIsModelLoading(isModelLoading: Boolean) {
        _state.update {
            it.copy(isModelLoading = isModelLoading)
        }
    }

    private fun setAppSearchQuery(query: String) {
        _state.update {
            it.copy(appSearchQuery = query)
        }
    }

    private fun setIsEditMode(isEditMode: Boolean) {
        _state.update {
            it.copy(isEditMode = isEditMode)
        }
    }

    private fun setIsAppListBottomSheetOpened(isAppListBottomSheetOpened: Boolean) {
        _state.update {
            it.copy(isAppListSheetOpened = isAppListBottomSheetOpened)
        }
    }

    private fun setIsWidgetListBottomSheetOpened(isWidgetListBottomSheetOpened: Boolean) {
        _state.update {
            it.copy(isWidgetListSheetOpened = isWidgetListBottomSheetOpened)
        }
    }

    private fun setPendingWidget(widgetInfo: WidgetInfo) {
        _state.update {
            it.copy(pendingWidgetInfo = widgetInfo)
        }
    }

    private fun addDisplayedWidgetList(withmoWidgetInfo: WithmoWidgetInfo) {
        _state.update { currentState ->
            currentState.copy(
                widgetList = (currentState.widgetList + withmoWidgetInfo).toPersistentList(),
            )
        }
    }

    private fun deleteWidget(withmoWidgetInfo: WithmoWidgetInfo) {
        _state.update { currentState ->
            currentState.copy(
                widgetList = currentState.widgetList.filterNot { it.widgetInfo.id == withmoWidgetInfo.widgetInfo.id }
                    .toPersistentList(),
            )
        }
    }

    private fun saveWidgetList() {
        val currentWidgetList = _state.value.widgetList
        val initialWidgetList = _state.value.initialWidgetList

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

    private fun setResizingWidget(withmoWidgetInfo: WithmoWidgetInfo?) {
        _state.update {
            it.copy(resizingWidget = withmoWidgetInfo)
        }
    }

    private fun setIsWidgetResizing(isWidgetResizing: Boolean) {
        _state.update {
            it.copy(isWidgetResizing = isWidgetResizing)
        }
    }

    private fun setCurrentPage(page: PageContent) {
        _state.update {
            it.copy(currentPage = page)
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod", "NestedBlockDepth")
    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnAppClick -> sendEffect(HomeEffect.LaunchApp(action.appInfo))

            is HomeAction.OnAppLongClick -> sendEffect(HomeEffect.DeleteApp(action.appInfo))

            is HomeAction.OnShowScaleSliderButtonClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.ShowObject, "")
                setIsShowScaleSliderButtonShown(true)
            }

            is HomeAction.OnCloseScaleSliderButtonClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.HideObject, "")
                setIsShowScaleSliderButtonShown(false)
            }

            is HomeAction.OnSetDefaultModelButtonClick -> {
                viewModelScope.launch {
                    val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                    val isDefaultModelFile =
                        state.value.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: false

                    if (!isDefaultModelFile) {
                        setIsModelLoading(true)
                        saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
                    }
                }
            }

            is HomeAction.OnOpenDocumentButtonClick -> {
                viewModelScope.launch {
                    val isModelChangeWarningFirstShown = oneTimeEventRepository.isModelChangeWarningFirstShown.first()
                    if (isModelChangeWarningFirstShown) {
                        sendEffect(HomeEffect.OpenDocument)
                    } else {
                        setIsModelChangeWarningDialogShown(true)
                    }
                }
            }

            is HomeAction.OnNavigateSettingsButtonClick -> sendEffect(HomeEffect.NavigateSettings)

            is HomeAction.OnModelChangeWarningDialogConfirm -> {
                viewModelScope.launch {
                    oneTimeEventRepository.markModelChangeWarningFirstShown()
                    setIsModelChangeWarningDialogShown(false)
                    sendEffect(HomeEffect.OpenDocument)
                }
            }

            is HomeAction.OnModelChangeWarningDialogDismiss -> setIsModelChangeWarningDialogShown(false)

            is HomeAction.OnAppSearchQueryChange -> setAppSearchQuery(action.query)

            is HomeAction.OnAppListSheetSwipeUp -> {
                sendEffect(HomeEffect.ShowAppListSheet)
                setIsAppListBottomSheetOpened(true)
            }

            is HomeAction.OnAppListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideAppListSheet)
                setIsAppListBottomSheetOpened(false)
            }

            is HomeAction.OnAddWidgetButtonClick -> {
                sendEffect(HomeEffect.ShowWidgetListSheet)
                setIsWidgetListBottomSheetOpened(true)
            }

            is HomeAction.OnWidgetListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideWidgetListSheet)
                setIsWidgetListBottomSheetOpened(false)
            }

            is HomeAction.OnDisplayModelContentSwipeLeft -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerExitScreenAnimation, "")
                setCurrentPage(PageContent.Widget)
            }

            is HomeAction.OnWidgetContentSwipeRight -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerEnterScreenAnimation, "")
                setCurrentPage(PageContent.DisplayModel)
            }

            is HomeAction.OnDisplayModelContentClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.MoveLookat, "${action.x},${action.y}")
            }

            is HomeAction.OnDisplayModelContentLongClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.VRMAnimationController, UnityMethod.TriggerTouchAnimation, "")
            }

            is HomeAction.OnWidgetListSheetItemClick -> {
                val widgetId = widgetManager.allocateId()
                val provider = action.widgetInfo.provider
                val result = widgetManager.bindAppWidgetId(widgetId, provider, action.widgetInfo.minWidth, action.widgetInfo.minHeight)

                val widgetInfo = WidgetInfo(widgetId, action.widgetInfo)
                setPendingWidget(widgetInfo)

                if (result) {
                    if (action.widgetInfo.configure != null) {
                        val intent = widgetManager.buildConfigureIntent(widgetId, action.widgetInfo.configure)
                        val activityInfo = widgetManager.getActivityInfo(action.widgetInfo.configure)

                        if (activityInfo != null && activityInfo.exported) {
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                        }
                    } else {
                        addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                    }
                } else {
                    val intent = widgetManager.buildBindIntent(widgetId, provider)
                    sendEffect(HomeEffect.BindWidget(intent))
                }
                sendEffect(HomeEffect.HideWidgetListSheet)
                setIsWidgetListBottomSheetOpened(false)
            }

            is HomeAction.OnWidgetContentLongClick -> setIsEditMode(true)

            is HomeAction.OnCompleteEditButtonClick -> {
                saveWidgetList()
                setIsEditMode(false)
            }

            is HomeAction.OnDeleteWidgetBadgeClick -> deleteWidget(action.withmoWidgetInfo)

            is HomeAction.OnResizeWidgetBadgeClick -> {
                setIsWidgetResizing(true)
                setResizingWidget(action.withmoWidgetInfo)
                deleteWidget(action.withmoWidgetInfo)
            }

            is HomeAction.OnWidgetResizeBottomSheetClose -> {
                setIsWidgetResizing(false)
                addDisplayedWidgetList(action.withmoWidgetInfo)
                setResizingWidget(null)
            }

            is HomeAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    if (action.uri == null) {
                        sendEffect(HomeEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        setIsModelLoading(true)
                        modelFileManager.deleteCopiedCacheFiles()
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(HomeEffect.ShowToast("ファイルの読み込みに失敗しました"))
                            setIsModelLoading(false)
                        } else {
                            if (filePath == state.value.currentUserSettings.modelFilePath.path) {
                                sendEffect(HomeEffect.ShowToast("同じファイルが選択されています"))
                                setIsModelLoading(false)
                            } else {
                                saveModelFilePathUseCase(ModelFilePath(filePath))
                            }
                        }
                    }
                }
            }

            is HomeAction.OnConfigureWidgetLauncherResult -> {
                state.value.pendingWidgetInfo?.let { widgetInfo ->
                    if (action.result.resultCode == RESULT_OK) {
                        addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                    } else {
                        widgetManager.deleteWidgetId(widgetInfo.id)
                    }
                }
            }

            is HomeAction.OnBindWidgetLauncherResult -> {
                state.value.pendingWidgetInfo?.let { widgetInfo ->
                    if (action.result.resultCode == RESULT_OK) {
                        if (widgetInfo.info.configure != null) {
                            val intent = widgetManager.buildConfigureIntent(widgetInfo.id, widgetInfo.info.configure)
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            addDisplayedWidgetList(WithmoWidgetInfo(widgetInfo))
                        }
                    } else {
                        widgetManager.deleteWidgetId(widgetInfo.id)
                    }
                }
            }
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
