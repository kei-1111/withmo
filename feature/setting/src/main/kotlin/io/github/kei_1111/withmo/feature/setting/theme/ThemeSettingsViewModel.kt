package io.github.kei_1111.withmo.feature.setting.theme

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThemeSettingsViewModel @Inject constructor(
    private val getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val saveThemeSettingsUseCase: SaveThemeSettingsUseCase,
) : StatefulBaseViewModel<ThemeSettingsViewModelState, ThemeSettingsState, ThemeSettingsAction, ThemeSettingsEffect>() {

    override fun createInitialViewModelState() = ThemeSettingsViewModelState()
    override fun createInitialState() = ThemeSettingsState()

    init {
        observeThemeSettings()
    }

    private fun observeThemeSettings() {
        viewModelScope.launch {
            getThemeSettingsUseCase().collect { themeSettings ->
                updateViewModelState {
                    copy(
                        themeSettings = themeSettings,
                        initialThemeSettings = themeSettings,
                    )
                }
            }
        }
    }

    override fun onAction(action: ThemeSettingsAction) {
        when (action) {
            is ThemeSettingsAction.OnThemeTypeRadioButtonClick -> {
                updateViewModelState {
                    val updatedThemeSettings = themeSettings.copy(themeType = action.themeType)
                    copy(themeSettings = updatedThemeSettings)
                }
            }

            is ThemeSettingsAction.OnSaveButtonClick -> {
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

            is ThemeSettingsAction.OnBackButtonClick -> {
                sendEffect(ThemeSettingsEffect.NavigateBack)
            }
        }
    }

    private companion object {
        const val TAG = "ThemeSettingsViewModel"
    }
}
