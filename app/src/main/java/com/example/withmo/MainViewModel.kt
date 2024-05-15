package com.example.withmo

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.withmo.domain.model.UserSetting
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor() : ViewModel() {
    private var uiState by mutableStateOf(false)

    fun getSettingState(): Boolean {
        return uiState
    }

    fun setSettingState(state: Boolean) {
        uiState = state
    }
}