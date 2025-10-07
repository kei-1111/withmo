# テストガイド

withmoプロジェクトにおけるテスト作成の標準化されたガイドラインを定義します。

## 目次

- [テスト戦略](#テスト戦略)
- [ViewModel（MVI）のテスト](#viewmodelmviのテスト)
- [UseCaseのテスト](#usecaseのテスト)
- [Repositoryのテスト](#repositoryのテスト)
- [共通パターンとベストプラクティス](#共通パターンとベストプラクティス)

---

## テスト戦略

### テスト範囲

このプロジェクトでは以下の層をユニットテストでカバーします：

1. **ViewModel層**: MVIパターンに基づくState、Action、Effectの検証
2. **Domain層**: UseCaseのビジネスロジックの検証
3. **Data層**: Repositoryのデータ取得・保存ロジックの検証

### 使用ライブラリ

- **JUnit**: テストフレームワーク
- **MockK**: Kotlinに特化したモックライブラリ
- **Turbine**: FlowとStateFlowのテスト用ライブラリ
- **kotlinx-coroutines-test**: コルーチンのテスト用ライブラリ

---

## ViewModel（MVI）のテスト

### 基本構造

ViewModelのテストは以下の要素を検証します：

1. **State遷移**: 初期状態から最終状態への正しい遷移
2. **Action処理**: ユーザーアクションに対する適切な状態更新
3. **Effect送信**: 一度だけ実行される副作用（ナビゲーション、トーストなど）
4. **エラーハンドリング**: 例外発生時の適切な状態遷移

### テンプレート

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class XxxViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getXxxUseCase: GetXxxUseCase
    private lateinit var saveXxxUseCase: SaveXxxUseCase
    private lateinit var viewModel: XxxViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getXxxUseCase = mockk()
        saveXxxUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でデータを取得して表示する`() = runTest {
        // Arrange
        val data = Xxx(...)
        coEvery { getXxxUseCase() } returns flowOf(Result.success(data))

        // Act
        viewModel = XxxViewModel(getXxxUseCase, saveXxxUseCase)

        // Assert
        viewModel.state.test {
            assertEquals(XxxState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is XxxState.Stable)
            // assertions...
        }
    }
}
```

### 重要なポイント

#### 1. android.util.Logのモック化

ViewModelのcatch文で`Log.e`を使用している場合、テスト実行時に「Method e in android.util.Log not mocked」エラーが発生します。これを防ぐために、必ず`setUp()`で以下のモックを追加してください：

```kotlin
@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)
    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0  // ← 必須
    // ...
}

@After
fun tearDown() {
    Dispatchers.resetMain()
    unmockkStatic(Log::class)  // ← クリーンアップも必須
}
```

#### 2. Loading状態の有無

**重要**: Loading状態の観測可否は、ViewModelの実装パターンによって異なります。

##### パターンA: 単一Flowの場合（Loading状態なし）

```kotlin
// ViewModel実装
private val xxxDataStream = getXxxUseCase()

init {
    viewModelScope.launch {
        updateViewModelState { copy(statusType = LOADING) }
        xxxDataStream.collect { result ->
            // ...
        }
    }
}
```

**理由**: `flowOf(Result.success(...))` は同期的に即座に値を発行するため、`LOADING` 状態が設定された直後に `STABLE` に上書きされます。テストでは観測できません。

**テストパターン**:
```kotlin
viewModel.state.test {
    assertEquals(XxxState.Idle, awaitItem())
    // Loading状態はスキップ
    val state = awaitItem()
    assertTrue(state is XxxState.Stable)
    // assertions...
}
```

**該当例**: `ClockSettingsViewModel`, `NotificationSettingsViewModel`, `AppIconSettingsViewModel`など

##### パターンB: combine使用の場合（Loading状態あり）

```kotlin
// ViewModel実装
private val xxxDataStream = combine(
    getAaaUseCase(),
    getBbbUseCase(),
) { aaa, bbb ->
    // ...
}

init {
    viewModelScope.launch {
        updateViewModelState { copy(statusType = LOADING) }
        xxxDataStream.collect { result ->
            // ...
        }
    }
}
```

**理由**: `combine()` は複数のFlowを組み合わせるため非同期的に動作し、すべてのFlowから最初の値を待つ必要があります。この遅延により`LOADING` 状態が観測可能になります。

**テストパターン**:
```kotlin
viewModel.state.test {
    assertEquals(XxxState.Idle, awaitItem())

    val loadingState = awaitItem()
    assertTrue(loadingState is XxxState.Loading)

    val state = awaitItem()
    assertTrue(state is XxxState.Stable)
    // assertions...
}
```

**該当例**: `FavoriteAppSettingsViewModel`, `HomeViewModel`など

#### 3. Effect（副作用）のテスト

Effectは一度だけ発行される副作用（ナビゲーション、トーストなど）をテストします。

```kotlin
@Test
fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
    // Arrange
    val data = Xxx(...)
    coEvery { getXxxUseCase() } returns flowOf(Result.success(data))
    coEvery { saveXxxUseCase(any()) } returns Unit

    viewModel = XxxViewModel(getXxxUseCase, saveXxxUseCase)
    advanceUntilIdle()  // 初期化完了を待つ

    // Act & Assert
    viewModel.effect.test {
        viewModel.onAction(XxxAction.OnSaveButtonClick)

        val toastEffect = awaitItem()
        assertTrue(toastEffect is XxxEffect.ShowToast)
        assertEquals("保存しました", (toastEffect as XxxEffect.ShowToast).message)

        val navigateEffect = awaitItem()
        assertEquals(XxxEffect.NavigateBack, navigateEffect)

        advanceUntilIdle()

        coVerify { saveXxxUseCase(data) }
    }
}
```

#### 4. エラーハンドリングのテスト

保存失敗時のエラートーストテスト：

```kotlin
@Test
fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
    // Arrange
    coEvery { getXxxUseCase() } returns flowOf(Result.success(Xxx()))
    coEvery { saveXxxUseCase(any()) } throws Exception("Save failed")

    viewModel = XxxViewModel(getXxxUseCase, saveXxxUseCase)
    advanceUntilIdle()

    // Act & Assert
    viewModel.effect.test {
        viewModel.onAction(XxxAction.OnSaveButtonClick)

        val effect = awaitItem()
        assertTrue(effect is XxxEffect.ShowToast)
        assertEquals("保存に失敗しました", (effect as XxxEffect.ShowToast).message)
    }
}
```

初期化時のエラー状態テスト：

```kotlin
@Test
fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
    // Arrange
    val error = Exception("Failed to load settings")
    coEvery { getXxxUseCase() } returns flowOf(Result.failure(error))

    // Act
    viewModel = XxxViewModel(getXxxUseCase, saveXxxUseCase)

    // Assert
    viewModel.state.test {
        assertEquals(XxxState.Idle, awaitItem())
        // combine使用時のみLoading状態を観測

        val state = awaitItem()
        assertTrue(state is XxxState.Error)
        assertEquals(error, (state as XxxState.Error).error)
    }
}
```

### テストすべき項目

#### Stateテスト
- ✅ 初期状態でデータを取得して表示する
- ✅ アクション実行時に状態が更新される
- ✅ 初期化時にエラーが発生した場合エラー状態になる

#### Effectテスト
- ✅ 保存成功時にトーストとNavigateBackエフェクトが送信される
- ✅ 保存失敗時にエラートーストが表示される
- ✅ 戻るボタンクリック時にNavigateBackエフェクトが送信される

#### Actionテスト
各Actionごとに適切な状態更新が行われることを確認

---

## UseCaseのテスト

### 基本構造

UseCaseは単一責任の原則に従い、Repositoryから取得したデータを適切に変換・加工します。

### テンプレート

```kotlin
class GetXxxUseCaseTest {

    private lateinit var mockRepository: XxxRepository
    private lateinit var useCase: GetXxxUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetXxxUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのデータを取得できること`() = runTest {
        // Arrange
        every { mockRepository.xxx } returns flowOf(Result.success(Xxx()))

        // Act & Assert
        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val data = result.getOrThrow()
            // assertions...
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `エラーが発生した場合Result_failureが返されること`() = runTest {
        // Arrange
        val exception = RuntimeException("Database error")
        every { mockRepository.xxx } returns flowOf(Result.failure(exception))

        // Act & Assert
        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
```

### テストすべき項目

- ✅ デフォルト値の取得
- ✅ カスタム値の取得
- ✅ データ変更の反映（Flowの更新）
- ✅ distinctUntilChangedの動作確認（同じ値では流れない）
- ✅ エラーハンドリング（Result.failure）

### 実例

```kotlin
@Test
fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
    val sameSettings = ClockSettings(
        isClockShown = true,
        clockType = ClockType.TOP_DATE,
    )
    val userSettings = UserSettings(clockSettings = sameSettings)
    every { mockRepository.userSettings } returns flowOf(
        Result.success(userSettings),
        Result.success(userSettings),
        Result.success(userSettings),
    )

    useCase().test {
        awaitItem()  // 最初の値のみ
        awaitComplete()  // 2回目、3回目は流れない
    }
}
```

---

## Repositoryのテスト

### 基本構造

Repositoryは、DataStoreやRoomなどのデータソースとのやり取りをテストします。

### テンプレート

```kotlin
@OptIn(ExperimentalCoroutinesApi::class)
class XxxRepositoryImplTest {

    private lateinit var mockDataStore: DataStore<Preferences>
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockDataStore = mockk(relaxed = true)
        testDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `デフォルト設定を取得できること`() = runTest(testDispatcher) {
        // Arrange
        every { mockDataStore.data } returns flowOf(emptyPreferences())

        val repository = XxxRepositoryImpl(mockDataStore, testDispatcher)

        // Act & Assert
        repository.xxx.test {
            val result = awaitItem()
            assert(result.isSuccess)
            val data = result.getOrThrow()
            // assertions...
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `データを保存できること`() = runTest(testDispatcher) {
        // Arrange
        coEvery { mockDataStore.updateData(any()) } returns emptyPreferences()

        val data = Xxx(...)
        val repository = XxxRepositoryImpl(mockDataStore, testDispatcher)

        // Act
        repository.saveXxx(data)

        // Assert
        coVerify { mockDataStore.updateData(any()) }
    }
}
```

### 重要なポイント

#### 1. TestDispatcherの選択

- **UnconfinedTestDispatcher**: データストアやFlow操作のテストに推奨
- **StandardTestDispatcher**: 明示的なタイミング制御が必要な場合

```kotlin
private lateinit var testDispatcher: TestDispatcher

@Before
fun setup() {
    testDispatcher = UnconfinedTestDispatcher()
}
```

#### 2. DataStoreのモック

```kotlin
private lateinit var mockDataStore: DataStore<Preferences>

@Before
fun setup() {
    mockDataStore = mockk(relaxed = true)  // relaxed = trueで未定義メソッドも許容
}
```

### テストすべき項目

- ✅ デフォルト値の取得
- ✅ データの保存
- ✅ データの更新
- ✅ 複数設定の保存と取得
- ✅ エラーハンドリング

---

## 共通パターンとベストプラクティス

### 1. テストの命名規則

日本語のバッククォートを使用した説明的な命名：

```kotlin
@Test
fun `初期状態でデータを取得して表示する`() = runTest { }

@Test
fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest { }

@Test
fun `エラーが発生した場合Result_failureが返されること`() = runTest { }
```

### 2. Arrange-Act-Assert パターン

テストは以下の3つのセクションで構成します：

```kotlin
@Test
fun `テスト名`() = runTest {
    // Arrange（準備）
    val data = Xxx(...)
    coEvery { useCase() } returns flowOf(Result.success(data))

    // Act（実行）
    viewModel = XxxViewModel(useCase)

    // Assert（検証）
    viewModel.state.test {
        // assertions...
    }
}
```

### 3. MockKの使用パターン

#### 基本的なモック

```kotlin
private lateinit var mockRepository: XxxRepository

@Before
fun setup() {
    mockRepository = mockk()
}
```

#### 戻り値の設定

```kotlin
// 通常のメソッド
every { mockRepository.getData() } returns data

// suspend関数
coEvery { mockRepository.saveData(any()) } returns Unit

// Flow
every { mockRepository.dataFlow } returns flowOf(Result.success(data))
```

#### 呼び出しの検証

```kotlin
// 通常のメソッド
verify { mockRepository.getData() }

// suspend関数
coVerify { mockRepository.saveData(expectedData) }

// 呼び出し回数の検証
coVerify(exactly = 1) { mockRepository.saveData(any()) }
```

#### 静的メソッドのモック

```kotlin
@Before
fun setUp() {
    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0
}

@After
fun tearDown() {
    unmockkStatic(Log::class)
}
```

### 4. Turbineの使用パターン

#### 基本的なFlow/StateFlowのテスト

```kotlin
flow.test {
    val item = awaitItem()
    assertEquals(expected, item)
    cancelAndIgnoreRemainingEvents()
}
```

#### 複数の値を検証

```kotlin
flow.test {
    assertEquals(first, awaitItem())
    assertEquals(second, awaitItem())
    assertEquals(third, awaitItem())
    cancelAndIgnoreRemainingEvents()
}
```

#### イベントがないことを確認

```kotlin
flow.test {
    viewModel.onAction(SomeAction)
    expectNoEvents()  // イベントが発行されないことを検証
}
```

#### 完了を待つ

```kotlin
flow.test {
    awaitItem()
    awaitComplete()  // Flowの完了を待つ
}
```

### 5. コルーチンテストのパターン

#### StandardTestDispatcher

```kotlin
private val testDispatcher = StandardTestDispatcher()

@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)
}

@After
fun tearDown() {
    Dispatchers.resetMain()
}

@Test
fun `テスト名`() = runTest {
    // テストコード
    advanceUntilIdle()  // すべてのコルーチンの完了を待つ
}
```

#### UnconfinedTestDispatcher（Repositoryテスト用）

```kotlin
private lateinit var testDispatcher: TestDispatcher

@Before
fun setup() {
    testDispatcher = UnconfinedTestDispatcher()
}

@Test
fun `テスト名`() = runTest(testDispatcher) {
    // テストコード
}
```

### 6. テスト実行コマンド

```bash
# すべてのテストを実行
./gradlew test

# 特定モジュールのテストを実行
./gradlew :feature:setting:testDebugUnitTest

# 特定のテストクラスを実行
./gradlew :feature:setting:testDebugUnitTest --tests "*NotificationSettingsViewModelTest*"

# テストレポートの確認
# build/reports/tests/testDebugUnitTest/index.html
```

### 7. 高度なモックパターン

#### SystemClockのモック

時間ベースのロジック（クールダウン、スロットリングなど）をテストする場合：

```kotlin
@Before
fun setUp() {
    Dispatchers.setMain(testDispatcher)
    mockkStatic(SystemClock::class)
    every { SystemClock.elapsedRealtime() } returns 1000L
    // ...
}

@After
fun tearDown() {
    Dispatchers.resetMain()
    unmockkStatic(SystemClock::class)
    // ...
}

@Test
fun `OnScaleSliderChangeアクションでスケールが更新されUnityにメッセージが送信される`() = runTest {
    // Arrange
    every { SystemClock.elapsedRealtime() } returns 1000L

    // Act & Assert
    viewModel.onAction(HomeAction.OnScaleSliderChange(1.5f))

    verify { AndroidToUnityMessenger.sendMessage(...) }
}
```

#### object（シングルトン）のモック

Unity統合などのobjectクラスをモックする場合：

```kotlin
@Before
fun setUp() {
    mockkObject(AndroidToUnityMessenger)
    every { AndroidToUnityMessenger.sendMessage(any(), any(), any()) } just Runs
    mockkObject(UnityToAndroidMessenger)
    every { UnityToAndroidMessenger.setReceiver(any()) } just Runs
}

@After
fun tearDown() {
    unmockkObject(AndroidToUnityMessenger)
    unmockkObject(UnityToAndroidMessenger)
}
```

#### value class（inline class）の扱い

`Offset`のようなvalue classはモックできないため、実際のインスタンスを使用：

```kotlin
// ❌ モックできない
// val offset: Offset = mockk()

// ✅ 実際のインスタンスを使用
val offset = Offset(0f, 0f)
val placedWidget = PlacedWidgetInfo(
    info = widgetInfo,
    width = 200,
    height = 100,
    position = offset,  // 実際のOffsetインスタンス
)
```

#### relaxedモックの使用

Androidフレームワークの複雑なクラス（AppWidgetProviderInfoなど）をモックする場合：

```kotlin
val widgetInfo = WidgetInfo(
    id = 1,
    info = mockk(relaxed = true),  // relaxed = trueで未定義メソッドも許容
)
```

### 8. よくあるエラーと対処法

#### エラー1: Method e in android.util.Log not mocked

**原因**: ViewModelのcatch文で`Log.e`を使用しているが、モックしていない

**対処法**:
```kotlin
@Before
fun setUp() {
    mockkStatic(Log::class)
    every { Log.e(any(), any(), any()) } returns 0
}

@After
fun tearDown() {
    unmockkStatic(Log::class)
}
```

#### エラー2: No value produced in 3s (Turbine timeout)

**原因**:
- Flowから値が発行されない
- `advanceUntilIdle()`を呼んでいない
- Loading状態の有無を間違えている

**対処法**:
- MockKの設定を確認（`coEvery`が正しいか）
- `advanceUntilIdle()`を追加
- ViewModelの実装を確認してLoading状態の有無を判断

#### エラー3: AssertionError on state transition

**原因**: 期待する状態と実際の状態が異なる

**対処法**:
- ViewModelの実装を確認
- combine使用時はLoading状態を追加
- 単一Flow使用時はLoading状態をスキップ

#### エラー4: Cannot mock value class / inline class

**原因**: `Offset`などのvalue classはモック不可

**対処法**:
```kotlin
// 実際のインスタンスを使用
val position = Offset(0f, 0f)
```

---

## まとめ

### テスト作成のチェックリスト

#### ViewModelテスト
- [ ] `@OptIn(ExperimentalCoroutinesApi::class)` アノテーション
- [ ] `StandardTestDispatcher` の使用
- [ ] `Dispatchers.setMain()` / `resetMain()` の設定
- [ ] `android.util.Log` のモック化
- [ ] Loading状態の有無を実装パターンで判断（combine使用 = あり、単一Flow = なし）
- [ ] State遷移テスト（初期状態、Action処理、エラー）
- [ ] Effectテスト（成功、失敗）
- [ ] `advanceUntilIdle()` の適切な配置

#### UseCaseテスト
- [ ] Repositoryのモック化
- [ ] デフォルト値テスト
- [ ] カスタム値テスト
- [ ] Flow更新テスト
- [ ] distinctUntilChangedテスト
- [ ] エラーハンドリングテスト

#### Repositoryテスト
- [ ] `UnconfinedTestDispatcher` の使用
- [ ] DataStore/Daoのモック化
- [ ] デフォルト値取得テスト
- [ ] データ保存テスト
- [ ] `coVerify` での呼び出し確認

このガイドに従うことで、一貫性のある保守しやすいテストを作成できます。