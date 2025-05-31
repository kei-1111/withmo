package io.github.kei_1111.withmo.core.featurebase

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update

@Suppress("VariableNaming")
abstract class BaseViewModel<S : State, A : Action, E : Effect> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun createInitialState(): S

    abstract fun onAction(action: A)

    /**
     * 現在の状態 [state] を更新するためのユーティリティ関数。
     *
     * この関数は、`StateFlow` に保持されている現在の状態を引数 `update` で受け取ったラムダに渡し、
     * ラムダの戻り値で新しい状態を生成し、それを [MutableStateFlow] に反映します。
     * `copy()` を使った不変データクラスの状態更新に適しており、状態の変更を明確かつ安全に行えます。
     *
     * ### 使用例
     * ```kotlin
     * updateState {
     *     copy(isModelChangeWarningDialogShown = false)
     * }
     * ```
     * 上記の例では、`isModelChangeWarningDialogShown` フラグを false に変更した新しい状態を作成し、
     * それを現在の状態として反映しています。
     *
     * ### 注意事項
     * - この関数はスレッドセーフに状態を更新するため、`_state.update {}` を内部で使用しています。
     * - 状態は `data class` で定義されている必要があります（`copy()` を使うため）。
     *
     * @param update 現在の状態 [S] を受け取り、新しい状態を返すラムダ関数
     */
    fun updateState(update: S.() -> S) {
        _state.update { update(it) }
    }

    fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
