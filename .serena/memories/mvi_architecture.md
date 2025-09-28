# MVI アーキテクチャ実装ガイド

## MVIパターンの概要
このプロジェクトでは、全フィーチャーモジュールで統一されたMVI（Model-View-Intent）パターンを採用しています。

## 主要コンポーネント
- **Action**: ユーザー操作をViewModelに伝達するためのアクション
- **State**: UIに公開される画面描画用の状態
- **ViewModelState**: ViewModelの内部状態（StatefulBaseViewModelのみ）
- **Effect**: ナビゲーションやトーストなど、一度だけ実行される副作用

## BaseViewModel（2種類）

### 1. StatefulBaseViewModel - 状態管理を行う画面用
```kotlin
abstract class StatefulBaseViewModel<VS : ViewModelState<S>, S : State, A : Action, E : Effect> : ViewModel()
```
- ViewModelStateを内部状態として保持
- `toState()`メソッドでViewModelStateをStateに変換
- `updateViewModelState { copy(...) }`で状態を更新
- 主に設定画面や複雑なUIロジックを持つ画面で使用

### 2. StatelessBaseViewModel - 状態管理が不要な画面用
```kotlin
abstract class StatelessBaseViewModel<A : Action, E : Effect> : ViewModel()
```
- Stateを持たず、ActionとEffectのみ使用
- ナビゲーション中心の単純な画面で使用
- 例：ウェルカム画面、シンプルな選択画面

## ViewModelStateとStateの分離
- **ViewModelState**: ViewModel内部の実装詳細を含む状態（初期値との比較用データなど）
- **State**: UIに公開される状態（UI描画に必要な情報のみ）
- ViewModelStateは`toState()`でStateに変換される
- StatefulBaseViewModelのみで使用

## 典型的なStateパターン（StatefulBaseViewModel）
```kotlin
sealed interface XxxState : State {
    data object Idle : XxxState
    data object Loading : XxxState
    data class Stable(val data: Data, val isEnabled: Boolean) : XxxState
    data class Error(val error: Throwable) : XxxState
}
```

## 画面構成（3層構造）

### 1. public Screen - ViewModelを受け取る層
- `@Composable fun XxxScreen(viewModel: XxxViewModel = hiltViewModel(), ...)`
- stateとeffectを収集
- LaunchedEffectでエフェクトを監視
- **Stateful**: stateとonActionを渡す
- **Stateless**: onActionのみを渡す

### 2. private Screen - UI構造を定義する層
- `@Composable private fun XxxScreen(state: XxxState, onAction: (XxxAction) -> Unit, ...)`（Stateful）
- `@Composable private fun XxxScreen(onAction: (XxxAction) -> Unit, ...)`（Stateless）
- **Stateful**: when文でstate分岐（Idle/Loading/Stable/Error）
- **Stateless**: 状態分岐なし、直接UI定義
- TopAppBar、Content、Buttonなどの構造定義

### 3. Content - 純粋なUI描画層
- `@Composable fun XxxScreenContent(state: XxxState.Stable, onAction: (XxxAction) -> Unit, ...)`（Stateful）
- `@Composable fun XxxScreenContent(...)`（Stateless）
- 純粋なUI描画のみ
- ViewModelに直接依存しない

## 命名規則
- **Action**: `On` + 対象 + 過去形（例：`OnSaveButtonClick`、`OnBackButtonClick`）
- **Effect**: 命令形動詞 + 対象（例：`NavigateBack`、`ShowToast`、`OpenDocument`）
- **State**: sealed interfaceで状態表現（例：`Idle`、`Loading`、`Stable`、`Error`）
- **ViewModelState**: data classでViewModel内部状態保持

## データフロー
1. ユーザーがボタンを押す
2. `onAction(OnButtonClick)` が実行される
3. ViewModelが処理を実行
4. `updateViewModelState { ... }`で状態更新（Stateful）または`sendEffect(...)`（両方）
5. UIが状態を反映またはエフェクトを実行

## 新機能実装手順
1. 既存のMVIパターンを確認（StatefulまたはStateless）
2. 必要なモジュール構成を理解（core/feature分離）
3. ViewModelState、State、Action、Effectを定義
4. ViewModel実装（適切なBaseViewModel継承）
5. 3層Screen構造で画面実装
6. 既存コンポーネントやユーティリティを活用