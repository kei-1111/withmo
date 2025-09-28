# Git・GitHub運用フロー

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

## Issue・PRタイトル規則

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
```

## 開発フロー

### 1. Issue作成
- 機能要求・バグ報告・タスクをIssueとして登録
- 適切なラベル（Priority, Status, Type）を付与
- 必要に応じてAssigneeを設定

### 2. ブランチ作成
```bash
git checkout main
git pull origin main
git checkout -b feature/#225
```

### 3. 開発・コミット
```bash
git add .
git commit -m "feat: アプリをホーム画面上に追加でき、保存されるように変更"
```

### 4. PR作成
- Issueと同じタイトルでPull Requestを作成
- `.github/PULL_REQUEST_TEMPLATE.md`のテンプレートに従って記述
- 関連するIssueをリンク

### 5. レビュー・マージ
- コードレビューを実施
- CI/CDチェックの通過を確認（detekt、test）
- 承認後、メインブランチにマージ
- 不要になったブランチを削除

## ベストプラクティス
- **原子性**: 1つのコミットでは1つの論理的変更のみ
- **頻度**: 小さな変更でも定期的にコミット
- **ブランチ寿命**: 短命に保つ（数日〜1週間程度）
- **PRサイズ**: レビューしやすいサイズに保つ（500行程度まで）

## 禁止事項
- **直接push**: mainブランチへの直接pushは禁止
- **Force push**: 共有ブランチでのforce pushは禁止
- **大量変更**: 1つのPRでの大量ファイル変更は避ける
- **意味不明**: 意味不明なコミットメッセージは禁止

詳細は`docs/GIT_GITHUB_CONVENTIONS.md`を参照してください。