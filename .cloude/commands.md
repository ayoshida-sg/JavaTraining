# 開発コマンド

## アプリケーションの実行

### Web アプリケーション (メインモジュール)

```bash
# Mavenラッパーの使用 (推奨)
./mvnw spring-boot:run -pl webapp

# Windows
mvnw spring-boot:run -pl webapp
```

### バッチアプリケーション

```bash
# Mavenラッパーの使用
./mvnw spring-boot:run -pl batch

# Windows
mvnw spring-boot:run -pl batch
```

## プロジェクトのビルド

```bash
# 全モジュールをビルド
./mvnw package

# 特定モジュールのビルド
./mvnw package -pl webapp
./mvnw package -pl batch
```

## テストの実行

```bash
# 全テストを実行
./mvnw test

# 特定モジュールのテストを実行
./mvnw test -pl webapp
./mvnw test -pl batch
```

## ビルド済みアプリケーションの実行

ビルド後、アプリケーションは直接実行できます：

```bash
# Webアプリケーション (webapp-0.0.1-SNAPSHOT.warを生成)
java -jar webapp/target/webapp-0.0.1-SNAPSHOT.war

# バッチアプリケーション (batch-0.0.1-SNAPSHOT.jarを生成)
java -jar batch/target/batch-0.0.1-SNAPSHOT.jar
```
