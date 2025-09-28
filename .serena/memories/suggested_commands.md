# 推奨コマンド一覧

## タスク完了時に実行すべきコマンド

### 静的解析（必須）
```bash
./gradlew detekt
```
- Detektによるコード品質チェック
- Compose固有ルールも含む
- タスク完了後に必ず実行

### ユニットテスト
```bash
# 全てのユニットテストを実行
./gradlew test

# UseCaseテストのみ実行
./gradlew :core:domain:test

# Repositoryテストのみ実行
./gradlew :core:data:test

# テストレポート生成（エラーがあっても続行）
./gradlew test --continue
# レポートは各モジュールの build/reports/tests/test/index.html に生成される
```

### ビルド
```bash
# デバッグビルド
./gradlew assembleDebug

# リリースビルド
./gradlew assembleRelease

# 全モジュールのビルド
./gradlew build
```

## CI/CDで実行されるコマンド
GitHub ActionsのCIパイプラインでは以下が自動実行されます：
1. `./gradlew :core:domain:test` - UseCaseテスト
2. `./gradlew :core:data:test` - Repositoryテスト
3. `./gradlew detekt` - 静的解析

## Git・GitHub運用コマンド

### ブランチ作成
```bash
git checkout main
git pull origin main
git checkout -b feature/#<issue-number>
```

### コミット
```bash
git add .
git commit -m "<type>: <description>"
```

### プルリクエスト作成
```bash
git push origin feature/#<issue-number>
# その後GitHub UIでPR作成
```

## 便利なGradleコマンド

### 依存関係の確認
```bash
./gradlew :app:dependencies
```

### キャッシュクリア
```bash
./gradlew clean
```

### 特定モジュールのビルド
```bash
./gradlew :core:data:build
./gradlew :feature:home:build
```

## macOS（Darwin）特有のシステムコマンド
このプロジェクトはDarwin（macOS）で開発されています：
- `ls` - ファイル一覧表示
- `cd` - ディレクトリ移動
- `grep` または `rg`（ripgrep） - テキスト検索
- `find` - ファイル検索
- `git` - バージョン管理
- `chmod +x gradlew` - gradlewに実行権限付与

## 注意事項
- **タスク完了時は必ず`./gradlew detekt`を実行**
- テストは主要なロジック（Repository、UseCase）を中心にカバー
- detektエラーは修正してからコミット