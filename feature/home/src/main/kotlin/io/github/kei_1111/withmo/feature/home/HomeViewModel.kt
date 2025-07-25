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
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.util.FileUtils
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import java.lang.ref.WeakReference
import java.util.UUID
import javax.inject.Inject

@Suppress("TooManyFunctions")
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val getFavoriteAppsUseCase: GetFavoriteAppsUseCase,
    private val getPlacedItemsUseCase: GetPlacedItemsUseCase,
    private val savePlacedItemsUseCase: SavePlacedItemsUseCase,
    private val getModelChangeWarningStatusUseCase: GetModelChangeWarningStatusUseCase,
    private val markModelChangeWarningShownUseCase: MarkModelChangeWarningShownUseCase,
    private val modelFileManager: ModelFileManager,
    private val widgetManager: WidgetManager,
    private val saveModelFilePathUseCase: SaveModelFilePathUseCase,
    private val saveModelSettingsUseCase: SaveModelSettingsUseCase,
) : BaseViewModel<HomeState, HomeAction, HomeEffect>(), UnityToAndroidMessenger.MessageReceiverFromUnity {

    private var lastScaleSentTime = 0L

    override fun createInitialState(): HomeState = HomeState()

    override fun onMessageReceivedFromUnity(message: String) {
        when (message) {
            ModelLoadState.LoadingSuccess.name -> {
                updateState { copy(isModelLoading = false) }
                AndroidToUnityMessenger.sendMessage(
                    UnityObject.VRMloader,
                    UnityMethod.AdjustScale,
                    state.value.currentUserSettings.modelSettings.scale.toString(),
                )
            }
            ModelLoadState.LoadingFailure.name -> {
                updateState { copy(isModelLoading = false) }
            }
            else -> {
                Log.e(TAG, "Unknown message from Unity: $message")
            }
        }
    }

    init {
        UnityToAndroidMessenger.receiver = WeakReference(this)

        observeUserSettings()
        observeFavoriteAppList()
        observePlaceableItemList()
    }

    private fun observeUserSettings() {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                updateState {
                    copy(currentUserSettings = userSettings)
                }
            }
        }
    }

    private fun observeFavoriteAppList() {
        viewModelScope.launch {
            getFavoriteAppsUseCase().collect { favoriteAppList ->
                updateState { copy(favoriteAppInfoList = favoriteAppList.toPersistentList()) }
            }
        }
    }

    private fun observePlaceableItemList() {
        viewModelScope.launch {
            getPlacedItemsUseCase().collect { placedItemList ->
                updateState { copy(placedItemList = placedItemList.toPersistentList()) }
            }
        }
    }

    /**
     * 配置可能なアイテムをStateの配置済みリストに追加する
     *
     * @param placeableItem 追加するアイテム
     */
    private fun addPlaceableItem(placeableItem: PlaceableItem) {
        updateState { copy(placedItemList = (placedItemList + placeableItem).toPersistentList()) }
    }

    /**
     * 指定されたアイテムをStateの配置済みリストから削除する
     *
     * @param placeableItem 削除するアイテム
     */
    private fun deletePlaceableItem(placeableItem: PlaceableItem) {
        updateState {
            copy(
                placedItemList = placedItemList
                    .filterNot { it.id == placeableItem.id }
                    .toPersistentList(),
            )
        }
    }

    private fun savePlaceableItemList() {
        val currentPlacedItemList = state.value.placedItemList

        viewModelScope.launch {
            savePlacedItemsUseCase(currentPlacedItemList)
        }
    }

    @Suppress("LongMethod", "CyclomaticComplexMethod", "NestedBlockDepth")
    override fun onAction(action: HomeAction) {
        when (action) {
            is HomeAction.OnAppClick -> sendEffect(HomeEffect.LaunchApp(action.appInfo))

            is HomeAction.OnAppLongClick -> sendEffect(HomeEffect.DeleteApp(action.appInfo))

            is HomeAction.OnShowScaleSliderButtonClick -> {
                updateState { copy(isChangeModelScaleContentShown = true) }
            }

            is HomeAction.OnCloseScaleSliderButtonClick -> {
                updateState { copy(isChangeModelScaleContentShown = false) }
                viewModelScope.launch {
                    saveModelSettingsUseCase(state.value.currentUserSettings.modelSettings)
                }
            }

            is HomeAction.OnScaleSliderChange -> {
                val now = SystemClock.elapsedRealtime()
                if (now - lastScaleSentTime >= ScaleCooldownMillis) {
                    AndroidToUnityMessenger.sendMessage(UnityObject.VRMloader, UnityMethod.AdjustScale, action.scale.toString())
                    updateState {
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
                        state.value.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) } ?: false

                    if (!isDefaultModelFile) {
                        updateState { copy(isModelLoading = true) }
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
                        updateState { copy(isModelChangeWarningDialogShown = true) }
                    }
                }
            }

            is HomeAction.OnNavigateSettingsButtonClick -> sendEffect(HomeEffect.NavigateSettings)

            is HomeAction.OnModelChangeWarningDialogConfirm -> {
                viewModelScope.launch {
                    markModelChangeWarningShownUseCase()
                    updateState { copy(isModelChangeWarningDialogShown = false) }
                    sendEffect(HomeEffect.OpenDocument)
                }
            }

            is HomeAction.OnModelChangeWarningDialogDismiss -> updateState { copy(isModelChangeWarningDialogShown = false) }

            is HomeAction.OnAppListSheetSwipeUp -> {
                sendEffect(HomeEffect.ShowAppListSheet)
                updateState { copy(isAppListSheetOpened = true) }
            }

            is HomeAction.OnAppListSheetSwipeDown -> {
                sendEffect(HomeEffect.HideAppListSheet)
                updateState { copy(isAppListSheetOpened = false) }
            }

            is HomeAction.OnAddPlaceableItemButtonClick -> {
                sendEffect(HomeEffect.ShowPlaceableItemListSheet)
                updateState { copy(isPlaceableItemListSheetOpened = true) }
            }

            is HomeAction.OnPlaceableItemListSheetSwipeDown -> {
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnDisplayModelContentSwipeLeft -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerExitScreenAnimation, "")
                updateState { copy(currentPage = PageContent.PlaceableItem) }
            }

            is HomeAction.OnPlaceableItemContentSwipeRight -> {
                AndroidToUnityMessenger.sendMessage(UnityObject.IKAnimationController, UnityMethod.TriggerEnterScreenAnimation, "")
                updateState { copy(currentPage = PageContent.DisplayModel) }
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
                updateState { copy(pendingWidgetInfo = widgetInfo) }

                if (result) {
                    if (action.widgetInfo.configure != null) {
                        val intent = widgetManager.buildConfigureIntent(widgetId, action.widgetInfo.configure)
                        val activityInfo = widgetManager.getActivityInfo(action.widgetInfo.configure)

                        if (activityInfo != null && activityInfo.exported) {
                            sendEffect(HomeEffect.ConfigureWidget(intent))
                        } else {
                            addPlaceableItem(PlacedWidgetInfo(widgetInfo))
                        }
                    } else {
                        addPlaceableItem(PlacedWidgetInfo(widgetInfo))
                    }
                } else {
                    val intent = widgetManager.buildBindIntent(widgetId, provider)
                    sendEffect(HomeEffect.BindWidget(intent))
                }
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnPlaceableItemListSheetAppClick -> {
                addPlaceableItem(
                    PlacedAppInfo(
                        id = UUID.randomUUID().toString(),
                        info = action.appInfo,
                        position = Offset.Zero,
                    ),
                )
                sendEffect(HomeEffect.HidePlaceableItemListSheet)
                updateState { copy(isPlaceableItemListSheetOpened = false) }
            }

            is HomeAction.OnPlaceableItemContentLongClick -> updateState { copy(isEditMode = true) }

            is HomeAction.OnCompleteEditButtonClick -> {
                savePlaceableItemList()
                updateState { copy(isEditMode = false) }
            }

            is HomeAction.OnDeletePlaceableItemBadgeClick -> deletePlaceableItem(action.placeableItem)

            is HomeAction.OnResizeWidgetBadgeClick -> {
                updateState {
                    copy(
                        isWidgetResizing = true,
                        resizingWidget = action.placedWidgetInfo,
                    )
                }
                deletePlaceableItem(action.placedWidgetInfo)
            }

            is HomeAction.OnWidgetResizeBottomSheetClose -> {
                updateState {
                    copy(
                        isWidgetResizing = false,
                        resizingWidget = null,
                    )
                }
                addPlaceableItem(action.placedWidgetInfo)
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
                        addPlaceableItem(PlacedWidgetInfo(widgetInfo))
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
                            addPlaceableItem(PlacedWidgetInfo(widgetInfo))
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
