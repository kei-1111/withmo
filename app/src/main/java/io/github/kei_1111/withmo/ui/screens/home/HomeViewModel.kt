package io.github.kei_1111.withmo.ui.screens.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.domain.model.WidgetInfo
import io.github.kei_1111.withmo.domain.repository.AppInfoRepository
import io.github.kei_1111.withmo.domain.repository.OneTimeEventRepository
import io.github.kei_1111.withmo.domain.repository.WidgetInfoRepository
import io.github.kei_1111.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import io.github.kei_1111.withmo.utils.FileUtils
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentList
import kotlinx.collections.immutable.toPersistentMap
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@Suppress("TooManyFunctions")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetHost: AppWidgetHost,
    private val appInfoRepository: AppInfoRepository,
    private val widgetInfoRepository: WidgetInfoRepository,
    private val oneTimeEventRepository: OneTimeEventRepository,
) : BaseViewModel<HomeUiState, HomeUiEvent>() {

    override fun createInitialState(): HomeUiState = HomeUiState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    val isModelChangeWarningFirstShown = oneTimeEventRepository.isModelChangeWarningFirstShown
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = false)

    init {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                _uiState.update {
                    it.copy(currentUserSettings = userSettings)
                }
            }
        }
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { favoriteAppList ->
                _uiState.update {
                    it.copy(
                        favoriteAppList = favoriteAppList.toPersistentList(),
                    )
                }
            }
        }
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
        appWidgetHost.startListening()
    }

    fun setShowScaleSlider(show: Boolean) {
        _uiState.update {
            it.copy(isShowScaleSlider = show)
        }
    }

    suspend fun getVrmFilePath(context: Context, uri: Uri): String? {
        FileUtils.deleteAllCacheFiles(context)
        return FileUtils.copyVrmFile(context, uri)?.absolutePath
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
            it.copy(isAppListBottomSheetOpened = isAppListBottomSheetOpened)
        }
    }

    fun changeIsWidgetListBottomSheetOpened(isWidgetListBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isWidgetListBottomSheetOpened = isWidgetListBottomSheetOpened)
        }
    }

    fun getGroupedWidgetInfoMap(): ImmutableMap<String, List<AppWidgetProviderInfo>> {
        return appWidgetManager
            .installedProviders
            .groupBy { it.provider.packageName }
            .toPersistentMap()
    }

    fun selectWidget(
        widgetInfo: AppWidgetProviderInfo,
        context: Context,
        configureWidgetLauncher: ActivityResultLauncher<Intent>,
        bindWidgetLauncher: ActivityResultLauncher<Intent>,
    ) {
        val widgetId = appWidgetHost.allocateAppWidgetId()
        val provider = widgetInfo.provider

        val options = bundleOf(
            AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH to widgetInfo.minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH to widgetInfo.minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT to widgetInfo.minHeight,
            AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT to widgetInfo.minHeight,
        )

        val success = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, provider, options)

        _uiState.update {
            it.copy(
                pendingWidgetId = widgetId,
                pendingWidgetInfo = widgetInfo,
            )
        }

        if (success) {
            if (widgetInfo.configure != null) {
                val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_CONFIGURE).apply {
                    component = widgetInfo.configure
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                }

                val activityInfo = try {
                    context.packageManager.getActivityInfo(widgetInfo.configure, 0)
                } catch (e: PackageManager.NameNotFoundException) {
                    Log.e(
                        "HomeViewModel",
                        "Failed to retrieve activity info for widget configuration: ${widgetInfo.configure}",
                        e,
                    )
                    null
                }

                if (activityInfo != null && activityInfo.exported) {
                    configureWidgetLauncher.launch(intent)
                } else {
                    addDisplayedWidgetList(
                        WidgetInfo(
                            id = widgetId,
                            info = widgetInfo,
                        ),
                    )
                }
            } else {
                addDisplayedWidgetList(
                    WidgetInfo(
                        id = widgetId,
                        info = widgetInfo,
                    ),
                )
            }
        } else {
            val intent = Intent(AppWidgetManager.ACTION_APPWIDGET_BIND).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId)
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_PROVIDER, provider)
            }
            bindWidgetLauncher.launch(intent)
        }
    }

    fun addDisplayedWidgetList(widgetInfo: WidgetInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                widgetList = (currentState.widgetList + widgetInfo).toPersistentList(),
            )
        }
    }

    fun deleteWidgetId(pendingWidgetId: Int) {
        appWidgetHost.deleteAppWidgetId(pendingWidgetId)
    }

    fun createWidgetView(
        context: Context,
        widgetInfo: WidgetInfo,
        adjustWidgetWidth: Int,
        adjustWidgetHeight: Int,
    ): View {
        return appWidgetHost.createView(
            context.applicationContext,
            widgetInfo.id,
            widgetInfo.info,
        ).apply {
            val widgetSizeBundle = Bundle().apply {
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH, adjustWidgetWidth)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT, adjustWidgetHeight)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH, adjustWidgetWidth)
                putInt(AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT, adjustWidgetHeight)
            }

            updateAppWidgetSize(
                widgetSizeBundle,
                adjustWidgetWidth,
                adjustWidgetHeight,
                adjustWidgetWidth,
                adjustWidgetHeight,
            )
        }
    }

    fun deleteWidget(widgetInfo: WidgetInfo) {
        _uiState.update { currentState ->
            currentState.copy(
                widgetList = currentState.widgetList.filterNot { it.id == widgetInfo.id }
                    .toPersistentList(),
            )
        }
    }

    fun saveWidgetList() {
        val currentWidgetList = _uiState.value.widgetList
        val initialWidgetList = _uiState.value.initialWidgetList

        val addedWidgetList = currentWidgetList.filterNot { currentWidget ->
            initialWidgetList.any { initialWidget -> initialWidget.id == currentWidget.id }
        }

        val updatedWidgetList = currentWidgetList.filter { currentWidget ->
            initialWidgetList.any { initialWidget -> initialWidget.id == currentWidget.id }
        }

        val deletedWidgetList = initialWidgetList.filterNot { initialWidget ->
            currentWidgetList.any { currentWidget -> initialWidget.id == currentWidget.id }
        }

        viewModelScope.launch {
            widgetInfoRepository.insertWidget(addedWidgetList)
            widgetInfoRepository.deleteWidget(deletedWidgetList)
            widgetInfoRepository.updateWidget(updatedWidgetList)
        }
    }

    fun changeResizingWidget(widgetInfo: WidgetInfo?) {
        _uiState.update {
            it.copy(resizeWidget = widgetInfo)
        }
    }

    fun changeIsWidgetResizing(isWidgetResizing: Boolean) {
        _uiState.update {
            it.copy(isWidgetResizing = isWidgetResizing)
        }
    }

    override fun onCleared() {
        super.onCleared()
        appWidgetHost.deleteHost()
    }

    companion object {
        private const val TimeoutMillis = 5000L
    }
}
