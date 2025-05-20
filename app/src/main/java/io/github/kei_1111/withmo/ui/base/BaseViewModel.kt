package io.github.kei_1111.withmo.ui.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@Suppress("VariableNaming")
abstract class BaseViewModel<S : State, A : Action, E : Effect> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _action = MutableSharedFlow<A>()
    val action: SharedFlow<A> = _action

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun createInitialState(): S

    fun onAction(action: A) {
        viewModelScope.launch {
            _action.emit(action)
        }
    }

    fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
