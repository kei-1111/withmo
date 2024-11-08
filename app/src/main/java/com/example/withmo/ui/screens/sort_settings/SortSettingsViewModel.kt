package com.example.withmo.ui.screens.sort_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.withmo.domain.model.user_settings.SortType
import com.example.withmo.domain.usecase.user_settings.sort.GetSortSettingsUseCase
import com.example.withmo.domain.usecase.user_settings.sort.SaveSortSettingsUseCase
import com.example.withmo.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SortSettingsViewModel @Inject constructor(
    private val getSortSettingsUseCase: GetSortSettingsUseCase,
    private val saveSortSettingsUseCase: SaveSortSettingsUseCase,
) : BaseViewModel<SortSettingsUiState, SortSettingsUiEvent>() {

    override fun createInitialState(): SortSettingsUiState = SortSettingsUiState()

    init {
        observerSortSettings()
    }

    private fun observerSortSettings() {
        viewModelScope.launch {
            getSortSettingsUseCase().collect { sortSettings ->
                _uiState.update {
                    it.copy(
                        sortSettings = sortSettings,
                        initialSortSettings = sortSettings,
                    )
                }
            }
        }
    }

    fun changeSortType(sortType: SortType) {
        _uiState.update {
            it.copy(
                sortSettings = it.sortSettings.copy(
                    sortType = sortType,
                ),
                isSaveButtonEnabled = sortType != it.initialSortSettings.sortType,
            )
        }
    }

    fun saveSortSettings() {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                val sortSettings = _uiState.value.sortSettings
                saveSortSettingsUseCase(sortSettings)
                _uiEvent.emit(SortSettingsUiEvent.SaveSuccess)
            } catch (e: Exception) {
                Log.e("SortSettingsViewModel", "Failed to save sort settings", e)
                _uiEvent.emit(SortSettingsUiEvent.SaveFailure)
            }
        }
    }
}
