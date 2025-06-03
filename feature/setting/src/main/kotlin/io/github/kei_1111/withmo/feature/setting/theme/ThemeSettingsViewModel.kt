package io.github.kei_1111.withmo.feature.setting.theme

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val saveThemeSettingsUseCase: SaveThemeSettingsUseCase,
) : BaseViewModel<ThemeSettingsState, ThemeSettingsAction, ThemeSettingsEffect>() {

    override fun createInitialState(): ThemeSettingsState = ThemeSettingsState()

    init {
        observeThemeSettings()
    }

    private fun observeThemeSettings() {
        viewModelScope.launch {
            getThemeSettingsUseCase().collect { themeSettings ->
                updateState {
                    copy(
                        themeSettings = themeSettings,
                        initialThemeSettings = themeSettings,
                    )
                }
            }
        }
    }

    private fun saveThemeSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                val themeSettings = state.value.themeSettings
                saveThemeSettingsUseCase(themeSettings)
                sendEffect(ThemeSettingsEffect.ShowToast("保存しました"))
                sendEffect(ThemeSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save theme settings", e)
                sendEffect(ThemeSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: ThemeSettingsAction) {
        when (action) {
            is ThemeSettingsAction.OnThemeTypeRadioButtonClick -> {
                updateState {
                    val updatedThemeSettings = themeSettings.copy(themeType = action.themeType)
                    copy(
                        themeSettings = updatedThemeSettings,
                        isSaveButtonEnabled = updatedThemeSettings != initialThemeSettings,
                    )
                }
            }

            is ThemeSettingsAction.OnSaveButtonClick -> saveThemeSettings()

            is ThemeSettingsAction.OnBackButtonClick -> sendEffect(ThemeSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "ThemeSettingsViewModel"
    }
}
