# 研修: Java 実習用サービス加入者管理システム

このプロジェクトは、Java 実習用で使用するサービス加入者システムです。

本アプリケーションは Spring Framework 6(Spring Boot 3) を使用して作成されており、以下のパッケージを利用しています。

- Spring Web
  - Thymeleaf
- Spring Validation
- Spring Security
- Spring JDBC
  - H2 Database 
  - PosgreSQL Driver
- Lombok

**本リポジトリを自分Githubアカウントにコピー(右上の「Use this template」→「Create a new repository」)して利用してください**

## 必要なソフトウエア

本プロジェクトの開発をするには以下のソフトウエアが必要です。
公式サイトからダウンロードしてインストールしてください。

- Java Development Kit (JDK) 17 以降
  - [Oracle Java SE](https://www.oracle.com/jp/java/technologies/java-se-glance.html)
  - [Amazon Corrette](https://aws.amazon.com/jp/corretto)
  - [Eclipse Temurin](https://adoptium.net/temurin/releases/)
- IDEもしくはテキストエディタ
  - [Eclopse](https://www.eclipse.org/downloads/)
    - [Pleiades](https://willbrains.jp/) - Pleiades All in One - Eclipse日本語化プラグイン＋α
  - [Visual Studio Code](https://azure.microsoft.com/ja-jp/products/visual-studio-code)
- [Git for Windows](https://gitforwindows.org/)

## あると良いソフトウエア

- コンテナ化プラットフォーム ※コンテナ(DevContainer)環境で開発する場合に必要になります。
  - [Docker Desktop][https://www.docker.com/ja-jp/products/docker-desktop/]
  - [Docker Engine](https://docs.docker.com/engine/install/)


## 開発方法

本プロジェクトは、 EclipseもしくはVisual Studio Code(以下、VSCode) で開発することを想定しています。
なお、開発ドキュメントは所定の場所に格納しています。事前に講師が説明をしますので、その場所のドキュメントを参照してください。

### Eclipse

本プロジェクトを適当な場所にクローンした後、Eclipseを起動して、インポート機能の「既存Mavenプロジェクト」で本プロジェクトをワークスペースにインポートします。

### VSCode

VSCode を起動し、フォークしたリポジトリを適当な場所にクローンしてください。その後、クローンしたフォルダを開きます。
初回クローンしたフォルダを VSCode で開くと、本プロジェクトが利用している拡張機能のインストールを求められますので、インストールしてください。


## 実行およびデバッグ方法

### Eclipseを使用している場合

Eclipseでインポートしたプロジェクトで、Alt+Shift+Xを同時押しした後、bキーを押します。
本システムが起動したら、Webブラウザで以下のURLにアクセスします。

``` URL
http://localhost:8080
```

### VSCodeを使用している場合

クローンしたフォルダを VSCode で開いた状態で、F5 キーを押します。
本システムが起動したら、Webブラウザで以下のURLにアクセスします。

``` URL
http://localhost:8080
```


## データベースへのアクセス

開発環境モードで動作中は、**H2 Database**で動作しています。本システムデバッグ中に、Webブラウザから以下のURLにアクセスすると、データベースを操作する画面が表示されます。

``` URL
http://localhost:8080/h2-console
```


### コマンドライン

以下のコマンドを実行します。

(Windows)
```
mvnw spring-boot:run -pl webapp
```

(Linux)
```
./mvnw spring-boot:run -pl webapp
```

## ビルド方法

ターミナルから以下のコマンドを実行します。

```sh
./mvnw package
```

エラーがない場合は、`webapp/targe` フォルダには`webapp-0.0.1-SNAPSHOT.war` が、
`batch/target` フォルダには、`batch-0.0.1-SNAPSHOT.jar` が作成されます。

それぞれ、`java -jar <生成されたファイル>` とすることで実行できます。
