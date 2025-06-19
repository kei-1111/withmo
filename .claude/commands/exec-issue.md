# Issue自動実行コマンド

`gh issue view $ARGUMENTS` でGitHubのIssueの内容を確認し、タスクの遂行を行なってください。

## 実行手順

### 1. Issue内容の理解
- Issueに記載されている内容を理解する
- タスクの種類（Feature/Bug/Refactor/Docs/Research）を特定する

### 2. リポジトリの最新化
```bash
git checkout main
git pull origin main
```

### 3. ブランチ作成
**docs/GIT_GITHUB_CONVENTIONS.mdのブランチ命名規則に従って作成**
- **形式**: `<type>/#<issue-number>`
- **Type一覧**:
  - `feature/`: 新機能開発
  - `fix/`: バグ修正  
  - `refactor/`: リファクタリング
  - `docs/`: ドキュメント作成・更新
  - `research/`: 調査・研究目的

例：`feature/#225`, `fix/#213`, `docs/#221`

### 4. タスク実行
- Issueの内容を実現するために必要なタスクを実行する
- CLAUDE.mdの技術仕様とアーキテクチャに従って実装する

### 5. テスト追加
- 適切なテストを追加する（必要に応じて）

### 6. 品質確認
- テストとLintを実行し、すべてのテストが通ることを確認する
```bash
./gradlew detekt
```

### 7. コミット作成
**docs/GIT_GITHUB_CONVENTIONS.mdのコミットメッセージ規則に従って作成**
- **形式**: `<type>: <description>`
- **Type一覧**: `feat`, `fix`, `refactor`, `docs`, `other`
- **言語**: 日本語
- **動詞**: 過去形で終える
- **長さ**: 50文字程度を目安

例：
```bash
feat: アプリをホーム画面上に追加でき、保存されるように変更
fix: AppUtils.getAppIconでクラッシュが発生するバグの修正
refactor: detekt警告箇所の修正
```

### 8. PR作成
- Issueと同じタイトルでPull Requestを作成
- **Pull Request作成の際は、.github/PULL_REQUEST_TEMPLATE.mdに従って作成**

## 参考資料
- `docs/GIT_GITHUB_CONVENTIONS.md`: Git/GitHub運用規則
- `CLAUDE.md`: プロジェクト技術仕様
- `docs/GIT_GITHUB_CONVENTIONS.md`: Pull Requestテンプレート
