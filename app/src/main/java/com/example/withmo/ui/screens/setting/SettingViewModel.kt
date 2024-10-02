package com.example.withmo.ui.screens.setting

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.SettingMode
import com.example.withmo.domain.model.UserSetting
import com.example.withmo.domain.repository.UserSettingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val userSettingRepository: UserSettingRepository,
) : ViewModel() {
    var uiState by mutableStateOf(SettingScreenUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                currentUserSetting = userSettingRepository.userSetting.first(),
            )
        }
    }

    fun saveUserSetting() {
        viewModelScope.launch {
            userSettingRepository.saveUserSetting(uiState.currentUserSetting)
        }
    }

    fun setCurrentUserSetting(currentUserSetting: UserSetting) {
        uiState = uiState.copy(currentUserSetting = currentUserSetting)
    }

    fun setSettingMode(settingMode: SettingMode) {
        uiState = uiState.copy(settingMode = settingMode)
    }

    fun setShowNotificationCheckDialog(show: Boolean) {
        uiState = uiState.copy(showNotificationCheckDialog = show)
    }

    fun setShowFileAccessCheckDialog(show: Boolean) {
        uiState = uiState.copy(showFileAccessCheckDialog = show)
    }
}
