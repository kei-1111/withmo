# CLAUDE.md

このファイルは Claude Code (claude.ai/code) がこのリポジトリでコード作業を行う際のガイドラインを示しています。

## プロジェクト概要

**withmo** は「デジタルフィギュア × ランチャー」のコンセプトで作られた日本語Androidランチャーアプリです。Unity as a Libraryを活用してホーム画面に3Dモデル（.vrm形式）を表示することができ、インタラクティブなランチャーとライブ壁紙の両方の役割を果たします。時間に応じた視覚変化や豊富なカスタマイズ機能が特徴です。

## SDK要件

- **minSdk**: 29 (Android 10.0)
- **compileSdk**: 35 (Android 15)
- **targetSdk**: 35 (Android 15)

## アーキテクチャ

### マルチモジュール Clean Architecture + MVI

このプロジェクトでは、全フィーチャーモジュールでMVI（Model-View-Intent）パターンによるClean Architectureを採用しています：

**Core Modules:**
- `core:common` - Unity統合、定数、ディスパッチャーなどの共通機能
- `core:data` - Roomデータベース、DataStoreリポジトリ、各種マネージャー
- `core:domain` - ユースケース、リポジトリインターフェース、ビジネスロジック
- `core:designsystem` - Composeコンポーネント、テーマ、Material 3デザインシステム
- `core:featurebase` - MVI基底クラス（State、Action、Effect、BaseViewModel）
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
- **State**: 画面描画に必要なデータの状態
- **Effect**: ナビゲーションやトーストなど、一度だけ実行される副作用

**BaseViewModel Architecture:**
```kotlin
abstract class BaseViewModel<S : State, A : Action, E : Effect> : ViewModel() {
    protected val _state = MutableStateFlow<S>(createInitialState())
    val state: StateFlow<S> = _state.asStateFlow()
    
    protected val _effect = Channel<E>(Channel.BUFFERED)
    val effect: Flow<E> = _effect.receiveAsFlow()
    
    abstract fun onAction(action: A)
    fun updateState(update: S.() -> S)
    fun sendEffect(effect: E)
}
```

**画面構成:**
- **メイン画面**: ViewModelを受け取り、エフェクトを監視してナビゲーションを処理
- **コンテンツ画面**: stateとonActionを受け取って純粋なUIを描画

**命名規則:**
- **Action**: `On` + 対象 + 過去形（例：`OnSaveButtonClick`、`OnNavigateSettingsButtonClick`）
- **Effect**: 命令形動詞 + 対象（例：`NavigateBack`、`ShowToast`、`OpenDocument`）
- **State**: 名詞形プロパティ（例：`clockSettings`、`isSaveButtonEnabled`）

**データフロー例:**
1. ユーザーが戻るボタンを押す
2. `onAction(OnBackButtonClick)` が実行される
3. ViewModelが `sendEffect(NavigateBack)` を呼び出す
4. 画面のLaunchedEffectが収集してナビゲーションを実行

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
- `withmo.android.feature` - MVI依存関係を含むフィーチャーモジュール設定
- `withmo.android.library.compose` - Composeライブラリ設定
- `withmo.detekt` - カスタムルールによるコード品質管理
- `withmo.hilt` - 依存性注入設定

### 共通コマンド

```bash
# 静的解析を実行
./gradlew detekt
```

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
- **Unity Libraryは含まれません**: unityLibraryはファイルサイズのためgitから除外されています
- **VRMファイルサポート**: 対応する3Dモデル形式はVRMのみです
- **NDK ABIs**: armeabi-v7aとarm64-v8aのみに対応しています

### デザインシステム
- 時間による背景変化機能を持つMaterial 3テーマ
- 包括的なローカライゼーション対応の日本語ファーストUI
- KDocドキュメンテーション付きのカスタムComposeコンポーネント
- 統一されたスペーシング管理用のPaddingsオブジェクト（Tiny=2dp〜ExtraLarge=25dp）

### 開発パターン
- プレビュー関数にはKDocで `@sample` アノテーションを使用する
- 新機能追加時は既存のMVIパターンに従って実装する
- Unity操作では適切なライフサイクル管理を行う
- データ層全体でFlowベースのリアクティブプログラミングを使用する

## テスト・品質管理
- **Detekt**: Compose固有ルールを含む包括的な静的解析
- **GitHub Actions**: コード品質チェックを含む自動CI/CD
- **Convention-based構造**: カスタムGradleプラグインによる統一的な構成
- **最小限のテストカバレッジ**: テスト追加時は重要なUnity統合ポイントを重視する

## Git・GitHub運用規則
詳細な運用規則は `docs/GIT_GITHUB_CONVENTIONS.md` を参照してください。

### 主要なルール
- **コミットメッセージ**: `<type>: <description>` 形式で日本語、過去形で記述
- **ブランチ命名**: `<type>/#<issue-number>` 形式（例：`feature/#225`）
- **Issue/PRタイトル**: `[<Type>]: <タイトル>` 形式で統一
- **開発フロー**: Issue作成 → ブランチ作成 → 開発 → PR作成 → レビュー → マージ
