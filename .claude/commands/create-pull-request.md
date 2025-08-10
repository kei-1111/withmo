---
allowed-tools: Bash(git:*), Bash(gh:*), Read(*)
description: "コミット済みの変更を確認してプルリクエストを作成する"
---

# プルリクエスト作成支援コマンド

## タスク概要
このコマンドは、現在のブランチの変更内容を確認し、GitHub CLI (gh) を使用してプルリクエストを作成します。

## 実行手順

1. **現在の状態確認**
   - git statusで変更を確認
   - git logで最近のコミットを確認
   - 現在のブランチとベースブランチを確認

2. **変更内容の分析**
   - 全てのコミットメッセージを確認
   - 変更されたファイルを分析
   - 変更の目的と影響範囲を把握

3. **PRタイトルとボディの作成**
   - ブランチ名からIssue番号を取得 (format: type/#issue-number)
   - gh issue view でIssue情報を取得し、Issueタイトルと同じPRタイトルを使用
   - プロジェクトのPRテンプレート (.github/PULL_REQUEST_TEMPLATE.md) に基づいてボディを作成
   - 変更内容のサマリーを作成
   - テストプランを記載

4. **プルリクエストの作成**
   - リモートブランチへのpush (必要に応じて)
   - gh pr create コマンドでPRを作成
   - 作成されたPRのURLを表示

## 注意事項

- コミットされていない変更がある場合は、先にコミットするよう促してください
- PRタイトルは関連するIssueのタイトルと同じにしてください
- ブランチ名は type/#issue-number 形式になっているため、そこからIssue番号を取得します
- gh issue view コマンドでIssue情報を取得し、そのタイトルをPRタイトルに使用します
- 日本語でタイトルとボディを作成してください

## 引数の使用

引数が提供された場合 ($ARGUMENTS):
- 引数をPRボディの参考情報として使用
- 例: /create-pull-request "ユーザー設定の保存機能を追加"

引数がない場合:
- コミットメッセージから変更内容を分析してボディを生成

※ PRタイトルは常にIssueタイトルを使用します

## プロジェクト固有のルール

1. このプロジェクトのGit/GitHub運用規則に従う (docs/GIT_GITHUB_CONVENTIONS.md参照)
2. ベースブランチは通常 main を使用
3. PRボディには必ず以下を含める:
   - 変更内容のサマリー
   - 関連Issue番号 (あれば)
   - テストプラン (テストの実行結果など)
   - スクリーンショット (UI変更の場合)

## 実行フロー

以下の順序で処理を実行してください:

1. まず git status で未コミットの変更がないか確認
2. git log --oneline -n 10 で最近のコミットを確認
3. git branch --show-current で現在のブランチ名を取得
4. ブランチ名からIssue番号を抽出 (例: feature/#123 から 123 を取得)
5. gh issue view [issue-number] でIssue情報を取得し、タイトルを確認
6. git diff main...HEAD で変更内容を確認
7. PRテンプレートを読み込んで形式を確認
8. 必要に応じて git push -u origin [branch-name] でリモートにpush
9. gh pr create --title "[Issueのタイトル]" でPRを作成
10. 作成されたPRのURLを表示

作成が完了したら、PRのURLとともに成功メッセージを表示してください。