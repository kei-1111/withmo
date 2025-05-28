package io.github.kei_1111.withmo.ui.screens.sort_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.core.domain.usecase.sort.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.sort.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.featurebase.BaseViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortSettingsViewModel @Inject constructor(
    private val getSortSettingsUseCase: GetSortSettingsUseCase,
    private val saveSortSettingsUseCase: SaveSortSettingsUseCase,
) : BaseViewModel<SortSettingsState, SortSettingsAction, SortSettingsEffect>() {

    override fun createInitialState(): SortSettingsState = SortSettingsState()

    init {
        observerSortSettings()
    }

    private fun observerSortSettings() {
        viewModelScope.launch {
            getSortSettingsUseCase().collect { sortSettings ->
                updateState {
                    copy(
                        sortSettings = sortSettings,
                        initialSortSettings = sortSettings,
                    )
                }
            }
        }
    }

    private fun saveSortSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                val sortSettings = state.value.sortSettings
                saveSortSettingsUseCase(sortSettings)
                sendEffect(SortSettingsEffect.ShowToast("保存しました"))
                sendEffect(SortSettingsEffect.NavigateBack)
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save sort settings", e)
                sendEffect(SortSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    override fun onAction(action: SortSettingsAction) {
        when (action) {
            is SortSettingsAction.OnSortTypeRadioButtonClick -> {
                updateState {
                    val updatedSortSettings = sortSettings.copy(sortType = action.sortType)
                    copy(
                        sortSettings = updatedSortSettings,
                        isSaveButtonEnabled = updatedSortSettings != initialSortSettings,
                    )
                }
            }

            is SortSettingsAction.OnSaveButtonClick -> saveSortSettings()

            is SortSettingsAction.OnBackButtonClick -> sendEffect(SortSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "SortSettingsViewModel"
    }
}
