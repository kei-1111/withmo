# Git/GitHub運用規則

withmoプロジェクトにおけるGit/GitHub運用の標準化された規則を定義します。

## コミットメッセージ規則

### 基本形式
```
<type>: <description>
```

### Type一覧
- `feat`: 新機能の追加
- `fix`: バグ修正
- `refactor`: リファクタリング（機能変更なし）
- `docs`: ドキュメント関連の変更
- `other`: その他の変更（バージョン変更など）

### 例
```bash
feat: アプリをホーム画面上に追加でき、保存されるように変更
fix: AppUtils.getAppIconでクラッシュが発生するバグの修正
refactor: detekt警告箇所の修正
docs: CLAUDE.mdを作成し、Claude Codeがより効果的にコード生成できるように設定
other: リリースビルドのためにバージョンを変更
```

### ルール
- **言語**: 日本語で記述する
- **動詞**: 過去形で終える（「〜した」「〜するように変更」）
- **内容**: 具体的で分かりやすい説明を心がける
- **長さ**: 50文字程度を目安とする
- **詳細**: 必要に応じて本文で詳細を説明

## ブランチ命名規則

### 基本形式
```
<type>/#<issue-number>
```

### Type一覧
- `feature/`: 新機能開発
- `fix/`: バグ修正
- `refactor/`: リファクタリング
- `docs/`: ドキュメント作成・更新
- `research/`: 調査・研究目的

### 例
```bash
feature/#225
fix/#213
refactor/#218
docs/#221
research/#189
```

### 特例
- 初期開発時: `feature/issue#<number>` 形式も使用可能
- 緊急修正時: `hotfix/#<issue-number>` 形式も許可

## Issue タイトル規則

### 基本形式
```
[<Type>]: <タイトル>
```

### Type一覧
- `Feature`: 新機能要求
- `Bug`: バグ報告
- `Refactor`: リファクタリング要求
- `Docs`/`Documentation`: ドキュメント作成要求
- `Research`: 調査・研究タスク

### 例
```
[Feature]: アプリをホーム画面に追加する機能の作成
[Bug]: AppUtils.getAppIconでクラッシュが発生し、アプリが起動できないバグの修正
[Refactor]: ComposableにPreviewをつける
[Docs]: Gemini Code Assist用のスタイルガイドを作成
[Research]: アプリを長時間バックグラウンドに置くと落ちてしまう問題の調査
```

### ルール
- **明確性**: 何をするのかが一目で分かるようにする
- **具体性**: 抽象的な表現を避け、具体的に記述する
- **責任範囲**: 1つのIssueでは1つの責任に集中する

## Pull Request タイトル規則

### 基本形式
```
[<Type>]: <タイトル>
```

### ルール
- **Issue連動**: 対応するIssueと同じタイトルを使用する
- **一貫性**: ブランチ名と連動させる（`<type>/#<issue-number>`）
- **明確性**: PRで何が変更されるかを明確に示す

### 例
```
[Feature]: WithmoDialogを作成し、DialogUIの変更
[Bug]: 同時タップすると画面遷移がおかしくなる
[Refactor]: ComposableにPreviewをつける
[Documentation]: アーキテクチャについて説明するドキュメントの作成
```

## 運用フロー

### 1. Issue作成
- 機能要求・バグ報告・タスクをIssueとして登録
- 適切なラベル（Priority, Status, Type）を付与
- 必要に応じてAssigneeを設定

### 2. ブランチ作成
```bash
# main/developブランチから分岐
git checkout main
git pull origin main
git checkout -b feature/#225
```

### 3. 開発作業
- 該当ブランチで開発を実施
- 定期的にcommitして進捗を記録
- 必要に応じてrebaseでコミット履歴を整理

### 4. コミット
```bash
# ステージング
git add .

# コミット（規定の形式でメッセージを記述）
git commit -m "feat: アプリをホーム画面上に追加でき、保存されるように変更"
```

### 5. PR作成
- Issueと同じタイトルでPull Requestを作成
- 変更内容の概要を説明
- 関連するIssueをリンク
- `.github/PULL_REQUEST_TEMPLATE.md`のテンプレートに従って記述

### 6. レビュー・マージ
- コードレビューを実施
- CI/CDチェックの通過を確認
- 承認後、メインブランチにマージ
- 不要になったブランチを削除

## ベストプラクティス

### コミット
- **原子性**: 1つのコミットでは1つの論理的変更のみ行う
- **頻度**: 小さな変更でも定期的にコミットする
- **メッセージ**: 将来の自分や他の開発者が理解できるように記述

### ブランチ
- **寿命**: ブランチは短命に保つ（数日〜1週間程度）
- **同期**: 定期的にmainブランチと同期する
- **命名**: 一意で分かりやすい名前を付ける

### Pull Request
- **サイズ**: レビューしやすいサイズに保つ（500行程度まで）
- **説明**: 変更理由と影響範囲を明確に記述
- **テスト**: 適切なテストを含める

### Issue
- **粒度**: 適切な粒度で分割する（大きすぎず小さすぎず）
- **更新**: 進捗状況を定期的に更新する
- **クローズ**: 完了時は必ずクローズする

## 禁止事項

- **直接push**: mainブランチへの直接pushは禁止
- **Force push**: 共有ブランチでのforce pushは禁止
- **大量変更**: 1つのPRでの大量ファイル変更は避ける
- **意味不明**: 意味不明なコミットメッセージは禁止

## 推奨ツール

- **Git GUI**: SourceTree, GitKraken, VS Code Git拡張
- **コミット**: Conventional Commits拡張の使用
- **レビュー**: GitHub Pull Requestのレビュー機能を活用