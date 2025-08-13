# アーキテクチャ概要

## Web アプリケーション (`webapp` モジュール)

- **フレームワーク**: Spring Boot 3 + Spring Security, Spring Data JPA, Thymeleaf
- **データベース**: H2 (開発環境), PostgreSQL (本番環境)
- **セキュリティ**: Spring Security + カスタム SecurityConfig
- **テンプレートエンジン**: Thymeleaf + Spring Security 統合
- **パッケージング**: デプロイ用 WAR ファイル

**主要コンポーネント:**

- `WebappApplication.java`: JPA 監査機能付き Spring Boot メインアプリケーションクラス
- `SecurityConfig.java`: Spring Security の設定
- `MemberController.java`: 会員管理用 Web コントローラー
- `MemberService.java`/`MemberServiceImpl.java`: ビジネスロジック層
- `MemberRepository.java`: Spring Data JPA を使用したデータアクセス層
- `Member.java`: サービス加入者の JPA エンティティ

**テンプレート構成:**

- `layout/app.html`: メインアプリケーションレイアウト
- `member_*.html`: 会員管理ビュー (検索、編集、結果)
- `fragments/header.html`: 再利用可能なヘッダーコンポーネント

## バッチアプリケーション (`batch` モジュール)

- **フレームワーク**: Spring Boot + Spring Data JDBC
- **目的**: 研修用バッチ処理
- **パッケージング**: 実行可能 JAR

## データベース設定

- **開発環境**: H2 インメモリデータベース
- **本番環境**: PostgreSQL
- **スキーマ**: リソース内の`schema.sql`で管理
