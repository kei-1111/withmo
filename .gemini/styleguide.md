# withmoプロジェクト コードレビュー StyleGuide

## 概要
このStyleGuideは、withmoプロジェクトのPRに対してGemini Code Assistantが**日本語で**コードレビューを行う際の指針として作成されています。

### レビュー規則
- **すべてのコードレビューは日本語で行ってください**
- コメントやフィードバックは**日本語**で記述してください
- 技術的な議論も**日本語**を基本としてください
- **特に問題がない場合は「LGTM」を返してください**

## プロジェクト構造

### アーキテクチャ
- **アーキテクチャパターン**: Clean Architecture + MVI (Model-View-Intent)
- **UI フレームワーク**: Jetpack Compose
- **依存性注入**: Hilt
- **モジュール構造**: マルチモジュール構成

### モジュール構成
```
:app - メインアプリケーションモジュール
:core - 共通機能モジュール群
  :common - 共通定数・ユーティリティ
  :data - データ層（Repository実装、Database、API）
  :domain - ドメイン層（UseCase、Repository interface）
  :model - データモデル
  :designsystem - デザインシステム・UIコンポーネント
  :ui - UI共通機能
  :util - ユーティリティ関数
  :service - システムサービス
  :featurebase - 機能共通基盤（BaseViewModel等）
:feature - 機能別モジュール群
  :home - ホーム画面機能
  :onboarding - オンボーディング機能
  :setting - 設定画面機能
:build-logic - ビルド設定
```

## コーディング規約

### 1. 命名規則

#### ファイル・クラス命名
- **Screen**: `[機能名]Screen.kt` (例: `HomeScreen.kt`, `AppIconSettingsScreen.kt`)
- **ViewModel**: `[機能名]ViewModel.kt` (例: `HomeViewModel.kt`)
- **ViewModelState**: `[機能名]ViewModelState.kt` (例: `HomeViewModelState.kt`) ※StatefulBaseViewModelのみ
- **State**: `[機能名]State.kt` (例: `HomeState.kt`) ※StatefulBaseViewModelのみ
- **Action**: `[機能名]Action.kt` (例: `HomeAction.kt`)
- **Effect**: `[機能名]Effect.kt` (例: `HomeEffect.kt`)
- **Component**: `[ComponentName].kt` (例: `AppItem.kt`)
- **UseCase**: `[動詞][名詞]UseCase.kt` (例: `GetUserSettingsUseCase.kt`)
- **Repository**: `[名詞]Repository.kt` + `[名詞]RepositoryImpl.kt`

#### パッケージ構造
```
feature/[featurename]/src/main/kotlin/.../screens/[screen_name]/
├── [ScreenName]Screen.kt
├── [ScreenName]ViewModel.kt
├── [ScreenName]ViewModelState.kt (StatefulBaseViewModelのみ)
├── [ScreenName]State.kt (StatefulBaseViewModelのみ)
├── [ScreenName]Action.kt
├── [ScreenName]Effect.kt
└── component/
    ├── [ScreenName]ScreenContent.kt
    └── [ComponentName].kt (その他のコンポーネント)
```

**実際のパッケージ例:**
```
feature/setting/src/main/kotlin/.../screens/clock/
├── ClockSettingsScreen.kt
├── ClockSettingsViewModel.kt
├── ClockSettingsViewModelState.kt
├── ClockSettingsState.kt
├── ClockSettingsAction.kt
├── ClockSettingsEffect.kt
└── component/
    ├── ClockSettingsScreenContent.kt
    └── ClockTypePicker.kt

feature/onboarding/src/main/kotlin/.../screens/welcome/
├── WelcomeScreen.kt
├── WelcomeViewModel.kt
├── WelcomeAction.kt (StatelessBaseViewModelのため)
├── WelcomeEffect.kt (ViewModelState/Stateファイルなし)
└── component/
    └── WelcomeScreenContent.kt
```

#### MVIアーキテクチャの命名規則（docs/FEATURE_ARCHITECTURE.mdより）

**Action**: `On + 対象 + 動作`
```kotlin
// 例
OnOpenDocumentLauncherResult
OnNavigateSettingsButtonClick
OnIsClockShownSwitchChange
OnClockTypeRadioButtonClick
OnSaveButtonClick
OnBackButtonClick
```

**Effect**: `命令形動詞 + 目的語`
```kotlin
// 例
OpenDocument
NavigateBack
ShowToast
NavigateSettings
```

**State**: `名詞`
```kotlin
// 例（プロパティ名）
clockSettings
initialClockSettings
isSaveButtonEnabled
isLoading
errorMessage
```

### 2. Compose コンポーネント規約

#### Composable関数
```kotlin
@Composable
fun ComponentName(
    // 必須パラメータ
    requiredParam: String,
    // オプションパラメータ（デフォルト値あり）
    modifier: Modifier = Modifier,
    optionalParam: Boolean = false,
) {
    // 実装
}
```

#### Preview実装パターン
**必須**: 全てのComposableにLight/Darkの両方のPreviewを実装する
```kotlin

@Preview
@Composable
private fun ComponentNameLightPreview() {
    [ModuleName]LightPreviewEnvironment {
        ComponentName(
            requiredParam = "サンプルデータ",
        )
    }
}


@Preview
@Composable
private fun ComponentNameDarkPreview() {
    [ModuleName]DarkPreviewEnvironment {
        ComponentName(
            requiredParam = "サンプルデータ",
        )
    }
}
```

### 3. MVI パターン

#### BaseViewModel（2種類）

**1. StatefulBaseViewModel - 状態管理を行う画面用**
```kotlin
@HiltViewModel
class FeatureNameViewModel @Inject constructor(
    private val useCase: SomeUseCase,
) : StatefulBaseViewModel<FeatureNameViewModelState, FeatureNameState, FeatureNameAction, FeatureNameEffect>() {

    override fun createInitialViewModelState() = FeatureNameViewModelState()

    override fun createInitialState() = FeatureNameState.Idle

    override fun onAction(action: FeatureNameAction) {
        when (action) {
            // アクション処理
            // updateViewModelState { copy(...) } で状態更新
            // sendEffect(...) でエフェクト送信
        }
    }
}
```

**2. StatelessBaseViewModel - 状態管理が不要な画面用**
```kotlin
@HiltViewModel
class FeatureNameViewModel @Inject constructor() :
    StatelessBaseViewModel<FeatureNameAction, FeatureNameEffect>() {

    override fun onAction(action: FeatureNameAction) {
        when (action) {
            // アクション処理
            // sendEffect(...) でエフェクト送信のみ
        }
    }
}
```

#### ViewModelState（StatefulBaseViewModelのみ）
```kotlin
data class FeatureNameViewModelState(
    val statusType: StatusType = StatusType.IDLE,
    val data: SomeData = SomeData(),
    val initialData: SomeData = SomeData(),
    val error: Throwable? = null,
) : ViewModelState<FeatureNameState> {
    enum class StatusType { IDLE, LOADING, STABLE, ERROR }

    override fun toState() = when (statusType) {
        StatusType.IDLE -> FeatureNameState.Idle
        StatusType.LOADING -> FeatureNameState.Loading
        StatusType.STABLE -> FeatureNameState.Stable(
            data = data,
            isEnabled = data != initialData,
        )
        StatusType.ERROR -> FeatureNameState.Error(error ?: Throwable("Unknown error"))
    }
}
```

#### State
```kotlin
// StatefulBaseViewModelの場合: sealed interface
sealed interface FeatureNameState : State {
    data object Idle : FeatureNameState
    data object Loading : FeatureNameState
    data class Stable(
        val data: SomeData,
        val isEnabled: Boolean,
    ) : FeatureNameState
    data class Error(val error: Throwable) : FeatureNameState
}

// StatelessBaseViewModelの場合: Stateは不要
```

#### Action
```kotlin
sealed interface FeatureNameAction : Action {
    data object OnButtonClick : FeatureNameAction
    data class OnInputChanged(val input: String) : FeatureNameAction
}
```

#### Effect
```kotlin
sealed interface FeatureNameEffect : Effect {
    data object NavigateToNext : FeatureNameEffect
    data class ShowToast(val message: String) : FeatureNameEffect
}
```

### 4. UI コンポーネント設計

#### デザインシステム
- `:core:designsystem`内のコンポーネントを最優先で使用
- **Withmo接頭語は汎用コンポーネントを拡張する場合のみ付ける**（例：WithmoButton、WithmoTextField）
- Material3をベースとし、カスタムテーマを適用

#### コンポーネント階層
```
WithmoApp (最上位)
├── WithmoTheme (テーマ適用)
├── Screen (画面レベル)
├── Content (画面内容)
├── Component (再利用可能コンポーネント)
└── Primitive (基本UI要素)
```

### 5. ナビゲーション

#### Navigation Compose
- **Type-safe Navigation**: `@Serializable` data objectによる型安全なルート定義
- **Graph構造**: 関連画面をまとめたNavigation Graph
- **拡張関数**: NavHostControllerの拡張関数による画面遷移

#### ルート定義（:core:ui/navigation/NavigationRoute.kt）
```kotlin
interface NavigationRoute

interface Graph : NavigationRoute

interface Screen : NavigationRoute

@Serializable
data object Home : Screen

@Serializable
data object OnboardingGraph : Graph

@Serializable
data object Settings : Screen
```

#### ナビゲーション実装パターン

**1. 各featureモジュールでnavigationパッケージを作成**
```
feature/[featurename]/src/main/kotlin/.../navigation/
└── [FeatureName]Navigation.kt
```

**2. NavHostController拡張関数の定義**
```kotlin
// 画面遷移用の拡張関数
fun NavHostController.navigateSettings() = navigate(Settings)

fun NavHostController.navigateClockSettings() = navigate(ClockSettings)

// バックスタック制御を含む遷移
fun NavHostController.navigateHome() = navigate(Home) {
    popUpTo(OnboardingGraph) {
        inclusive = true
    }
}
```

**3. NavGraphBuilder拡張関数でグラフを構築**
```kotlin
fun NavGraphBuilder.settingsGraph(
    navigateBack: () -> Unit,
    navigateClockSettings: () -> Unit,
    navigateAppIconSettings: () -> Unit,
    // その他のナビゲーション関数
) {
    navigation<SettingsGraph>(
        startDestination = Settings,
    ) {
        composable<Settings> {
            SettingsScreen(
                navigateBack = navigateBack,
                navigateClockSettings = navigateClockSettings,
                navigateAppIconSettings = navigateAppIconSettings,
            )
        }

        composable<ClockSettings> {
            ClockSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        // 他の画面定義
    }
}
```

**4. NavHostの構成（:app/navigation/WithmoNavHost.kt）**
```kotlin
@Composable
fun WithmoNavHost(
    navController: NavHostController,
    startDestination: NavigationRoute,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        onboardingGraph(
            navigateBack = navController::popBackStack,
            navigateSelectFavoriteApp = navController::navigateSelectFavoriteApp,
            navigateHome = navController::navigateHome,
        )

        homeGraph(
            navigateSettings = navController::navigateSettings,
        )

        settingsGraph(
            navigateBack = navController::popBackStack,
            navigateClockSettings = navController::navigateClockSettings,
            // 他のナビゲーション関数
        )
    }
}
```

#### ナビゲーション規約
- **ルート定義**: `:core:ui`モジュールの`NavigationRoute.kt`に集約
- **拡張関数**: 各featureモジュールの`navigation`パッケージに配置
- **関数命名**: `navigate[ScreenName]`形式（例：`navigateSettings`）
- **戻る処理**: `navController::popBackStack`を使用
- **グラフ関数**: `[graphName]Graph`形式（例：`settingsGraph`）
- **アニメーション**: NavHostレベルで統一的に定義

### 6. 依存性注入（Hilt）

#### Module配置
```
:core:data/di/ - データ層のDIモジュール
:core:domain/di/ - ドメイン層のDIモジュール
各feature/di/ - 機能固有のDIモジュール (必要に応じて)
```

#### アノテーション使用
```kotlin
@HiltViewModel // ViewModel
@Inject // コンストラクタ注入
@Module // DIモジュール
@Provides // プロバイダー関数
@Binds // インターフェース実装バインド
```

## レビューポイント

### 必須チェック項目

#### 1. アーキテクチャ準拠
- [ ] Clean Architectureの層分離が適切か
- [ ] MVIパターンが正しく実装されているか（StatefulまたはStateless BaseViewModelの使い分け）
- [ ] ViewModelStateとStateの分離が適切か（StatefulBaseViewModelの場合）
- [ ] 依存関係の方向が正しいか（上位層が下位層に依存）

#### 2. Compose実装
- [ ] Composableが適切にstatelessか
- [ ] `remember`の使用が適切か
- [ ] Previewが正しく実装されているか（Light/Dark両方）
- [ ] ``が設定されているか

#### 3. 命名規則
- [ ] ファイル名がパターンに従っているか
- [ ] クラス名、関数名が意図を表しているか
- [ ] パッケージ構造が適切か

#### 4. パフォーマンス
- [ ] 不要な再コンポーズが発生していないか
- [ ] LazyListの使用が適切か
- [ ] 重い処理がメインスレッドで実行されていないか

#### 5. コード品質
- [ ] Kotlinの慣用的な書き方になっているか
- [ ] null安全性が保たれているか
- [ ] 適切な例外処理が実装されているか

### 推奨事項

#### 1. ドキュメント
```kotlin
/**
 * ユーザー設定を取得するUseCase
 * 
 * @return ユーザー設定のFlow
 */
class GetUserSettingsUseCase
```

#### 2. テスタビリティ
- ViewModelのテストが書きやすい構造か
- DIが適切に設定されているか

#### 3. ユーザビリティ
- アクセシビリティが考慮されているか
- エラーハンドリングが適切か

## コードレビューコメント例文（日本語）

### 良い例
```
✅ Previewの実装が適切です。Light/Dark両方のパターンが実装されており、プロジェクトの規約に従っています。

✅ MVIパターンが正しく実装されています。ViewModelState、State、Action、Effectが適切に分離されており、StatefulBaseViewModelを継承してビジネスロジックが管理されています。

✅ StatelessBaseViewModelの使用が適切です。この画面は状態管理が不要なため、ActionとEffectのみで実装されています。
```

### 改善提案
```
💡 この部分はdesignsystemモジュールの既存コンポーネントを使用することを検討してください。重複実装を避けることができます。

💡 このComposableは複雑になっているようです。より小さなコンポーネントに分割することを検討してみてください。

⚠️ PreviewにDarkテーマのバリエーションが不足しています。`ComponentNameDarkPreview`を追加してください。
```

### 必須修正
```
❌ このファイルの命名がプロジェクトの規約に従っていません。`[FeatureName]Screen.kt`パターンに修正してください。

❌ ``アノテーションが不足しています。Preview関数に追加してください。

❌ Clean Architectureの層違反が発生しています。ドメイン層からデータ層の具象クラスを直接参照しないでください。

❌ ViewModelStateとStateの分離が不適切です。UI描画に不要な情報（initialDataなど）がStateに含まれています。ViewModelState内に保持してtoState()で変換してください。

❌ 状態管理が必要な画面でStatelessBaseViewModelを使用しています。StatefulBaseViewModelに変更し、適切な状態管理を実装してください。
```

## 除外事項
以下のファイルはレビュー対象外とする：
- `build.gradle.kts`（ビルド設定）
- `gradle.properties`（Gradle設定）
- `proguard-rules.pro`（難読化設定）
- `AndroidManifest.xml`（基本的な設定のみの場合）
- 自動生成ファイル（`build/`配下など）

---

このStyleGuideに基づいて、一貫性のある品質の高いコードレビューを実施してください。