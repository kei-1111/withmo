# コードスタイル・規約

## 基本スタイル
- **Kotlinコードスタイル**: `kotlin.code.style=official` を使用
- **コメント**: コメントは基本的に追加しない（明示的な指示がない限り）
- **KDoc**: パブリックAPIには適切なKDocを記述、`@sample`タグでプレビュー関数を示す

## 命名規則

### 定数・列挙型
- **const val / enum**: UPPER_SNAKE_CASE
- 例：`STOP_TIMEOUT_MILLIS`、`MAX_SCALE`、`DEFAULT_VALUE`

### クラス・インターフェース
- **クラス・インターフェース**: PascalCase
- 例：`HomeViewModel`、`GetUserSettingsUseCase`、`AppRepository`

### 関数・変数
- **関数・変数**: camelCase
- 例：`onAction()`、`updateState()`、`isEnabled`、`userName`

### Composable関数
- **Composable関数**: PascalCase（Composeの慣習）
- 例：`HomeScreen()`、`ClockSettingsContent()`、`WithmoButton()`

## Suppress警告の使用
必要に応じて以下のSuppress警告を使用:
- `@Suppress("TooManyFunctions")` - ViewModelなどで関数が多い場合
- `@Suppress("ModifierMissing")` - public Screen関数でModifierが不要な場合
- `@Suppress("VariableNaming")` - MVIパターンでの`_state`、`_effect`などの命名時

## Compose規約
- 全てのComposable関数には`@Preview`を追加
- Material3 デザインシステムに準拠
- Modifierパラメータは最後に配置し、デフォルト値は`Modifier`
- 状態管理は`remember`や`rememberSaveable`を適切に使用

## データ層規約

### Repository
- インターフェースを`core:domain`に配置
- 実装を`core:data`に配置
- 戻り値は`Result<T>`型または`Flow<Result<T>>`型でラップ

### UseCase
- 単一責任の原則に従う
- `operator fun invoke()`を使用
- 戻り値は`Result<T>`型または`Flow<Result<T>>`型でラップ
- ファイル名は`<動詞><名詞>UseCase.kt`（例：`GetUserSettingsUseCase.kt`）

### Flow使用
- データ層全体でFlowベースのリアクティブプログラミング
- 一度だけ実行する処理は`suspend fun`
- 監視が必要な処理は`Flow`を返す

## デザインシステム
- 統一されたスペーシング管理用のPaddingsオブジェクト
  - Tiny=2dp、Small=5dp、Medium=10dp、Large=15dp、ExtraLarge=25dp
- カスタムComposeコンポーネントはKDocドキュメンテーション付き
- 時間による背景変化機能を持つMaterial 3テーマ
- 包括的なローカライゼーション対応の日本語ファーストUI

## 重要な原則
- **不要なインラインコメントは避ける**: 基本的に自明なコメントを追加しない（明示的な指示がない限り）
- **既存パターンの踏襲**: 新機能追加時は既存のMVIパターンに従って実装する
- **ライブラリの確認**: 使用前に既存コードベースで使用されているか確認する
- **Unity操作**: 適切なライフサイクル管理を行う
- **セキュリティ**: シークレット・キーの露出禁止、コミット禁止