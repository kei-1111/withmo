package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.SortMode
import com.example.withmo.domain.usecase.user_settings.GetUserSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.sort_mode.SaveSortModeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val saveSortModeUseCase: SaveSortModeUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<HomeUiEvent>()
    val uiEvent: MutableSharedFlow<HomeUiEvent> = _uiEvent

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

    fun onEvent(event: HomeUiEvent) {
        viewModelScope.launch {
            _uiEvent.emit(event)
        }
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                _uiState.update {
                    it.copy(currentTime = ZonedDateTime.now())
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

    fun getTime(): DateTimeInfo {
        return DateTimeInfo(
            year = _uiState.value.currentTime.year.toString(),
            month = String.format(Locale.JAPAN, "%02d", _uiState.value.currentTime.monthValue),
            day = String.format(Locale.JAPAN, "%02d", _uiState.value.currentTime.dayOfMonth),
            hour = String.format(Locale.JAPAN, "%02d", _uiState.value.currentTime.hour),
            minute = String.format(Locale.JAPAN, "%02d", _uiState.value.currentTime.minute),
            dayOfWeek = _uiState.value.currentTime.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.ENGLISH,
            ).uppercase(),
        )
    }

    fun saveSortMode(sortMode: SortMode) {
        viewModelScope.launch {
            saveSortModeUseCase(sortMode)
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

    @Suppress("MagicNumber")
    companion object {
        private const val SplashScreenDuration = 5000L
        private const val ClockUpdateInterval = 1000L
    }
}
