package io.github.kei_1111.withmo.ui.screens.settings

import io.github.kei_1111.withmo.ui.base.BaseViewModel
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class SettingsViewModel @Inject constructor() : BaseViewModel<SettingsState, SettingsAction>() {

    override fun createInitialState(): SettingsState = SettingsState()

    fun changeIsDefaultHomeApp(isDefaultHomeApp: Boolean) {
        _state.update {
            it.copy(
                isDefaultHomeApp = isDefaultHomeApp,
            )
        }
    }

    private companion object {
        const val TAG = "SettingsViewModel"
    }
}
