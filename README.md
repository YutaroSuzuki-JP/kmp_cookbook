# Metropolitan Museum of Art KMP Explorer

メトロポリタン美術館のオープンアクセスAPI（Metropolitan Museum of Art API）を利用して、美術品の検索やお気に入り登録ができる Kotlin Multiplatform (KMP) のサンプルギャラリーアプリケーションです。

---

## 🌟 アプリの特徴
- **美しくダイナミックなデザイン**: ゴールドのアクセントをあしらった高級感あふれるダークテーマ、インタラクティブなホバー・タップエフェクト、詳細モーダルを搭載。
- **オフラインファースト設計**: 一度取得した美術品情報は Room にキャッシュされ、オフライン環境でも閲覧・検索およびお気に入りリストの閲覧が可能です。
- **プラットフォーム適応型 DI**: プラットフォーム毎に最適な Ktor ネットワークエンジン（Android: OkHttp / iOS: Darwin）を DI を介して自動注入します。
- **クリーンアーキテクチャ**: `/shared` モジュールは `presentation`・`domain`・`infra (data)` に階層化され、ビジネスロジックは `ViewModel` に寄せて UI から完全に分離されています。

---

## 🛠️ 技術スタック
- **UI フレームワーク**: Compose Multiplatform (Material 3)
- **依存性注入 (DI)**: Koin (ViewModel / UseCases / Repository / Database / Ktor Engine)
- **ネットワーク**: Ktor HttpClient
- **オフラインキャッシュ**: AndroidX Room (SQLite)
- **画像読み込み**: Coil 3 (Ktor 連携)
- **日付・時刻操作**: kotlinx-datetime & kotlin.time
- **テスト＆カバレッジ計測**: Kotlin Test, Ktor MockEngine, JetBrains Kover (リポジトリ層でブランチカバレッジ 100% を検証)

---

## 📁 ディレクトリ構成

### `/shared` モジュール
- `commonMain/kotlin/org/example/project/`
  - **`presentation/`**: UIとUIロジックの分離層
    - `MainScreen.kt`: ギャラリー検索とお気に入り画面のCompose UI
    - `MainViewModel.kt`: 検索実行やお気に入り切り替えのライフサイクル管理用 ViewModel
    - `MainUiState.kt`: 単一ソースとなる画面状態（StateFlow）の定義
  - **`domain/`**: ビジネスルール（プラットフォーム非依存）
    - `model/Artwork.kt`: 美術品ドメインエンティティ
    - `repository/ArtworkRepository.kt`: リポジトリのインターフェース
    - `usecase/`: 特定のビジネスユースケース (Search, GetFavorites, ToggleFavorite)
  - **`infra/`**: データの取得元（API/データベース）と実装層
    - `network/`: Ktorクライアントの実装、MetApiインターフェースおよびMetApiImpl
    - `database/`: Room エンティティ・Dao・AppDatabase定義
    - `repository/ArtworkRepositoryImpl.kt`: キャッシュ戦略とAPI呼び出しを仲介する実装クラス
    - `repository/ArtworkMapper.kt`: DTO ↔ ドメインモデル ↔ データベースエンティティ の相互変換マッパー
  - **`di/`**: アプリケーション全体のDI設定
    - `AppModule.kt`: presentation, domain, repository, network の共通Koin定義
    - `DatabaseModule.kt` (expect/actual): プラットフォームごとのRoomデータベース構築
    - `NetworkModule.kt` (expect/actual): プラットフォームごとの Ktor HttpClientEngine の注入

---

## 🚀 ビルドと起動方法

### Android アプリ
以下のコマンドでビルドが可能です。
```bash
./gradlew :androidApp:assembleDebug
```

### iOS アプリ
`/iosApp` ディレクトリを Xcode で開き、デバイスまたはシミュレータを指定して実行してください。

---

## 🧪 テストの実行とカバレッジ計測

すべてのビジネスロジック、マッパー、ユースケース、APIクライアント、リポジトリ層に対してテストが実装されています。

### テストの実行
```bash
./gradlew :shared:allTests
```

### カバレッジの検証 (Kover)
リポジトリ層（`ArtworkRepositoryImpl`）で **ブランチカバレッジ 100%** に達しているかを検証し、HTMLレポートを生成します。
```bash
# カバレッジ基準の検証（不足している場合はビルドエラーになります）
./gradlew :shared:koverVerify

# HTML形式のカバレッジレポートの生成
./gradlew :shared:koverHtmlReport
```
*レポートは `shared/build/reports/kover/html/` に出力されます。*