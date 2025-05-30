package io.github.kei_1111.withmo.feature.home

import android.app.Activity.RESULT_OK
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.common.unity.UnityToAndroidMessenger
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.manager.WidgetManager
import io.github.kei_1111.withmo.core.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.core.domain.repository.WidgetInfoRepository
import io.github.kei_1111.withmo.core.domain.usecase.GetUserSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.model_file_path.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.WithmoWidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.util.FileUtils
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import javax.inject.Inject

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
                updateState { copy(isModelLoading = false) }
            }
            ModelLoadState.LoadingFailure.name -> {
                updateState { copy(isModelLoading = false) }
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
                updateState { copy(currentUserSettings = userSettings) }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                updateState { copy(favoriteAppList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    private fun observeWidgetList() {
        viewModelScope.launch {
            widgetInfoRepository.getAllWidgetList().collect { widgetList ->
                updateState {
                    copy(
                        widgetList = widgetList.toPersistentList(),
                        initialWidgetList = widgetList.toPersistentList(),
                    )
                }
            }
        }
    }

    private fun addWidget(withmoWidgetInfo: WithmoWidgetInfo) {
        updateState { copy(widgetList = (widgetList + withmoWidgetInfo).toPersistentList()) }
    }

    private fun deleteWidget(withmoWidgetInfo: WithmoWidgetInfo) {
        updateState {
            copy(
                widgetList = widgetList
                    .filterNot { it.widgetInfo.id == withmoWidgetInfo.widgetInfo.id }
                    .toPersistentList(),
            )
        }
    }

    private fun saveWidgetList() {
        val currentWidgetList = state.value.widgetList
        val initialWidgetList = state.value.initialWidgetList

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

    @Suppress("LongMethod", "CyclomaticComplexMethod", "NestedBlockDepth")
    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnAppClick -> sendEffect(HomeEffect.LaunchApp(action.appInfo))

            is HomeAction.OnAppLongClick -> sendEffect(HomeEffect.DeleteApp(action.appInfo))

            is HomeAction.OnShowScaleSliderButtonClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.ShowObject, "")
                updateState { copy(isCloseScaleSliderButtonShown = true) }
            }

            is HomeAction.OnCloseScaleSliderButtonClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.SliderManeger, UnityMethod.HideObject, "")
                updateState { copy(isCloseScaleSliderButtonShown = false) }
            }

            is HomeAction.OnSetDefaultModelButtonClick -> {
                viewModelScope.launch {
                    val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                    val isDefaultModelFile =
                        state.value.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: false

                    if (!isDefaultModelFile) {
                        updateState { copy(isModelLoading = true) }
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
                        updateState { copy(isModelChangeWarningDialogShown = true) }
                    }
                }
            }

            is HomeAction.OnNavigateSettingsButtonClick -> sendEffect(HomeEffect.NavigateSettings)

            is HomeAction.OnModelChangeWarningDialogConfirm -> {
                viewModelScope.launch {
                    oneTimeEventRepository.markModelChangeWarningFirstShown()
                    updateState { copy(isModelChangeWarningDialogShown = false) }
                    sendEffect(HomeEffect.OpenDocument)
                }
            }

            is HomeAction.OnModelChangeWarningDialogDismiss -> updateState { copy(isModelChangeWarningDialogShown = false) }

            is HomeAction.OnAppSearchQueryChange -> updateState { copy(appSearchQuery = action.query) }

            is HomeAction.OnAppListSheetSwipeUp -> {
                sendEffect(HomeEffect.ShowAppListSheet)
                updateState { copy(isAppListSheetOpened = true) }
            }

            is HomeAction.OnAppListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideAppListSheet)
                updateState { copy(isAppListSheetOpened = false) }
            }

            is HomeAction.OnAddWidgetButtonClick -> {
                sendEffect(HomeEffect.ShowWidgetListSheet)
                updateState { copy(isWidgetListSheetOpened = true) }
            }

            is HomeAction.OnWidgetListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideWidgetListSheet)
                updateState { copy(isWidgetListSheetOpened = false) }
            }

            is HomeAction.OnDisplayModelContentSwipeLeft -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerExitScreenAnimation, "")
                updateState { copy(currentPage = PageContent.Widget) }
            }

            is HomeAction.OnWidgetContentSwipeRight -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerEnterScreenAnimation, "")
                updateState { copy(currentPage = PageContent.DisplayModel) }
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
                updateState { copy(pendingWidgetInfo = widgetInfo) }

                if (result) {
                    if (action.widgetInfo.configure != null) {
                        val intent = widgetManager.buildConfigureIntent(widgetId, action.widgetInfo.configure)
                        val activityInfo = widgetManager.getActivityInfo(action.widgetInfo.configure)

                        if (activityInfo != null && activityInfo.exported) {
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            addWidget(WithmoWidgetInfo(widgetInfo))
                        }
                    } else {
                        addWidget(WithmoWidgetInfo(widgetInfo))
                    }
                } else {
                    val intent = widgetManager.buildBindIntent(widgetId, provider)
                    sendEffect(HomeEffect.BindWidget(intent))
                }
                sendEffect(HomeEffect.HideWidgetListSheet)
                updateState { copy(isWidgetListSheetOpened = false) }
            }

            is HomeAction.OnWidgetContentLongClick -> updateState { copy(isEditMode = true) }

            is HomeAction.OnCompleteEditButtonClick -> {
                saveWidgetList()
                updateState { copy(isEditMode = false) }
            }

            is HomeAction.OnDeleteWidgetBadgeClick -> deleteWidget(action.withmoWidgetInfo)

            is HomeAction.OnResizeWidgetBadgeClick -> {
                updateState {
                    copy(
                        isWidgetResizing = true,
                        resizingWidget = action.withmoWidgetInfo,
                    )
                }
                deleteWidget(action.withmoWidgetInfo)
            }

            is HomeAction.OnWidgetResizeBottomSheetClose -> {
                updateState {
                    copy(
                        isWidgetResizing = false,
                        resizingWidget = null,
                    )
                }
                addWidget(action.withmoWidgetInfo)
            }

            is HomeAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    if (action.uri == null) {
                        sendEffect(HomeEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        updateState { copy(isModelLoading = true) }
                        modelFileManager.deleteCopiedCacheFiles()
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(HomeEffect.ShowToast("ファイルの読み込みに失敗しました"))
                            updateState { copy(isModelLoading = false) }
                        } else {
                            if (filePath == state.value.currentUserSettings.modelFilePath.path) {
                                sendEffect(HomeEffect.ShowToast("同じファイルが選択されています"))
                                updateState { copy(isModelLoading = false) }
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
                        addWidget(WithmoWidgetInfo(widgetInfo))
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
                            addWidget(WithmoWidgetInfo(widgetInfo))
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
