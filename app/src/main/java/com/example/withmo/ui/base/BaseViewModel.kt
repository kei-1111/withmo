package com.example.withmo.ui.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class BaseViewModel<S: UiState, E: UiEvent>: ViewModel() {
    protected val _uiState = MutableStateFlow<S>(createInitialState())
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    protected val _uiEvent = MutableSharedFlow<E>()
    val uiEvent: SharedFlow<E> = _uiEvent

    abstract fun createInitialState(): S
}