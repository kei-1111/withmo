# アーキテクチャ概要
本プロジェクトでは MVI をベースにしたアーキテクチャを採用しています。
データの流れは以下のようになっています。
<img src="https://github.com/user-attachments/assets/3000e51d-887e-4fa1-b610-6a928d6aaf62" width="50%" />

- **Action** … ユーザ操作を ViewModel へ渡す入力
- **State** … UIに公開される画面描画用の状態
- **ViewModelState** … ViewModelの内部状態（StatefulBaseViewModelのみ）
- **Effect** … ナビゲーション・Toast など一度だけ UI が実行する副作用

## BaseViewModelの種類

画面の特性に応じて2種類のBaseViewModelを使い分けています。

### 1. StatefulBaseViewModel（状態管理を行う画面用）

状態管理が必要な画面で使用します。ViewModelの内部状態（ViewModelState）とUIに公開される状態（State）を分離することで、ViewModelの実装詳細をUIから隠蔽します。

```kt
interface Action
interface Effect
interface State
interface ViewModelState<S : State> {
    fun toState(): S
}

abstract class StatefulBaseViewModel<VS : ViewModelState<S>, S : State, A : Action, E : Effect> : ViewModel() {
    protected val _viewModelState = MutableStateFlow<VS>(createInitialViewModelState())
    val state: StateFlow<S> = _viewModelState.map { it.toState() }.stateIn(...)

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun createInitialViewModelState(): VS
    abstract fun createInitialState(): S
    abstract fun onAction(action: A)

    protected fun updateViewModelState(update: VS.() -> VS) {
        _viewModelState.update { update(it) }
    }

    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
```

### 2. StatelessBaseViewModel（状態管理が不要な画面用）

状態管理が不要で、単純な画面遷移のみを行う画面（ウェルカム画面など）で使用します。

```kt
abstract class StatelessBaseViewModel<A : Action, E : Effect> : ViewModel() {
    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onAction(action: A)

    protected fun sendEffect(effect: E) {
        _effect.trySend(effect)
    }
}
```

# データフロー（例：[時計設定画面](https://github.com/kei-1111/withmo/tree/main/app/src/main/java/io/github/kei_1111/withmo/ui/screens/clock_settings)）
時計設定画面とは、画面左上に時計を表示させるか、どのようなタイプの時計を表示させるかを設定する画面です。  
最初に、時計設定画面にはどのような State, Action, Effect, ViewModel, Screen があるのかを説明し、最後にデータフローについて述べます。  
時計設定画面とは以下の画面です。これを用いて詳細なデータフローについて説明していきます。
| 時計設定画面 |
|-----|
| <img src="https://github.com/user-attachments/assets/d68dc1e6-8ee4-48a6-889f-30358a826f31" width="300" /> |


## ViewModelStateとState

時計設定画面では、ViewModelの内部状態（ViewModelState）とUIに公開される状態（State）を分離しています。

### ViewModelState（内部状態）
ViewModelの実装詳細を含む内部状態です。`statusType`で画面の状態（IDLE/LOADING/STABLE/ERROR）を管理し、`toState()`メソッドでStateに変換します。

```kt
data class ClockSettingsViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val error: Throwable? = null,
) : ViewModelState<ClockSettingsState> {
    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> ClockSettingsState.Idle
        StatusType.LOADING -> ClockSettingsState.Loading
        StatusType.STABLE -> ClockSettingsState.Stable(
            clockSettings = clockSettings,
            isSaveButtonEnabled = clockSettings != initialClockSettings,
        )
        StatusType.ERROR -> ClockSettingsState.Error(error ?: Throwable("Unknown error"))
    }
}
```

### State（UI状態）
UIに公開される状態です。sealed interfaceで定義し、画面の状態に応じた分岐を行います。

```kt
sealed interface ClockSettingsState : State {
    data object Idle : ClockSettingsState
    data object Loading : ClockSettingsState
    data class Stable(
        val clockSettings: ClockSettings,
        val isSaveButtonEnabled: Boolean,
    ) : ClockSettingsState
    data class Error(val error: Throwable) : ClockSettingsState
}
```

## Action
時計設定画面では、時計を表示させるかどうかを切り替えるSwitchと、どの種類の時計を表示させるかを選ぶRadioButton、ユーザが設定した設定値を保存するためのSaveButton、一つ前の画面に戻るBackButtonがあります。それぞれを実行したときのActionがあり、ViewModelには、このActionを受け取りどのような処理を行うのかを決める`onAction`があります。この`onAction`では、受け取ったActionの種類に応じて値を変更したり、画面遷移するためのEffectをUIに流したりします。
```kt
sealed interface ClockSettingsAction : Action {
    data class OnIsClockShownSwitchChange(val isClockShown: Boolean) : ClockSettingsAction
    data class OnClockTypeRadioButtonClick(val clockType: ClockType) : ClockSettingsAction
    data object OnSaveButtonClick : ClockSettingsAction
    data object OnBackButtonClick : ClockSettingsAction
}
```

## Effect
時計設定画面では、前の画面に戻る`NavigateBack`と、Toastを表示させる`ShowToast`があります。ViewModelを受け取る大元となるScreenではEffectを購読して、処理を実行します。
```kt
sealed interface ClockSettingsEffect : Effect {
    data object NavigateBack : ClockSettingsEffect
    data class ShowToast(val message: String) : ClockSettingsEffect
}
```

## ViewModel
ここまで、定義した ViewModelState, State, Action, Effect を StatefulBaseViewModel に渡して、それを継承した`ClockSettingsViewModel`を作成します。Actionの説明のときに述べた`onAction`でActionを受け取りこれに応じて処理を行います。この`onAction`によって、ViewModel内の他のメソッドをUIに表示せずにすみ、ユーザ操作と状態の管理を分離させることができます。

```kt
@HiltViewModel
class ClockSettingsViewModel @Inject constructor(
    getClockSettingsUseCase: GetClockSettingsUseCase,
    private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
) : StatefulBaseViewModel<ClockSettingsViewModelState, ClockSettingsState, ClockSettingsAction, ClockSettingsEffect>() {

    override fun createInitialViewModelState() = ClockSettingsViewModelState()
    override fun createInitialState() = ClockSettingsState.Idle

    private val clockSettingsDataStream = getClockSettingsUseCase()

    init {
        viewModelScope.launch {
            updateViewModelState { copy(statusType = ClockSettingsViewModelState.StatusType.LOADING) }
            clockSettingsDataStream.collect { result ->
                result
                    .onSuccess { clockSettings ->
                        updateViewModelState {
                            copy(
                                statusType = ClockSettingsViewModelState.StatusType.STABLE,
                                clockSettings = clockSettings,
                                initialClockSettings = clockSettings,
                            )
                        }
                    }
                    .onFailure { error ->
                        updateViewModelState {
                            copy(
                                statusType = ClockSettingsViewModelState.StatusType.ERROR,
                                error = error,
                            )
                        }
                    }
            }
        }
    }

    // 設定値を保存するメソッド
    private fun saveClockSettings() {
        viewModelScope.launch {
            try {
                saveClockSettingsUseCase(_viewModelState.value.clockSettings)
                sendEffect(ClockSettingsEffect.NavigateBack)
                sendEffect(ClockSettingsEffect.ShowToast("保存しました"))
            } catch (e: Exception) {
                Log.e(TAG, "Failed to save clock settings", e)
                sendEffect(ClockSettingsEffect.ShowToast("保存に失敗しました"))
            }
        }
    }

    /*** 処理を実行するonAction ***/
    override fun onAction(action: ClockSettingsAction) {
        when (action) {
            is ClockSettingsAction.OnIsClockShownSwitchChange -> {
                updateViewModelState {
                    val updatedClockSettings = clockSettings.copy(isClockShown = action.isClockShown)
                    copy(clockSettings = updatedClockSettings)
                }
            }

            is ClockSettingsAction.OnClockTypeRadioButtonClick -> {
                updateViewModelState {
                    val updatedClockSettings = clockSettings.copy(clockType = action.clockType)
                    copy(clockSettings = updatedClockSettings)
                }
            }

            is ClockSettingsAction.OnSaveButtonClick -> saveClockSettings()

            is ClockSettingsAction.OnBackButtonClick -> sendEffect(ClockSettingsEffect.NavigateBack)
        }
    }

    private companion object {
        const val TAG = "ClockSettingsViewModel"
    }
}
```

## Screen

画面は3層構造で構成されています。

### 1. public Screen（外部公開用）
ViewModelを受け取り、stateとeffectを収集します。ここでは、ViewModelの`effect`を購読し、送られてきたEffectに応じて処理を行います。これにより、ViewModel側にUIのメソッドを渡さなくてすむという利点があります。

```kt
@Suppress("ModifierMissing")
@Composable
fun ClockSettingsScreen(
    onBackButtonClick: () -> Unit,
    viewModel: ClockSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val currentOnBackButtonClick by rememberUpdatedState(onBackButtonClick)

    BackHandler {
        viewModel.onAction(ClockSettingsAction.OnBackButtonClick)
    }

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClockSettingsEffect.NavigateBack -> currentOnBackButtonClick()
                is ClockSettingsEffect.ShowToast -> showToast(context, effect.message)
            }
        }
    }

    ClockSettingsScreen(
        state = state,
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}
```

### 2. private Screen（画面描画用）
stateに応じた画面全体の構造を定義します。sealed interfaceのStateで分岐し、各状態に応じたUIを表示します。

```kt
@Composable
private fun ClockSettingsScreen(
    state: ClockSettingsState,
    onAction: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    when (state) {
        ClockSettingsState.Idle, ClockSettingsState.Loading -> { /* Loading UI */ }
        is ClockSettingsState.Error -> { /* Error UI */ }
        is ClockSettingsState.Stable -> {
            Surface(modifier = modifier) {
                Column {
                    WithmoTopAppBar(
                        content = { TitleLargeText(text = "時計") },
                        navigateBack = { onAction(ClockSettingsAction.OnBackButtonClick) },
                    )
                    ClockSettingsScreenContent(
                        state = state,
                        onAction = onAction,
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(Weights.Medium)
                            .verticalScroll(rememberScrollState()),
                    )
                    WithmoSaveButton(
                        onClick = { onAction(ClockSettingsAction.OnSaveButtonClick) },
                        enabled = state.isSaveButtonEnabled,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(Paddings.Medium),
                    )
                }
            }
        }
    }
}
```

### 3. Content（コンテンツ描画用）
純粋なUI描画のみを担当します。ViewModelに直接依存せず、再利用可能なコンポーネントとして設計されています。詳細は[こちら](https://github.com/kei-1111/withmo/blob/main/feature/setting/src/main/kotlin/io/github/kei_1111/withmo/feature/setting/clock/component/ClockSettingsScreenContent.kt)をご覧ください。

## データフロー
1. ユーザが戻るボタンを押す
2. `{ onAction(ClockSettingsAction.OnBackButtonClick) }`が実行される
3. `onAction`は`ClockSettingsAction.OnBackButtonClick`を受け取り、以下の処理が行われEffectをtrySendする
```kt
override fun onAction(action: ClockSettingsAction) {
        when (action) {

    /*** 中略 ***/

            is ClockSettingsAction.OnBackButtonClick -> sendEffect(ClockSettingsEffect.NavigateBack)
        }
    }
```
4. 購読していたScreenが処理を実行する。今回は、前の画面に戻る。
```kt
LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClockSettingsEffect.NavigateBack -> currentOnBackButtonClick()

    /*** 中略 ***/
```


# 命名規約
最後に、これらアーキテクチャに置いてどのような命名規則を行っているのかについて説明します。  
概念: ルール / 例  
Action:	On + 対象 + 過去形 / OnOpenDocumentLauncherResult, OnNavigateSettingsButtonClick  
Effect:	命令形動詞 + 目的語 / OpenDocument, NavigateBack, ShowToast  
State: 名詞 / clockSettings, isSaveButtonEnabled  
