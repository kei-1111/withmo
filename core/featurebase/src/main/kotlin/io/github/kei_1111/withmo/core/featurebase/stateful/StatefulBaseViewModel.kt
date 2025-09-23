package io.github.kei_1111.withmo.core.featurebase.stateful

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.featurebase.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * 状態管理を行う画面用の基底ViewModel
 *
 * このViewModelは、MVIパターンに基づいた状態管理を提供します。
 * ViewModelの内部状態（[ViewModelState]）とUIに公開される状態（[State]）を分離することで、
 * ViewModelの実装詳細をUIから隠蔽し、よりクリーンなアーキテクチャを実現します。
 *
 * ### アーキテクチャの特徴
 * - **内部状態と公開状態の分離**: ViewModelStateは内部実装用、StateはUI描画用
 * - **単方向データフロー**: Action → ViewModel → State → UI
 * - **副作用の管理**: Effectを通じて画面遷移やトースト表示などを制御
 *
 * ### 使用例
 * ```kotlin
 * // ViewModelState（内部状態）
 * data class ClockSettingsViewModelState(
 *     val clockSettings: ClockSettings = ClockSettings(),
 *     val initialClockSettings: ClockSettings = ClockSettings(),
 * ) : ViewModelState<ClockSettingsState> {
 *     override fun toState() = ClockSettingsState(
 *         clockSettings = clockSettings,
 *         isSaveButtonEnabled = clockSettings != initialClockSettings,
 *     )
 * }
 *
 * // State（UI状態）
 * data class ClockSettingsState(
 *     val clockSettings: ClockSettings = ClockSettings(),
 *     val isSaveButtonEnabled: Boolean = false,
 * ) : State
 *
 * // ViewModel実装
 * @HiltViewModel
 * class ClockSettingsViewModel @Inject constructor(
 *     private val getClockSettingsUseCase: GetClockSettingsUseCase,
 *     private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
 * ) : BaseViewModel<ClockSettingsViewModelState, ClockSettingsState, ClockSettingsAction, ClockSettingsEffect>() {
 *
 *     override fun createInitialViewModelState() = ClockSettingsViewModelState()
 *     override fun createInitialState() = ClockSettingsState()
 *
 *     override fun onAction(action: ClockSettingsAction) {
 *         when (action) {
 *             is ClockSettingsAction.OnSaveButtonClick -> saveSettings()
 *         }
 *     }
 * }
 * ```
 *
 * @param VS ViewModelStateの型。[ViewModelState]インターフェースを実装する必要があります
 * @param S Stateの型。[State]インターフェースを実装する必要があります
 * @param A Actionの型。[Action]インターフェースを実装する必要があります
 * @param E Effectの型。[Effect]インターフェースを実装する必要があります
 */
@Suppress("VariableNaming")
abstract class StatefulBaseViewModel<VS : ViewModelState<S>, S : State, A : Action, E : Effect> : ViewModel() {
    /**
     * ViewModelの内部状態を保持するMutableStateFlow
     *
     * この状態はViewModelの実装詳細を含み、UIには直接公開されません。
     * [updateViewModelState]メソッドを使用して更新します。
     */
    protected val _viewModelState = MutableStateFlow<VS>(createInitialViewModelState())

    /**
     * UIで観測される状態のStateFlow
     *
     * [_viewModelState]をViewModelStateの[toState]メソッドで変換した結果を公開します。
     * UIはこの状態を観測して画面を描画します。
     */
    val state: StateFlow<S> = _viewModelState
        .map(ViewModelState<S>::toState)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(STOP_TIMEOUT_MILLIS),
            initialValue = createInitialState(),
        )

    /**
     * エフェクトを送信するための内部チャンネル
     */
    protected val _effect = Channel<E>(Channel.BUFFERED)

    /**
     * UIで観測されるエフェクトのFlow
     *
     * 画面遷移、トースト表示、外部アプリ起動などの一度だけ実行される副作用を
     * UIに通知するために使用されます。
     */
    val effect: Flow<E> = _effect.receiveAsFlow()

    /**
     * ViewModelStateの初期値を作成する抽象メソッド
     *
     * ViewModelの初期化時に一度だけ呼ばれます。
     *
     * @return ViewModelStateの初期インスタンス
     */
    protected abstract fun createInitialViewModelState(): VS

    /**
     * Stateの初期値を作成する抽象メソッド
     *
     * [state]のStateFlowの初期値として使用されます。
     *
     * @return Stateの初期インスタンス
     */
    protected abstract fun createInitialState(): S

    /**
     * UIからのアクションを処理する抽象メソッド
     *
     * UIで発生したユーザーのインタラクション（ボタンクリック、テキスト入力など）を
     * 受け取り、適切な処理を行います。
     *
     * @param action 処理するアクション
     */
    abstract fun onAction(action: A)

    /**
     * 現在の状態を更新するためのユーティリティ関数
     *
     * この関数は、`StateFlow` に保持されている現在の状態を引数 `update` で受け取ったラムダに渡し、
     * ラムダの戻り値で新しい状態を生成し、それを [MutableStateFlow] に反映します。
     * `copy()` を使った不変データクラスの状態更新に適しており、状態の変更を明確かつ安全に行えます。
     *
     * ### 使用例
     * ```kotlin
     * updateViewModelState {
     *     copy(isModelChangeWarningDialogShown = false)
     * }
     * ```
     * 上記の例では、`isModelChangeWarningDialogShown` フラグを false に変更した新しい状態を作成し、
     * それを現在の状態として反映しています。
     *
     * ### 注意事項
     * - この関数はスレッドセーフに状態を更新するため、`_viewModelState.update {}` を内部で使用しています。
     * - 状態は `data class` で定義されている必要があります（`copy()` を使うため）。
     *
     * @param update 現在の状態 [VS] を受け取り、新しい状態を返すラムダ関数
     */
    protected fun updateViewModelState(update: VS.() -> VS) {
        _viewModelState.update { update(it) }
    }

    /**
     * エフェクトをUIに送信する
     *
     * 画面遷移、トースト表示などの副作用をUIに通知します。
     * [Channel.trySend]を使用しているため、チャンネルがいっぱいの場合は
     * エフェクトが破棄される可能性があります。
     *
     * @param effect 送信するエフェクト
     */
    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }

    private companion object {
        const val STOP_TIMEOUT_MILLIS = 5_000L
    }
}
