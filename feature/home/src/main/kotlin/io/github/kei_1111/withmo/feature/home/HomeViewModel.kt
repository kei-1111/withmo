package io.github.kei_1111.withmo.feature.home

import android.app.Activity.RESULT_OK
import android.os.SystemClock
import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.common.unity.UnityToAndroidMessenger
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.manager.WidgetManager
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetModelChangeWarningStatusUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetPlacedItemsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetUserSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.MarkModelChangeWarningShownUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SavePlacedItemsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.github.kei_1111.withmo.core.util.FileUtils
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.UUID
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class HomeViewModel @Inject constructor(
    getUserSettingsUseCase: GetUserSettingsUseCase,
    getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    getPlacedItemsUseCase: GetPlacedItemsUseCase,
    private val savePlacedItemsUseCase: SavePlacedItemsUseCase,
    private val getModelChangeWarningStatusUseCase: GetModelChangeWarningStatusUseCase,
    private val markModelChangeWarningShownUseCase: MarkModelChangeWarningShownUseCase,
    private val modelFileManager: ModelFileManager,
    private val widgetManager: WidgetManager,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val saveModelSettingsUseCase: SaveModelSettingsUseCase,
) : StatefulBaseViewModel<HomeViewModelState, HomeState, HomeAction, HomeEffect>(), UnityToAndroidMessenger.MessageReceiverFromUnity {

    private var lastScaleSentTime = 0L

    override fun createInitialViewModelState() = HomeViewModelState()
    override fun createInitialState() = HomeState.Idle

    private data class HomeData(
        val userSettings: UserSettings,
        val favoriteAppList: List<FavoriteAppInfo>,
        val placedItemList: List<PlaceableItem>,
    )
    private val homeDataStream: Flow<Result<HomeData>> = combine(
        getUserSettingsUseCase(),
        getFavoriteAppsUseCase(),
        getPlacedItemsUseCase(),
    ) { userSettings, favoriteAppList, placedItemList ->
        runCatching {
            HomeData(
                userSettings = userSettings.getOrThrow(),
                favoriteAppList = favoriteAppList.getOrThrow(),
                placedItemList = placedItemList.getOrThrow(),
            )
        }
    }

    override fun onMessageReceivedFromUnity(message: String) {
        when (message) {
            ModelLoadState.LoadingSuccess.name -> {
                updateViewModelState { copy(isModelLoading = false) }
                AndroidToUnityMessenger.sendMessage(
                    UnityObject.VRMloader,
                    UnityMethod.AdjustScale,
                    _viewModelState.value.currentUserSettings.modelSettings.scale.toString(),
                )
            }
            ModelLoadState.LoadingFailure.name -> {
                updateViewModelState { copy(isModelLoading = false) }
            }
            else -> {
                Log.e(TAG, "Unknown message from Unity: $message")
            }
        }
    }

    init {
        UnityToAndroidMessenger.receiver = WeakReference(this)

        viewModelScope.launch {
            updateViewModelState { copy(statusType = HomeViewModelState.StatusType.LOADING) }
            homeDataStream.collect { result ->
                result
                    .onSuccess { data ->
                        updateViewModelState {
                            copy(
                                statusType = HomeViewModelState.StatusType.STABLE,
                                currentUserSettings = data.userSettings,
                                favoriteAppInfoList = data.favoriteAppList.toPersistentList(),
                                placedItemList = data.placedItemList.toPersistentList(),
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = HomeViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod", "NestedBlockDepth")
    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnAppClick -> {
                sendEffect(HomeEffect.LaunchApp(action.appInfo))
            }

            is HomeAction.OnAppLongClick -> {
                sendEffect(HomeEffect.DeleteApp(action.appInfo))
            }

            is HomeAction.OnShowScaleSliderButtonClick -> {
                updateViewModelState { copy(isChangeModelScaleContentShown = true) }
            }

            is HomeAction.OnCloseScaleSliderButtonClick -> {
                updateViewModelState { copy(isChangeModelScaleContentShown = false) }
                viewModelScope.launch {
                    saveModelSettingsUseCase(_viewModelState.value.currentUserSettings.modelSettings)
                }
            }

            is HomeAction.OnScaleSliderChange -> {
                val now = SystemClock.elapsedRealtime()
                if (now - lastScaleSentTime >= ScaleCooldownMillis) {
                    AndroidToUnityMessenger.sendMessage(UnityObject.VRMloader, UnityMethod.AdjustScale, action.scale.toString())
                    updateViewModelState {
                        copy(
                            currentUserSettings = currentUserSettings.copy(
                                modelSettings = currentUserSettings.modelSettings.copy(scale = action.scale),
                            ),
                        )
                    }
                }
            }

            is HomeAction.OnSetDefaultModelButtonClick -> {
                viewModelScope.launch {
                    val defaultModelFilePath = modelFileManager.copyVrmFileFromAssets()?.absolutePath
                    val isDefaultModelFile =
                        _viewModelState.value.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: false

                    if (!isDefaultModelFile) {
                        updateViewModelState { copy(isModelLoading = true) }
                        saveModelFilePathUseCase(ModelFilePath(defaultModelFilePath))
                    }
                }
            }

            is HomeAction.OnOpenDocumentButtonClick -> {
                viewModelScope.launch {
                    val isModelChangeWarningFirstShown = getModelChangeWarningStatusUseCase()
                    if (isModelChangeWarningFirstShown) {
                        sendEffect(HomeEffect.OpenDocument)
                    } else {
                        updateViewModelState { copy(isModelChangeWarningDialogShown = true) }
                    }
                }
            }

            is HomeAction.OnNavigateSettingsButtonClick -> {
                sendEffect(HomeEffect.NavigateSettings)
            }

            is HomeAction.OnModelChangeWarningDialogConfirm -> {
                viewModelScope.launch {
                    markModelChangeWarningShownUseCase()
                    updateViewModelState { copy(isModelChangeWarningDialogShown = false) }
                    sendEffect(HomeEffect.OpenDocument)
                }
            }

            is HomeAction.OnModelChangeWarningDialogDismiss -> {
                updateViewModelState { copy(isModelChangeWarningDialogShown = false) }
            }

            is HomeAction.OnAppListSheetSwipeUp -> {
                sendEffect(HomeEffect.ShowAppListSheet)
                updateViewModelState { copy(isAppListSheetOpened = true) }
            }

            is HomeAction.OnAppListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideAppListSheet)
                updateViewModelState { copy(isAppListSheetOpened = false) }
            }

            is HomeAction.OnAddPlaceableItemButtonClick -> {
                sendEffect(HomeEffect.ShowPlaceableItemListSheet)
                updateViewModelState { copy(isPlaceableItemListSheetOpened = true) }
            }

            is HomeAction.OnPlaceableItemListSheetSwipeDown -> {
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateViewModelState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnDisplayModelContentSwipeLeft -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerExitScreenAnimation, "")
                updateViewModelState { copy(currentPage = PageContent.PlaceableItem) }
            }

            is HomeAction.OnPlaceableItemContentSwipeRight -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerEnterScreenAnimation, "")
                updateViewModelState { copy(currentPage = PageContent.DisplayModel) }
            }

            is HomeAction.OnDisplayModelContentClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.MoveLookat, "${action.x},${action.y}")
            }

            is HomeAction.OnDisplayModelContentLongClick -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.VRMAnimationController, UnityMethod.TriggerTouchAnimation, "")
            }

            is HomeAction.OnPlaceableItemListSheetWidgetClick -> {
                val widgetId = widgetManager.allocateId()
                val provider = action.widgetInfo.provider
                val result = widgetManager.bindAppWidgetId(widgetId, provider, action.widgetInfo.minWidth, action.widgetInfo.minHeight)

                val widgetInfo = WidgetInfo(widgetId, action.widgetInfo)
                updateViewModelState { copy(pendingWidgetInfo = widgetInfo) }

                if (result) {
                    if (action.widgetInfo.configure != null) {
                        val intent = widgetManager.buildConfigureIntent(widgetId, action.widgetInfo.configure)
                        val activityInfo = widgetManager.getActivityInfo(action.widgetInfo.configure)

                        if (activityInfo != null && activityInfo.exported) {
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            updateViewModelState { copy(placedItemList = (placedItemList + PlacedWidgetInfo(widgetInfo)).toPersistentList()) }
                        }
                    } else {
                        updateViewModelState { copy(placedItemList = (placedItemList + PlacedWidgetInfo(widgetInfo)).toPersistentList()) }
                    }
                } else {
                    val intent = widgetManager.buildBindIntent(widgetId, provider)
                    sendEffect(HomeEffect.BindWidget(intent))
                }
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateViewModelState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnPlaceableItemListSheetAppClick -> {
                updateViewModelState {
                    copy(
                        placedItemList = (
                            placedItemList + PlacedAppInfo(
                                id = UUID.randomUUID().toString(),
                                info = action.appInfo,
                                position = Offset.Zero,
                            )
                            ).toPersistentList(),
                    )
                }
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateViewModelState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnPlaceableItemContentLongClick -> {
                updateViewModelState { copy(isEditMode = true) }
            }

            is HomeAction.OnCompleteEditButtonClick -> {
                viewModelScope.launch {
                    savePlacedItemsUseCase(_viewModelState.value.placedItemList)
                }
                updateViewModelState { copy(isEditMode = false) }
            }

            is HomeAction.OnDeletePlaceableItemBadgeClick -> {
                updateViewModelState {
                    copy(
                        placedItemList = placedItemList
                            .filterNot { it.id == action.placeableItem.id }
                            .toPersistentList(),
                    )
                }
            }

            is HomeAction.OnResizeWidgetBadgeClick -> {
                updateViewModelState {
                    copy(
                        isWidgetResizing = true,
                        resizingWidget = action.placedWidgetInfo,
                        placedItemList = placedItemList
                            .filterNot { it.id == action.placedWidgetInfo.id }
                            .toPersistentList(),
                    )
                }
            }

            is HomeAction.OnWidgetResizeBottomSheetClose -> {
                updateViewModelState {
                    copy(
                        isWidgetResizing = false,
                        resizingWidget = null,
                        placedItemList = (placedItemList + action.placedWidgetInfo).toPersistentList(),
                    )
                }
            }

            is HomeAction.OnOpenDocumentLauncherResult -> {
                viewModelScope.launch {
                    if (action.uri == null) {
                        sendEffect(HomeEffect.ShowToast("ファイルが選択されませんでした"))
                    } else {
                        updateViewModelState { copy(isModelLoading = true) }
                        modelFileManager.deleteCopiedCacheFiles()
                        val filePath = modelFileManager.copyVrmFileFromUri(action.uri)?.absolutePath
                        if (filePath == null) {
                            sendEffect(HomeEffect.ShowToast("ファイルの読み込みに失敗しました"))
                            updateViewModelState { copy(isModelLoading = false) }
                        } else {
                            if (filePath == _viewModelState.value.currentUserSettings.modelFilePath.path) {
                                sendEffect(HomeEffect.ShowToast("同じファイルが選択されています"))
                                updateViewModelState { copy(isModelLoading = false) }
                            } else {
                                saveModelFilePathUseCase(ModelFilePath(filePath))
                            }
                        }
                    }
                }
            }

            is HomeAction.OnConfigureWidgetLauncherResult -> {
                _viewModelState.value.pendingWidgetInfo?.let { widgetInfo ->
                    if (action.result.resultCode == RESULT_OK) {
                        updateViewModelState { copy(placedItemList = (placedItemList + PlacedWidgetInfo(widgetInfo)).toPersistentList()) }
                    } else {
                        widgetManager.deleteWidgetId(widgetInfo.id)
                    }
                }
            }

            is HomeAction.OnBindWidgetLauncherResult -> {
                _viewModelState.value.pendingWidgetInfo?.let { widgetInfo ->
                    if (action.result.resultCode == RESULT_OK) {
                        if (widgetInfo.info.configure != null) {
                            val intent = widgetManager.buildConfigureIntent(widgetInfo.id, widgetInfo.info.configure)
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            updateViewModelState { copy(placedItemList = (placedItemList + PlacedWidgetInfo(widgetInfo)).toPersistentList()) }
                        }
                    } else {
                        widgetManager.deleteWidgetId(widgetInfo.id)
                    }
                }
            }
        }
    }

    private companion object {
        const val ScaleCooldownMillis = 16L

        const val TAG = "HomeViewModel"
    }
}

enum class ModelLoadState {
    LoadingSuccess,
    LoadingFailure,
}
