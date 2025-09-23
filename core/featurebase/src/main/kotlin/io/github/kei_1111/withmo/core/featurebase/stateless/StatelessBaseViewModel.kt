package io.github.kei_1111.withmo.core.featurebase.stateless

import androidx.lifecycle.ViewModel
import io.github.kei_1111.withmo.core.featurebase.Action
import io.github.kei_1111.withmo.core.featurebase.Effect
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * 状態管理が不要な画面用の基底ViewModel
 *
 * このViewModelは、状態（State）の管理が不要で、アクション（Action）とエフェクト（Effect）のみを
 * 扱う画面で使用します。ウェルカム画面など、単純な画面遷移のみを行う画面で使用されます。
 *
 * ### 使用例
 * ```kotlin
 * @HiltViewModel
 * class WelcomeViewModel @Inject constructor() :
 *     StatelessBaseViewModel<WelcomeAction, WelcomeEffect>() {
 *
 *     override fun onAction(action: WelcomeAction) {
 *         when (action) {
 *             is WelcomeAction.OnGetStartedButtonClick -> {
 *                 sendEffect(WelcomeEffect.NavigateToOnboarding)
 *             }
 *         }
 *     }
 * }
 * ```
 *
 * @param A アクションの型。[Action]インターフェースを実装する必要があります
 * @param E エフェクトの型。[Effect]インターフェースを実装する必要があります
 */
@Suppress("VariableNaming")
abstract class StatelessBaseViewModel<A : Action, E : Effect> : ViewModel() {
    /**
     * エフェクトを送信するための内部チャンネル
     *
     * バッファリングされたチャンネルを使用することで、エフェクトの送信時に
     * サスペンドされることを防ぎます。
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
     * UIからのアクションを処理する抽象メソッド
     *
     * UIで発生したユーザーのインタラクション（ボタンクリック、テキスト入力など）を
     * 受け取り、適切な処理を行います。必要に応じて[sendEffect]を使用して
     * UIにエフェクトを送信します。
     *
     * @param action 処理するアクション
     */
    abstract fun onAction(action: A)

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
}
