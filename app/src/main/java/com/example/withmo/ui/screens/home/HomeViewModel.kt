package com.example.withmo.ui.screens.home

import android.appwidget.AppWidgetHost
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.annotation.RequiresApi
import androidx.core.os.bundleOf
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.WidgetInfo
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.repository.AppInfoRepository
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortTypeUseCase
import com.example.withmo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@Suppress("TooManyFunctions")
@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val saveSortTypeUseCase: SaveSortTypeUseCase,
    private val appWidgetManager: AppWidgetManager,
    private val appWidgetHost: AppWidgetHost,
    private val appInfoRepository: AppInfoRepository,
) : BaseViewModel<HomeUiState, HomeUiEvent>() {

    override fun createInitialState(): HomeUiState = HomeUiState()

    val appList: StateFlow<List<AppInfo>> = appInfoRepository.getAllAppInfoList()
        .stateIn(viewModelScope, started = SharingStarted.WhileSubscribed(TimeoutMillis), initialValue = emptyList())

    init {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                _uiState.update {
                    it.copy(currentUserSettings = userSettings)
                }
            }
        }
        viewModelScope.launch {
            appInfoRepository.getFavoriteAppInfoList().collect { appInfoList ->
                _uiState.update {
                    it.copy(favoriteAppList = appInfoList.toPersistentList())
                }
            }
        }
        startClock()
        appWidgetHost.startListening()
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                _uiState.update {
                    it.copy(currentTime = getTime(ZonedDateTime.now()))
                }
                delay(ClockUpdateInterval)
            }
        }
    }

    private fun getTime(currentTime: ZonedDateTime): DateTimeInfo {
        return DateTimeInfo(
            year = currentTime.year.toString(),
            month = String.format(Locale.JAPAN, "%02d", currentTime.monthValue),
            day = String.format(Locale.JAPAN, "%02d", currentTime.dayOfMonth),
            hour = String.format(Locale.JAPAN, "%02d", currentTime.hour),
            minute = String.format(Locale.JAPAN, "%02d", currentTime.minute),
            dayOfWeek = currentTime.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.ENGLISH,
            ).uppercase(),
        )
    }

    fun saveSortType(sortType: SortType) {
        viewModelScope.launch {
            saveSortTypeUseCase(sortType)
        }
    }

    fun setShowScaleSlider(show: Boolean) {
        _uiState.update {
            it.copy(isShowScaleSlider = show)
        }
    }

    fun setPopupExpanded(expanded: Boolean) {
        _uiState.update {
            it.copy(isExpandPopup = expanded)
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

    fun changeIsActionSelectionBottomSheetOpened(isActionSelectionBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isActionSelectionBottomSheetOpened = isActionSelectionBottomSheetOpened)
        }
    }

    fun changeIsWidgetListBottomSheetOpened(isWidgetListBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isWidgetListBottomSheetOpened = isWidgetListBottomSheetOpened)
        }
    }

    fun getWidgetInfoList(): ImmutableList<AppWidgetProviderInfo> {
        return appWidgetManager.installedProviders.toPersistentList()
    }

    fun selectWidget(
        widgetInfo: AppWidgetProviderInfo,
        context: Context,
        configureWidgetLauncher: ActivityResultLauncher<Intent>,
        bindWidgetLauncher: ActivityResultLauncher<Intent>,
    ) {
        val widgetId = appWidgetHost.allocateAppWidgetId()
        val provider = widgetInfo.provider

        _uiState.update {
            it.copy(
                pendingWidgetId = widgetId,
                pendingWidgetInfo = widgetInfo,
            )
        }

        val options = bundleOf(
            AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH to widgetInfo.minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MAX_WIDTH to widgetInfo.minWidth,
            AppWidgetManager.OPTION_APPWIDGET_MIN_HEIGHT to widgetInfo.minHeight,
            AppWidgetManager.OPTION_APPWIDGET_MAX_HEIGHT to widgetInfo.minHeight,
        )

        val success = appWidgetManager.bindAppWidgetIdIfAllowed(widgetId, provider, options)

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
                displayedWidgetList = (currentState.displayedWidgetList + widgetInfo).toPersistentList(),
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
                displayedWidgetList = currentState.displayedWidgetList.filterNot { it.id == widgetInfo.id }
                    .toPersistentList(),
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        appWidgetHost.deleteHost()
    }

    companion object {
        private const val ClockUpdateInterval = 1000L
        private const val TimeoutMillis = 5000L
    }
}
