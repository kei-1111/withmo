package io.github.kei_1111.withmo.ui.screens.theme_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.user_settings.ThemeType
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.theme.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val saveThemeSettingsUseCase: SaveThemeSettingsUseCase,
) : BaseViewModel<ThemeSettingsUiState, ThemeSettingsUiEvent>() {

    override fun createInitialState(): ThemeSettingsUiState = ThemeSettingsUiState()

    init {
        viewModelScope.launch {
            getThemeSettingsUseCase().collect { themeSettings ->
                _uiState.update {
                    it.copy(
                        themeSettings = themeSettings,
                        initialThemeSettings = themeSettings,
                    )
                }
            }
        }
    }

    fun changeThemeType(themeType: ThemeType) {
        _uiState.update {
            it.copy(
                themeSettings = it.themeSettings.copy(
                    themeType = themeType,
                ),
                isSaveButtonEnabled = themeType != it.initialThemeSettings.themeType,
            )
        }
    }

    fun saveThemeSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                val themeSettings = _uiState.value.themeSettings
                saveThemeSettingsUseCase(themeSettings)
                _uiEvent.emit(ThemeSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("ThemeSettingsViewModel", "Failed to save theme settings", e)
                _uiEvent.emit(ThemeSettingsUiEvent.SaveFailure)
            }
        }
    }
}
