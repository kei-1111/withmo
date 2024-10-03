package com.example.withmo.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppViewModel @Inject constructor() : ViewModel() {
    var uiState by mutableStateOf(false)
        private set

    fun navigateToSettingScreen() {
        uiState = true
    }

    fun navigateToHomeScreen() {
        uiState = false
    }
}
