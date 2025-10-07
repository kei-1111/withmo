# CLAUDE.md

このファイルは Claude Code (claude.ai/code) がこのリポジトリでコード作業を行う際のガイドラインを示しています。

## プロジェクト概要

**withmo** は「デジタルフィギュア × ランチャー」のコンセプトで作られた日本語Androidランチャーアプリです。Unity as a Libraryを活用してホーム画面に3Dモデル（.vrm形式）を表示することができ、インタラクティブなランチャーとライブ壁紙の両方の役割を果たします。時間に応じた視覚変化や豊富なカスタマイズ機能が特徴です。

## SDK要件

- **minSdk**: 29 (Android 10.0)
- **compileSdk**: 36 (Android 16)
- **targetSdk**: 36 (Android 16)

## アーキテクチャ

### マルチモジュール Clean Architecture + MVI

このプロジェクトでは、全フィーチャーモジュールでMVI（Model-View-Intent）パターンによるClean Architectureを採用しています：

**Core Modules:**
- `core:common` - Unity統合、定数、ディスパッチャーなどの共通機能
- `core:data` - Roomデータベース、DataStoreリポジトリ、各種マネージャー
- `core:domain` - ユースケース、リポジトリインターフェース、ビジネスロジック
- `core:designsystem` - Composeコンポーネント、テーマ、Material 3デザインシステム
- `core:featurebase` - MVI基底クラス（ViewModelState、State、Action、Effect、BaseViewModel）
- `core:model` - ドメインモデルとユーザー設定データクラス
- `core:service` - バックグラウンドサービス（UnityWallpaperService、NotificationListener）
- `core:ui` - 共有UIユーティリティ、プロバイダー、モディファイア
- `core:util` - 汎用ユーティリティ、拡張関数

**Feature Modules:**
- `feature:home` - 3Dモデル表示とウィジェット管理を行うメインランチャー画面
- `feature:onboarding` - モデル選択とアプリ設定の初期セットアップ画面
- `feature:setting` - 各種設定画面（アプリアイコン、時計、テーマ、お気に入りなど）

### MVI実装

各フィーチャーでは統一されたデータフローによる厳格なMVIパターンを採用しています：

**主要コンポーネント:**
- **Action**: ユーザー操作をViewModelに伝達するためのアクション
- **State**: UIに公開される画面描画用の状態（StatefulBaseViewModelのみ）
- **ViewModelState**: ViewModelの内部状態（StatefulBaseViewModelのみ）
- **Effect**: ナビゲーションやトーストなど、一度だけ実行される副作用

**BaseViewModel Architecture（2種類）:**

1. **StatefulBaseViewModel** - 状態管理を行う画面用
```kotlin
abstract class StatefulBaseViewModel<VS : ViewModelState<S>, S : State, A : Action, E : Effect> : ViewModel() {
    protected val _viewModelState = MutableStateFlow<VS>(createInitialViewModelState())
    val state: StateFlow<S> = _viewModelState.map { it.toState() }.stateIn(...)

    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    protected abstract fun createInitialViewModelState(): VS
    protected abstract fun createInitialState(): S
    abstract fun onAction(action: A)

    protected fun updateViewModelState(update: VS.() -> VS)
    protected fun sendEffect(effect: E)
}
```

2. **StatelessBaseViewModel** - 状態管理が不要な画面用
```kotlin
abstract class StatelessBaseViewModel<A : Action, E : Effect> : ViewModel() {
    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()

    abstract fun onAction(action: A)
    protected fun sendEffect(effect: E)
}
```

**ViewModelStateとStateの分離:**
- **ViewModelState**: ViewModelの内部状態（実装詳細を含む、例：初期値との比較用データ）
- **State**: UIに公開される状態（UI描画に必要な情報のみ）
- ViewModelStateは`toState()`メソッドでStateに変換される
- StatelessBaseViewModelではStateを使用せず、ActionとEffectのみ

**状態管理パターン例（StatefulBaseViewModel）:**
```kotlin
// ViewModelState（内部状態）
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

// State（UI状態）
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

**状態なしパターン例（StatelessBaseViewModel）:**
```kotlin
// ViewModel（状態なし）
@HiltViewModel
class WelcomeViewModel @Inject constructor() :
    StatelessBaseViewModel<WelcomeAction, WelcomeEffect>() {

    override fun onAction(action: WelcomeAction) {
        when (action) {
            is WelcomeAction.OnNextButtonClick -> {
                sendEffect(WelcomeEffect.NavigateSelectFavoriteApp)
            }
        }
    }
}
```

**画面構成（3層構造）:**

1. **public Screen（外部公開用）** - ViewModelを受け取る層
   - `@Composable fun XxxScreen(viewModel: XxxViewModel = hiltViewModel(), ...)`
   - ViewModelを受け取り、stateとeffectを収集
   - LaunchedEffectでエフェクトを監視してナビゲーション処理
   - 内部のprivate Screenを呼び出す
   - **StatefulBaseViewModel使用時**: stateとonActionを渡す
   - **StatelessBaseViewModel使用時**: onActionのみを渡す

2. **private Screen（画面描画用）** - UI構造を定義する層
   - `@Composable private fun XxxScreen(state: XxxState, onAction: (XxxAction) -> Unit, ...)`
   - または `@Composable private fun XxxScreen(onAction: (XxxAction) -> Unit, ...)` (Stateless時)
   - stateに応じた画面全体の構造を定義（TopAppBar、Content、Buttonなど）
   - **StatefulBaseViewModel使用時**: sealed interfaceのStateで分岐（when文でIdle/Loading/Stable/Error）
   - **StatelessBaseViewModel使用時**: 状態分岐なし、直接UI構造を定義
   - Contentコンポーネントを呼び出す

3. **Content（コンテンツ描画用）** - 純粋なUI描画層
   - `@Composable fun XxxScreenContent(state: XxxState.Stable, onAction: (XxxAction) -> Unit, ...)`
   - または `@Composable fun XxxScreenContent(...)` (Stateless時)
   - 純粋なUI描画のみを担当
   - ViewModelに直接依存しない

**StatefulBaseViewModelの画面構成例:**
```kotlin
// 1. public Screen（ViewModelを受け取る）
@Composable
fun ClockSettingsScreen(
    navigateBack: () -> Unit,  // navigate[Destination]形式
    viewModel: ClockSettingsViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val currentNavigateBack by rememberUpdatedState(navigateBack)

    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is ClockSettingsEffect.NavigateBack -> currentNavigateBack()
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

// 2. private Screen（画面描画用、状態で分岐）
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
                    WithmoTopAppBar(...)
                    ClockSettingsScreenContent(
                        state = state,
                        onAction = onAction,
                        ...
                    )
                    WithmoSaveButton(...)
                }
            }
        }
    }
}

// 3. Content（純粋なUI描画）
@Composable
fun ClockSettingsScreenContent(
    state: ClockSettingsState.Stable,
    onAction: (ClockSettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    // 純粋なUI描画
}
```

**StatelessBaseViewModelの画面構成例:**
```kotlin
// 1. public Screen（ViewModelを受け取る）
@Composable
fun WelcomeScreen(
    navigateSelectFavoriteApp: () -> Unit,
    viewModel: WelcomeViewModel = hiltViewModel(),
) {
    LaunchedEffect(viewModel) {
        viewModel.effect.collect { effect ->
            when (effect) {
                is WelcomeEffect.NavigateSelectFavoriteApp -> navigateSelectFavoriteApp()
            }
        }
    }

    WelcomeScreen(
        onAction = viewModel::onAction,
        modifier = Modifier.fillMaxSize(),
    )
}

// 2. private Screen（画面描画用、状態分岐なし）
@Composable
private fun WelcomeScreen(
    onAction: (WelcomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(modifier = modifier) {
        Column {
            WelcomeScreenContent(...)
            WithmoButton(onClick = { onAction(WelcomeAction.OnNextButtonClick) }) {
                BodyMediumText(text = "次へ")
            }
        }
    }
}

// 3. Content（純粋なUI描画）
@Composable
fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
) {
    // 純粋なUI描画
}
```

**命名規則:**
- **Action**: `On` + 対象 + 過去形（例：`OnSaveButtonClick`、`OnBackButtonClick`）
- **Effect**: 命令形動詞 + 対象（例：`NavigateBack`、`ShowToast`、`OpenDocument`）
- **State**: sealed interfaceで状態を表現（例：`Idle`、`Loading`、`Stable`、`Error`）
- **ViewModelState**: data classでViewModel内部状態を保持

**データフロー例:**
1. ユーザーが戻るボタンを押す
2. `onAction(OnBackButtonClick)` が実行される
3. ViewModelが `sendEffect(NavigateBack)` を呼び出す
4. 画面のLaunchedEffectが収集してナビゲーションを実行

**状態更新の例（StatefulBaseViewModel）:**
```kotlin
// ViewModelState更新
updateViewModelState {
    copy(clockSettings = clockSettings.copy(isClockShown = true))
}

// ViewModelStateのstatusType変更でStateを切り替え
updateViewModelState {
    copy(statusType = ClockSettingsViewModelState.StatusType.LOADING)
}
```

### ナビゲーション

**Navigation Compose 2.8+** による型安全なナビゲーション実装：

**ルート定義** (`core:ui/navigation/NavigationRoute.kt`):
- インターフェース階層: `NavigationRoute` → `Graph`/`Screen`
- `@Serializable` data objectによる型安全なルート
- すべてのルート定義を単一ファイルに集約

**実装パターン**:
```kotlin
// 1. ルート定義
@Serializable
data object ClockSettings : Screen

// 2. NavController拡張関数（feature内のnavigationパッケージ）
fun NavHostController.navigateClockSettings() = navigate(ClockSettings)

// 3. NavGraphBuilder拡張関数でグラフ構築
fun NavGraphBuilder.settingsGraph(
    navigateBack: () -> Unit,
    navigateClockSettings: () -> Unit,
) {
    navigation<SettingsGraph>(startDestination = Settings) {
        composable<ClockSettings> {
            ClockSettingsScreen(navigateBack = navigateBack)
        }
    }
}

// 4. NavHostで統合（app/navigation/WithmoNavHost.kt）
NavHost(...) {
    settingsGraph(
        navigateBack = navController::popBackStack,
        navigateClockSettings = navController::navigateClockSettings,
    )
}
```

**規約**:
- 各featureモジュールに`navigation`パッケージを作成
- 拡張関数命名: `navigate[ScreenName]`形式
- グラフ関数命名: `[graphName]Graph`形式
- 戻る処理: `navController::popBackStack`を使用
- アニメーション: NavHostレベルで統一定義

### Unity as a Library統合

デュアルサーフェスレンダリングに対応した高度なUnity統合機能：

**UnityManager** (`core:common`):
- UnityPlayerのライフサイクルを管理するシングルトンクラス
- ActivityとWallpaperモード間でのサーフェス切り替え機能
- 様々なコンテキストに対応したライフサイクル管理メソッド

**メッセージングブリッジ**:
- `AndroidToUnityMessenger`: 列挙型（UnityObject/UnityMethod）による構造化メッセージング
- `UnityToAndroidMessenger`: WeakReferenceによるメモリ安全なコールバックインターフェース
- モデル読み込み、アニメーション、テーマ変更などの双方向通信

**マルチサーフェス対応**:
- Activityサーフェス: Compose UIをオーバーレイした背景レイヤーとしてのUnity
- Wallpaperサーフェス: WallpaperServiceによるライブ壁紙実装

## ビルドシステム

### Convention Plugins

`build-logic/convention/` 内のカスタムGradle convention plugins:
- `withmo.android.application` - メインアプリケーション設定
- `withmo.android.library` - Androidライブラリ設定
- `withmo.android.library.compose` - Composeライブラリ設定
- `withmo.android.feature` - MVI依存関係を含むフィーチャーモジュール設定
- `withmo.unity.library` - Unity as a Library統合設定
- `withmo.detekt` - カスタムルールによるコード品質管理
- `withmo.hilt` - 依存性注入設定

## 主要技術詳細

### データ層
- **Roomデータベース**: Flowベースのリアクティブクエリによるアプリ情報、ウィジェットデータ管理
- **DataStore**: ユーザー設定、一回限りイベントの管理（SharedPreferencesの代替）
- **ファイル管理**: 内部ストレージでのVRMモデルファイル管理

### Unity統合ポイント
- MainActivityでUnityManagerを初期化してモデル変更を監視
- HomeViewModelでコールバック用のMessageReceiverFromUnityを実装
- NotificationListenerでシステム通知によるUnityアニメーションのトリガー
- テーマシステムから時間ベースのコマンドをUnityに送信して視覚変化を制御

### 重要な制約事項
- **VRMファイルサポート**: 対応する3Dモデル形式はVRMのみです
- **NDK ABIs**: armeabi-v7aとarm64-v8aのみに対応しています

### デザインシステム
- 時間による背景変化機能を持つMaterial 3テーマ
- 包括的なローカライゼーション対応の日本語ファーストUI
- KDocドキュメンテーション付きのカスタムComposeコンポーネント

### コードスタイル・規約

**基本スタイル:**
- **Kotlinコードスタイル**: `kotlin.code.style=official` を使用
- **コメント**: コメントは基本的に追加しない（明示的な指示がない限り）
- **KDoc**: パブリックAPIには適切なKDocを記述、`@sample`タグでプレビュー関数を示す

**命名規則:**
- **const val / enum**: UPPER_SNAKE_CASE（例：`STOP_TIMEOUT_MILLIS`、`MAX_SCALE`）
- **クラス・インターフェース**: PascalCase（例：`HomeViewModel`、`GetUserSettingsUseCase`）
- **関数・変数**: camelCase（例：`onAction()`、`updateState()`、`isEnabled`）
- **Composable関数**: PascalCase（Composeの慣習、例：`HomeScreen()`、`ClockSettingsContent()`）

**Suppress警告:**
必要に応じて以下のSuppress警告を使用:
- `@Suppress("TooManyFunctions")` - ViewModelなどで関数が多い場合
- `@Suppress("ModifierMissing")` - public Screen関数でModifierが不要な場合
- `@Suppress("VariableNaming")` - MVIパターンでの`_state`、`_effect`などの命名時

**Compose規約:**
- 全てのComposable関数には`@Preview`を追加
- Material3 デザインシステムに準拠

**データ層規約:**
- Repository: インターフェースを`core:domain`に、実装を`core:data`に配置
- UseCase: 単一責任の原則、`operator fun invoke()`を使用
- 戻り値: `Result<T>`型または`Flow<Result<T>>`型でラップ
- Flow使用: データ層全体でFlowベースのリアクティブプログラミング

### 開発パターン

**新機能実装時の手順:**
1. 既存のMVIパターンを確認（StatefulまたはStateless）
2. 必要なモジュール構成を理解（core/feature分離）
3. ViewModelState、State、Action、Effectを定義
4. ViewModel実装（StatefulBaseViewModelまたはStatelessBaseViewModel継承）
5. 3層Screen構造で画面実装（public Screen → private Screen → Content）
6. 既存コンポーネントやユーティリティを活用

**重要な原則:**
- **コメント禁止**: コードにコメントを追加しない（明示的な指示がない限り）
- **既存パターンの踏襲**: 新機能追加時は既存のMVIパターンに従って実装する
- **ライブラリの確認**: 使用前に既存コードベースで使用されているか確認する
- **Unity操作**: 適切なライフサイクル管理を行う
- **セキュリティ**: シークレット・キーの露出禁止、コミット禁止

## テスト・品質管理

### 静的解析
- **Detekt**: Compose固有ルールを含む包括的な静的解析
- **Convention-based構造**: カスタムGradleプラグインによる統一的な構成
- **CI/CD**: GitHub Actionsによる自動テスト実行とコード品質チェック

### テスト実行コマンド
```bash
# 静的解析を実行
./gradlew detekt

# 全てのユニットテストを実行
./gradlew test

# 特定モジュールのテストを実行
./gradlew :core:data:test
./gradlew :core:domain:test

# テストレポートを生成
./gradlew test --continue
# レポートは各モジュールの build/reports/tests/test/index.html に生成される
```

### テスト戦略

**テスト範囲:**
- **ViewModel層**: 全フィーチャーモジュールのViewModelをテスト（StatefulBaseViewModel、StatelessBaseViewModelの両方）
- **UseCase層**: ビジネスロジックとデータ変換のテスト
- **Repository層**: DataStoreやRoomとのデータ取得・保存のテスト

**使用ライブラリ:**
- **JUnit**: テストフレームワーク
- **MockK**: Kotlinに特化したモックライブラリ
- **Turbine**: FlowとStateFlowのテスト用ライブラリ
- **kotlinx-coroutines-test**: コルーチンのテスト用ライブラリ（StandardTestDispatcher、UnconfinedTestDispatcher）

**テストガイド:**
詳細なテスト作成ガイドラインは `docs/TESTING_GUIDE.md` を参照してください：
- ViewModelテストの2つのパターン（Stateful/Stateless）
- Loading状態の有無の判断基準（単一Flow vs combine）
- State/Action/Effectの検証方法
- 高度なモックパターン（SystemClock、object、value class）
- よくあるエラーと対処法

## Git・GitHub運用規則
詳細な運用規則は `docs/GIT_GITHUB_CONVENTIONS.md` を参照してください。

### 主要なルール
- **コミットメッセージ**: `<type>: <description>` 形式で日本語、過去形で記述
- **ブランチ命名**: `<type>/#<issue-number>` 形式（例：`feature/#225`）
- **Issue/PRタイトル**: `[<Type>]: <タイトル>` 形式で統一
- **プルリクエスト**: PRテンプレートは `.github/PULL_REQUEST_TEMPLATE.md` を参照
- **開発フロー**: Issue作成 → ブランチ作成 → 開発 → PR作成 → レビュー → マージ
