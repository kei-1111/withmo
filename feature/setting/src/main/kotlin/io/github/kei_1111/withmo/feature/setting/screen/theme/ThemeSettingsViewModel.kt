package io.github.kei_1111.withmo.feature.setting.screen.theme

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.stateful.StatefulBaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class ThemeSettingsViewModel @Inject constructor(
    getThemeSettingsUseCase: GetThemeSettingsUseCase,
    private val saveThemeSettingsUseCase: SaveThemeSettingsUseCase,
) : StatefulBaseViewModel<ThemeSettingsViewModelState, ThemeSettingsState, ThemeSettingsAction, ThemeSettingsEffect>() {

    override fun createInitialViewModelState() = ThemeSettingsViewModelState()
    override fun createInitialState() = ThemeSettingsState.Idle

    private val themeSettingsDataStream = getThemeSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = ThemeSettingsViewModelState.StatusType.LOADING) }
            themeSettingsDataStream.collect { result ->
                result
                    .onSuccess { themeSettings ->
                        updateViewModelState {
                            copy(
                                statusType = ThemeSettingsViewModelState.StatusType.STABLE,
                                themeSettings = themeSettings,
                                initialThemeSettings = themeSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = ThemeSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
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
                        saveThemeSettingsUseCase(_viewModelState.value.themeSettings)
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
