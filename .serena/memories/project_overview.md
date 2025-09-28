# withmo プロジェクト概要

## プロジェクト説明
**withmo**は、「デジタルフィギュア × ランチャー」のコンセプトで作られた日本語Androidランチャーアプリです。Unity as a Libraryを活用してホーム画面に3Dモデル（.vrm形式）を表示することができ、インタラクティブなランチャーとライブ壁紙の両方の役割を果たします。

## SDK要件
- **minSdk**: 29 (Android 10.0)
- **compileSdk**: 36 (Android 16)
- **targetSdk**: 36 (Android 16)
- **Kotlin**: 2.2.0
- **AGP**: 8.11.0
- **JDK**: 17

## 技術スタック
- **言語**: Kotlin (official code style)
- **UIフレームワーク**: Jetpack Compose + Material 3
- **アーキテクチャ**: Clean Architecture + MVI (Model-View-Intent)
- **DI**: Hilt (Dagger)
- **データベース**: Room
- **設定管理**: DataStore (Preferences)
- **Unity統合**: Unity as a Library
- **静的解析**: Detekt (Compose固有ルール含む)
- **CI/CD**: GitHub Actions
- **テストライブラリ**: JUnit, MockK, Turbine, Kotlinx Coroutines Test

## モジュール構成

### Core Modules
- `core:common` - Unity統合、定数、ディスパッチャーなどの共通機能
- `core:data` - Roomデータベース、DataStoreリポジトリ、各種マネージャー
- `core:domain` - ユースケース、リポジトリインターフェース、ビジネスロジック
- `core:designsystem` - Composeコンポーネント、テーマ、Material 3デザインシステム
- `core:featurebase` - MVI基底クラス（State、Action、Effect、BaseViewModel）
- `core:model` - ドメインモデルとユーザー設定データクラス
- `core:service` - バックグラウンドサービス（UnityWallpaperService、NotificationListener）
- `core:ui` - 共有UIユーティリティ、プロバイダー、モディファイア
- `core:util` - 汎用ユーティリティ、拡張関数

### Feature Modules
- `feature:home` - 3Dモデル表示とウィジェット管理を行うメインランチャー画面
- `feature:onboarding` - モデル選択とアプリ設定の初期セットアップ画面
- `feature:setting` - 各種設定画面（アプリアイコン、時計、テーマ、お気に入りなど）

## Unity as a Library統合の特徴
- デュアルサーフェスレンダリング（ActivityとWallpaper）
- UnityManagerによるライフサイクル管理
- AndroidToUnityMessenger / UnityToAndroidMessengerによる双方向通信
- VRMモデルファイル（.vrm形式）のサポート
- 時間帯に応じた視覚変化（昼・夕方・夜）

## 重要な制約事項
- **VRMファイルのみ対応**: 3Dモデル形式はVRMのみ
- **NDK ABIs**: armeabi-v7aとarm64-v8aのみ対応