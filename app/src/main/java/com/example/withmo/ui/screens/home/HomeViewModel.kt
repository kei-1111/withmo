package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.DateTimeInfo
import com.example.withmo.domain.model.UserSetting
import com.example.withmo.domain.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.ZonedDateTime
import java.time.format.TextStyle
import java.util.Locale
import javax.inject.Inject

@RequiresApi(Build.VERSION_CODES.O)
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userSettingRepository: UserSettingRepository,
) : ViewModel() {
    var uiState by mutableStateOf(HomeScreenUiState())
        private set

    val userSetting: StateFlow<UserSetting> = userSettingRepository.userSetting
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = UserSetting(),
        )

    init {
        splashScreen()
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                uiState = uiState.copy(currentTime = ZonedDateTime.now())
                delay(ClockUpdateInterval)
            }
        }
    }

    private fun splashScreen() {
        viewModelScope.launch {
            delay(SplashScreenDuration)
            uiState = uiState.copy(finishSplashScreen = !uiState.finishSplashScreen)
        }
    }

    fun getTime(): DateTimeInfo {
        return DateTimeInfo(
            year = uiState.currentTime.year.toString(),
            month = String.format(Locale.JAPAN, "%02d", uiState.currentTime.monthValue),
            day = String.format(Locale.JAPAN, "%02d", uiState.currentTime.dayOfMonth),
            hour = String.format(Locale.JAPAN, "%02d", uiState.currentTime.hour),
            minute = String.format(Locale.JAPAN, "%02d", uiState.currentTime.minute),
            dayOfWeek = uiState.currentTime.dayOfWeek.getDisplayName(
                TextStyle.SHORT,
                Locale.ENGLISH,
            ).uppercase(),
        )
    }

    fun saveUserSetting(userSetting: UserSetting) {
        viewModelScope.launch {
            userSettingRepository.saveUserSetting(userSetting)
        }
    }

    fun setShowScaleSlider(show: Boolean) {
        uiState = uiState.copy(showScaleSlider = show)
    }

    fun setPopupExpanded(expanded: Boolean) {
        uiState = uiState.copy(popupExpanded = expanded)
    }

    @Suppress("MagicNumber")
    companion object {
        private const val SplashScreenDuration = 5000L
        private const val ClockUpdateInterval = 1000L
    }
}
