package com.example.withmo.ui.screens.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.UserSetting
import com.example.withmo.domain.model.UserSettingRepository
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
    private val userSettingRepository: UserSettingRepository
) : ViewModel() {
    var uiState by mutableStateOf(HomeScreenUiState())
    private set

    val userSetting: StateFlow<UserSetting> = userSettingRepository.userSetting
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UserSetting()
        )

    init {
        splashScreen()
        startClock()
    }

    private fun startClock() {
        viewModelScope.launch {
            while (true) {
                uiState = uiState.copy(currentTime = ZonedDateTime.now())
                delay(1000)
            }
        }
    }

    private fun splashScreen() {
        viewModelScope.launch {
            delay(5000)
            uiState = uiState.copy(finishSprashScreen = !uiState.finishSprashScreen)
        }
    }

    fun getTime(): List<String> {
        return listOf(
            uiState.currentTime.year.toString(),
            String.format("%02d", uiState.currentTime.monthValue),
            String.format("%02d", uiState.currentTime.dayOfMonth),
            String.format("%02d", uiState.currentTime.hour),
            String.format("%02d", uiState.currentTime.minute),
            uiState.currentTime.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH)
                .uppercase()
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
}