package io.github.kei_1111.withmo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@Suppress("VariableNaming")
abstract class BaseViewModel<S : State, A : Action> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _action = MutableSharedFlow<A>()
    val action: SharedFlow<A> = _action

    abstract fun createInitialState(): S

    fun onAction(action: A) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }
}
