package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsUiState, SettingsAction>() {

    override fun createInitialState(): SettingsUiState = SettingsUiState()

    fun changeIsDefaultHomeApp(isDefaultHomeApp: Boolean) {
        _uiState.update {
            it.copy(
                isDefaultHomeApp = isDefaultHomeApp,
            )
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}
