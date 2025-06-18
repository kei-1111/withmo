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
- **アーキテクチャパターン**: Clean Architecture + MVVM
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
- **State**: `[機能名]State.kt` (例: `HomeState.kt`)
- **Action**: `[機能名]Action.kt` (例: `HomeAction.kt`)
- **Effect**: `[機能名]Effect.kt` (例: `HomeEffect.kt`)
- **Component**: `[ComponentName].kt` (例: `AppItem.kt`)
- **UseCase**: `[動詞][名詞]UseCase.kt` (例: `GetUserSettingsUseCase.kt`)
- **Repository**: `[名詞]Repository.kt` + `[名詞]RepositoryImpl.kt`

#### パッケージ構造
```
feature/[featurename]/
├── [機能名]Screen.kt
├── [機能名]ViewModel.kt
├── [機能名]State.kt
├── [機能名]Action.kt
├── [機能名]Effect.kt
├── [機能名]ScreenDimensions.kt (必要に応じて)
└── component/
    ├── [ComponentName].kt
    └── contents/ (サブコンポーネント)
```

#### MVIアーキテクチャの命名規則（docs/FEATURE_ARCHITECTURE.mdより）

**Action**: `On + 対象 + 過去形`
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
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ComponentNameLightPreview() {
    [ModuleName]LightPreviewEnvironment {
        ComponentName(
            requiredParam = "サンプルデータ",
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
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

#### PreviewEnvironment
各モジュールに専用のPreviewEnvironmentを配置：
- `:feature:home` → `HomeLightPreviewEnvironment` / `HomeDarkPreviewEnvironment`
- `:feature:setting` → `SettingLightPreviewEnvironment` / `SettingDarkPreviewEnvironment`
- `:core:designsystem` → `DesignSystemLightPreviewEnvironment` / `DesignSystemDarkPreviewEnvironment`

### 3. MVVM パターン

#### ViewModel
```kotlin
@HiltViewModel
class FeatureNameViewModel @Inject constructor(
    private val useCase: SomeUseCase,
) : BaseViewModel<FeatureNameState, FeatureNameAction, FeatureNameEffect>(
    initialState = FeatureNameState()
) {
    override fun onAction(action: FeatureNameAction) {
        when (action) {
            // アクション処理
        }
    }
}
```

#### State
```kotlin
data class FeatureNameState(
    val isLoading: Boolean = false,
    val data: SomeData? = null,
    val errorMessage: String? = null,
) : State
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

### 5. 依存性注入（Hilt）

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
- [ ] MVVMパターンが正しく実装されているか
- [ ] 依存関係の方向が正しいか（上位層が下位層に依存）

#### 2. Compose実装
- [ ] Composableが適切にstatelessか
- [ ] `remember`の使用が適切か
- [ ] Previewが正しく実装されているか（Light/Dark両方）
- [ ] `@RequiresApi(Build.VERSION_CODES.O)`が設定されているか

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

✅ MVVMパターンが正しく実装されています。StateとActionが適切に分離されており、ViewModelでビジネスロジックが管理されています。
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

❌ `@RequiresApi(Build.VERSION_CODES.O)`アノテーションが不足しています。Preview関数に追加してください。

❌ Clean Architectureの層違反が発生しています。ドメイン層からデータ層の具象クラスを直接参照しないでください。
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