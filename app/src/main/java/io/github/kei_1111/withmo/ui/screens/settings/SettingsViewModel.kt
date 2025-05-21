package io.github.kei_1111.withmo.ui.screens.settings

import androidx.lifecycle.viewModelScope
import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsState, SettingsAction, SettingsEffect>() {

    override fun createInitialState(): SettingsState = SettingsState()

    fun changeIsDefaultHomeApp(isDefaultHomeApp: Boolean) {
        _state.update {
            it.copy(
                isDefaultHomeApp = isDefaultHomeApp,
            )
        }
    }

    override fun onAction(action: SettingsAction) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}
