package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortTypeUseCase
import com.example.withmo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserSettingsUseCase: GetUserSettingsUseCase,
    private val saveSortTypeUseCase: SaveSortTypeUseCase,
) : BaseViewModel<HomeUiState, HomeUiEvent>() {

    override fun createInitialState(): HomeUiState = HomeUiState()

    init {
        viewModelScope.launch {
            getUserSettingsUseCase().collect { userSettings ->
                _uiState.update {
                    it.copy(currentUserSettings = userSettings)
                }
            }
        }
        splashScreenDuration()
        startClock()
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

    private fun splashScreenDuration() {
        viewModelScope.launch {
            delay(SplashScreenDuration)
            _uiState.update {
                it.copy(isFinishSplashScreen = true)
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

    fun changeIsBottomSheetOpened(isBottomSheetOpened: Boolean) {
        _uiState.update {
            it.copy(isBottomSheetOpened = isBottomSheetOpened)
        }
    }

    companion object {
        private const val SplashScreenDuration = 5000L
        private const val ClockUpdateInterval = 1000L
    }
}
