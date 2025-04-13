package io.github.kei_1111.withmo.ui.screens.sort_settings

import android.util.Log
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.kei_1111.withmo.domain.model.user_settings.SortType
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.GetSortSettingsUseCase
import io.github.kei_1111.withmo.domain.usecase.user_settings.sort.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.ui.base.BaseViewModel
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

    fun saveSortSettings(
        onSaveSuccess: () -> Unit,
        onSaveFailure: () -> Unit,
    ) {
        _uiState.update {
            it.copy(
                isSaveButtonEnabled = false,
            )
        }
        viewModelScope.launch {
            try {
                val sortSettings = _uiState.value.sortSettings
                saveSortSettingsUseCase(sortSettings)
                onSaveSuccess()
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save sort settings", e)
                onSaveFailure()
            }
        }
    }

    private companion object {
        const val TAG = "SortSettingsViewModel"
    }
}
