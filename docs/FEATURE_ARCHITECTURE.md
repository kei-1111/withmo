# アーキテクチャ概要
本プロジェクトでは MVI をベースにしたアーキテクチャを採用しています。
データの流れは以下のようになっています。  
<img src="https://github.com/user-attachments/assets/3000e51d-887e-4fa1-b610-6a928d6aaf62" width="50%" />

- **Action** … ユーザ操作を ViewModel へ渡す入力
- **State** … 画面描画に必要なデータ
- **Effect** … ナビゲーション・Toast など一度だけ UI が実行する副作用

また、この処理をしやすくするためにベースとなるViewModelを作成し、そこでEffectを流す処理や、状態を変更するためのメソッドを定義しています。各画面では、このViewModelを継承し使用しています。
```kt
interface Action

interface Effect

interface State

abstract class BaseViewModel<S : State, A : Action, E : Effect> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun createInitialState(): S

    abstract fun onAction(action: A)

    fun updateState(update: S.() -> S) {
        _state.update { update(it) }
    }

    fun sendEffect(effect: E) {
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


## State
時計設定画面では、設定する`clockSettings`と、画面を開いたときの初期時計設定を保持しておくための`initialClockSettings`、保存ボタンが押せるかを判断する（初期時計設定と設定値が違うかどうかを判断する）`isSaveButtonEnabled`があります。
```kt
data class ClockSettingsState(
    val clockSettings: ClockSettings = ClockSettings(),
    val initialClockSettings: ClockSettings = ClockSettings(),
    val isSaveButtonEnabled: Boolean = false,
) : State
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
ここまで、定義した State, Action, Effect をBaseViewModelに渡して、それを継承した`ClockSettingsViewModel`を作成します。Actionの説明のときに述べた`onAction`でActionを受け取りこれに応じて処理を行います。この`onAction`によって、ViewModel内の他のメソッドをUIに表示せずにすみ、ユーザ操作と状態の管理を分裂を指せることができます。
```kt
@HiltViewModel
class ClockSettingsViewModel @Inject constructor(
    private val getClockSettingsUseCase: GetClockSettingsUseCase,
    private val saveClockSettingsUseCase: SaveClockSettingsUseCase,
) : BaseViewModel<ClockSettingsState, ClockSettingsAction, ClockSettingsEffect>() {

    override fun createInitialState(): ClockSettingsState = ClockSettingsState()

    init {
        observeClockSettings()
    }

    // 保存された設定値を購読するための関数
    private fun observeClockSettings() {
        viewModelScope.launch {
            getClockSettingsUseCase().collect { clockSettings ->
                updateState {
                    copy(
                        clockSettings = clockSettings,
                        initialClockSettings = clockSettings,
                    )
                }
            }
        }
    }

    // 設定値を保存するメソッド
    private fun saveClockSettings() {
        updateState { copy(isSaveButtonEnabled = false) }
        viewModelScope.launch {
            try {
                saveClockSettingsUseCase(state.value.clockSettings)
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
                updateState {
                    val updatedClockSettings = clockSettings.copy(isClockShown = action.isClockShown)
                    copy(
                        clockSettings = updatedClockSettings,
                        isSaveButtonEnabled = updatedClockSettings != initialClockSettings,
                    )
                }
            }

            is ClockSettingsAction.OnClockTypeRadioButtonClick -> {
                updateState {
                    val updatedClockSettings = clockSettings.copy(clockType = action.clockType)
                    copy(
                        clockSettings = updatedClockSettings,
                        isSaveButtonEnabled = updatedClockSettings != initialClockSettings,
                    )
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
ScreenはViewModelを引数とする大元となるScreenと、`state`と`onAction`を受け取りUI表示のみを行うScreenの2種類があります。アーキテクチャの説明において重要となるのは大元となるScreenだと判断し、ここで紹介します。UI表示のみも興味があれば[ご覧ください](https://github.com/kei-1111/withmo/blob/main/app/src/main/java/io/github/kei_1111/withmo/ui/screens/clock_settings/ClockSettingsScreen.kt)。  
ここでは、ViewModelの`effect`を購読し、送られてきたEffectに応じて処理を行います。これにより、ViewModel側にUIのメソッドを渡さなくてすむという利点があります。
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
