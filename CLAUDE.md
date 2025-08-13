# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.
やり取りはすべて日本語で。

## プロジェクト概要

Spring Boot 3 と Spring Framework 6 で構築されたサービス加入者管理システム（Java 実習用）です。以下の 2 つの主要モジュールで構成されています：

- **webapp**: サービス加入者管理用 Web アプリケーション (WAR パッケージング)
- **batch**: バッチ処理アプリケーション (JAR パッケージング)

本プロジェクトは、Spring Boot 3.5.3 と Java 17 を使用したマルチモジュール Maven 構成を採用しています。

## タスク実行の 4 段階フロー

### 1. 要件定義

- `.claude/workflow/complete.md`が存在すれば参照
- 目的の明確化、現状把握、成功基準の設定
- `.claude/workflow/requirements.md`に文書化
- **必須確認**: 「要件定義フェーズが完了しました。設計フェーズに進んでよろしいですか？」

### 2. 設計

- **必ず`.claude/workflow/requirements.md`を読み込んでから開始**
- アプローチ検討、実施手順決定、問題点の特定
- `.claude/workflow/design.md`に文書化
- **必須確認**: 「設計フェーズが完了しました。タスク化フェーズに進んでよろしいですか？」

### 3. タスク化

- **必ず`.claude/workflow/design.md`を読み込んでから開始**
- タスクを実行可能な単位に分解、優先順位設定
- `.claude/workflow/tasks.md`に文書化
- **必須確認**: 「タスク化フェーズが完了しました。実行フェーズに進んでよろしいですか？」

### 4. 実行

- **必ず`.claude/workflow/tasks.md`を読み込んでから開始**
- タスクを順次実行、進捗を`.claude/workflow/tasks.md`に更新
- 各タスク完了時に報告

## 実行ルール

### ファイル操作

- 新規タスク開始時: 既存ファイルの**内容を全て削除して白紙から書き直す**
- ファイル編集前に必ず現在の内容を確認

### フェーズ管理

- 各段階開始時: 「前段階の md ファイルを読み込みました」と報告
- 各段階の最後に、期待通りの結果になっているか確認
- 要件定義なしにいきなり実装を始めない

### 実行方針

- 段階的に進める: 一度に全てを変更せず、小さな変更を積み重ねる
- 複数のタスクを同時並行で進めない
- エラーは解決してから次へ進む
- エラーを無視して次のステップに進まない
- 指示にない機能を勝手に追加しない

## 実行コマンド

`.cloude/commands.md` を参照。

## アーキテクチャ概要

`.cloude/arch.md` を参照。

## コーディング標準

`.cloude/coding_standards.md` を参照。

## 依存関係・技術スタック

`.cloude/dependencies.md` を参照。

## ファイル構成メモ

- **設定ファイル**: `src/main/resources/`内のアプリケーションプロパティ
- **静的アセット**: `webapp/src/main/resources/static/`に配置
- **テンプレート**: `webapp/src/main/resources/templates/`内の Thymeleaf テンプレート
- **ドキュメント**: `extra/design/docs/`内の設計書・コーディングルール
- **テストデータ**: `extra/sql/testdata.sql`内のサンプル SQL データ
