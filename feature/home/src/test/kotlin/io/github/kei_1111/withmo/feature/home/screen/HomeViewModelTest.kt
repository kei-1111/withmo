package io.github.kei_1111.withmo.feature.home.screen

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.SystemClock
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.compose.ui.geometry.Offset
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.common.unity.AndroidToUnityMessenger
import io.github.kei_1111.withmo.core.common.unity.UnityMethod
import io.github.kei_1111.withmo.core.common.unity.UnityObject
import io.github.kei_1111.withmo.core.common.unity.UnityToAndroidMessenger
import io.github.kei_1111.withmo.core.domain.manager.ModelFileManager
import io.github.kei_1111.withmo.core.domain.manager.WidgetManager
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetModelChangeWarningStatusUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetPlacedItemsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetUserSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.MarkModelChangeWarningShownUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelFilePathUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveModelSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SavePlacedItemsUseCase
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.mockkStatic
import io.mockk.unmockkObject
import io.mockk.unmockkStatic
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import java.io.File

@Suppress("LargeClass")
@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getUserSettingsUseCase: GetUserSettingsUseCase
    private lateinit var getFavoriteAppsUseCase: GetFavoriteAppsUseCase
    private lateinit var getPlacedItemsUseCase: GetPlacedItemsUseCase
    private lateinit var savePlacedItemsUseCase: SavePlacedItemsUseCase
    private lateinit var getModelChangeWarningStatusUseCase: GetModelChangeWarningStatusUseCase
    private lateinit var markModelChangeWarningShownUseCase: MarkModelChangeWarningShownUseCase
    private lateinit var modelFileManager: ModelFileManager
    private lateinit var widgetManager: WidgetManager
    private lateinit var saveModelFilePathUseCase: SaveModelFilePathUseCase
    private lateinit var saveModelSettingsUseCase: SaveModelSettingsUseCase
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        mockkStatic(SystemClock::class)
        mockkObject(AndroidToUnityMessenger)
        mockkObject(UnityToAndroidMessenger)
        every { Log.e(any(), any(), any()) } returns 0
        every { SystemClock.elapsedRealtime() } returns 0L
        every { AndroidToUnityMessenger.sendMessage(any(), any(), any()) } returns Unit
        getUserSettingsUseCase = mockk()
        getFavoriteAppsUseCase = mockk()
        getPlacedItemsUseCase = mockk()
        savePlacedItemsUseCase = mockk()
        getModelChangeWarningStatusUseCase = mockk()
        markModelChangeWarningShownUseCase = mockk()
        modelFileManager = mockk()
        widgetManager = mockk()
        saveModelFilePathUseCase = mockk()
        saveModelSettingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
        unmockkStatic(SystemClock::class)
        unmockkObject(AndroidToUnityMessenger)
        unmockkObject(UnityToAndroidMessenger)
    }

    @Test
    fun `初期状態でホームデータを取得して表示する`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val userSettings = UserSettings()
        val favoriteApps = listOf(
            FavoriteAppInfo(
                info = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1"),
                favoriteOrder = 0,
            ),
        )
        val placedItems = emptyList<PlaceableItem>()

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(userSettings))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteApps))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(placedItems))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val stableState = awaitItem()
            assertTrue(stableState is HomeState.Stable)
            val stable = stableState as HomeState.Stable
            assertEquals(1, stable.favoriteAppInfoList.size)
            assertEquals(0, stable.placedItemList.size)
            assertEquals(PageContent.DISPLAY_MODEL, stable.currentPage)
        }
    }

    @Test
    fun `OnAppClickアクションでLaunchAppエフェクトが送信される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.app", label = "App")

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(HomeAction.OnAppClick(appInfo))

            val effect = awaitItem()
            assertTrue(effect is HomeEffect.LaunchApp)
            assertEquals(appInfo, (effect as HomeEffect.LaunchApp).appInfo)
        }
    }

    @Test
    fun `OnAppLongClickアクションでDeleteAppエフェクトが送信される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.app", label = "App")

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(HomeAction.OnAppLongClick(appInfo))

            val effect = awaitItem()
            assertTrue(effect is HomeEffect.DeleteApp)
            assertEquals(appInfo, (effect as HomeEffect.DeleteApp).appInfo)
        }
    }

    @Test
    fun `OnShowScaleSliderButtonClickアクションでスケールスライダーが表示される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isChangeModelScaleContentShown)

            viewModel.onAction(HomeAction.OnShowScaleSliderButtonClick)

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isChangeModelScaleContentShown)
        }
    }

    @Test
    fun `OnCloseScaleSliderButtonClickアクションでスケールスライダーが非表示になり設定が保存される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { saveModelSettingsUseCase(any()) } returns Unit

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnShowScaleSliderButtonClick)

            val shownState = awaitItem()
            assertTrue(shownState is HomeState.Stable)
            assertTrue((shownState as HomeState.Stable).isChangeModelScaleContentShown)

            viewModel.onAction(HomeAction.OnCloseScaleSliderButtonClick)
            advanceUntilIdle()

            val closedState = awaitItem()
            assertTrue(closedState is HomeState.Stable)
            assertFalse((closedState as HomeState.Stable).isChangeModelScaleContentShown)

            coVerify { saveModelSettingsUseCase(any()) }
        }
    }

    @Test
    fun `OnScaleSliderChangeアクションでスケールが更新されUnityにメッセージが送信される`() = runTest {
        val initialSettings = UserSettings(
            modelSettings = ModelSettings(scale = 1.0f),
        )
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        every { SystemClock.elapsedRealtime() } returns 1000L

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())
            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertEquals(1.0f, (initialState as HomeState.Stable).currentUserSettings.modelSettings.scale)

            viewModel.onAction(HomeAction.OnScaleSliderChange(1.5f))

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertEquals(1.5f, (updatedState as HomeState.Stable).currentUserSettings.modelSettings.scale)

            verify { AndroidToUnityMessenger.sendMessage(UnityObject.VRM_LOADER, UnityMethod.ADJUST_SCALE, "1.5") }
        }
    }

    @Test
    fun `OnResizeWidgetBadgeClickアクションでウィジェットのリサイズが開始される`() = runTest {
        val widgetInfo = WidgetInfo(
            id = 1,
            info = mockk(relaxed = true),
        )
        val placedWidget = PlacedWidgetInfo(
            info = widgetInfo,
            width = 200,
            height = 100,
            position = Offset(0f, 0f),
        )

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(listOf(placedWidget)))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())
            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isWidgetResizing)
            assertEquals(1, initialState.placedItemList.size)

            viewModel.onAction(HomeAction.OnResizeWidgetBadgeClick(placedWidget))

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            val stableState = updatedState as HomeState.Stable
            assertTrue(stableState.isWidgetResizing)
            assertEquals(placedWidget, stableState.resizingWidget)
            assertEquals(0, stableState.placedItemList.size)
        }
    }

    @Test
    fun `OnWidgetResizeBottomSheetCloseアクションでリサイズが終了しウィジェットが追加される`() = runTest {
        val widgetInfo = WidgetInfo(
            id = 1,
            info = mockk(relaxed = true),
        )
        val placedWidget = PlacedWidgetInfo(
            info = widgetInfo,
            width = 200,
            height = 100,
            position = Offset(0f, 0f),
        )
        val resizedWidget = PlacedWidgetInfo(
            info = widgetInfo,
            width = 300,
            height = 150,
            position = Offset(50f, 50f),
        )

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(listOf(placedWidget)))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())
            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnResizeWidgetBadgeClick(placedWidget))

            val resizingState = awaitItem()
            assertTrue(resizingState is HomeState.Stable)
            val resizing = resizingState as HomeState.Stable
            assertTrue(resizing.isWidgetResizing)
            assertEquals(0, resizing.placedItemList.size)

            viewModel.onAction(HomeAction.OnWidgetResizeBottomSheetClose(resizedWidget))

            val closedState = awaitItem()
            assertTrue(closedState is HomeState.Stable)
            val closed = closedState as HomeState.Stable
            assertFalse(closed.isWidgetResizing)
            assertEquals(null, closed.resizingWidget)
            assertEquals(1, closed.placedItemList.size)
            assertEquals(resizedWidget, closed.placedItemList[0])
        }
    }

    @Test
    fun `OnConfigureWidgetLauncherResultアクションで結果がOKの場合ウィジェットが追加される`() = runTest {
        val result = ActivityResult(RESULT_OK, null)

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.onAction(HomeAction.OnConfigureWidgetLauncherResult(result))
        advanceUntilIdle()
    }

    @Test
    fun `OnConfigureWidgetLauncherResultアクションで結果がキャンセルの場合ウィジェットが削除される`() = runTest {
        val result = ActivityResult(RESULT_CANCELED, null)

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.onAction(HomeAction.OnConfigureWidgetLauncherResult(result))
        advanceUntilIdle()
    }

    @Test
    fun `OnBindWidgetLauncherResultアクションで結果がOKの場合処理が実行される`() = runTest {
        val result = ActivityResult(RESULT_OK, null)

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.onAction(HomeAction.OnBindWidgetLauncherResult(result))
        advanceUntilIdle()
    }

    @Test
    fun `OnSetDefaultModelButtonClickアクションでデフォルトモデルが設定される`() = runTest {
        val defaultFile = mockk<File>()
        val defaultFilePath = "/path/to/default.vrm"
        val customFilePath = "/path/to/custom.vrm"

        coEvery { getUserSettingsUseCase() } returns flowOf(
            Result.success(UserSettings(modelFilePath = ModelFilePath(customFilePath))),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { modelFileManager.copyVrmFileFromAssets() } returns defaultFile
        every { defaultFile.absolutePath } returns defaultFilePath
        coEvery { saveModelFilePathUseCase(any()) } returns Unit

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isModelLoading)

            viewModel.onAction(HomeAction.OnSetDefaultModelButtonClick)
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isModelLoading)

            coVerify { saveModelFilePathUseCase(ModelFilePath(defaultFilePath)) }
        }
    }

    @Test
    fun `OnNavigateSettingsButtonClickアクションでNavigateSettingsエフェクトが送信される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(HomeAction.OnNavigateSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(HomeEffect.NavigateSettings, effect)
        }
    }

    @Test
    fun `OnOpenDocumentButtonClickアクションで警告が表示されていない場合ダイアログが表示される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getModelChangeWarningStatusUseCase() } returns false

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isModelChangeWarningDialogShown)

            viewModel.onAction(HomeAction.OnOpenDocumentButtonClick)
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isModelChangeWarningDialogShown)
        }
    }

    @Test
    fun `OnOpenDocumentButtonClickアクションで警告が表示済みの場合OpenDocumentエフェクトが送信される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getModelChangeWarningStatusUseCase() } returns true

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(HomeAction.OnOpenDocumentButtonClick)
            advanceUntilIdle()

            val effect = awaitItem()
            assertEquals(HomeEffect.OpenDocument, effect)
        }
    }

    @Test
    fun `OnModelChangeWarningDialogConfirmアクションでダイアログが非表示になりOpenDocumentエフェクトが送信される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getModelChangeWarningStatusUseCase() } returns false
        coEvery { markModelChangeWarningShownUseCase() } returns Unit

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnOpenDocumentButtonClick)
            advanceUntilIdle()

            val dialogShownState = awaitItem()
            assertTrue(dialogShownState is HomeState.Stable)
            assertTrue((dialogShownState as HomeState.Stable).isModelChangeWarningDialogShown)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnModelChangeWarningDialogConfirm)
                advanceUntilIdle()

                val effect = awaitItem()
                assertEquals(HomeEffect.OpenDocument, effect)
            }

            val dialogHiddenState = awaitItem()
            assertTrue(dialogHiddenState is HomeState.Stable)
            assertFalse((dialogHiddenState as HomeState.Stable).isModelChangeWarningDialogShown)

            coVerify { markModelChangeWarningShownUseCase() }
        }
    }

    @Test
    fun `OnModelChangeWarningDialogDismissアクションでダイアログが非表示になる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getModelChangeWarningStatusUseCase() } returns false

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnOpenDocumentButtonClick)
            advanceUntilIdle()

            val dialogShownState = awaitItem()
            assertTrue(dialogShownState is HomeState.Stable)
            assertTrue((dialogShownState as HomeState.Stable).isModelChangeWarningDialogShown)

            viewModel.onAction(HomeAction.OnModelChangeWarningDialogDismiss)

            val dialogHiddenState = awaitItem()
            assertTrue(dialogHiddenState is HomeState.Stable)
            assertFalse((dialogHiddenState as HomeState.Stable).isModelChangeWarningDialogShown)
        }
    }

    @Test
    fun `OnAppListSheetSwipeUpアクションでシートが開く`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isAppListSheetOpened)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnAppListSheetSwipeUp)

                val effect = awaitItem()
                assertEquals(HomeEffect.ShowAppListSheet, effect)
            }

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isAppListSheetOpened)
        }
    }

    @Test
    fun `OnAppListSheetSwipeDownアクションでシートが閉じる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnAppListSheetSwipeUp)

                val showEffect = awaitItem()
                assertEquals(HomeEffect.ShowAppListSheet, showEffect)
            }

            val openedState = awaitItem()
            assertTrue(openedState is HomeState.Stable)
            assertTrue((openedState as HomeState.Stable).isAppListSheetOpened)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnAppListSheetSwipeDown)

                val hideEffect = awaitItem()
                assertEquals(HomeEffect.HideAppListSheet, hideEffect)
            }

            val closedState = awaitItem()
            assertTrue(closedState is HomeState.Stable)
            assertFalse((closedState as HomeState.Stable).isAppListSheetOpened)
        }
    }

    @Test
    fun `OnAddPlaceableItemButtonClickアクションでプレースアブルアイテムリストシートが開く`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isPlaceableItemListSheetOpened)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnAddPlaceableItemButtonClick)

                val effect = awaitItem()
                assertEquals(HomeEffect.ShowPlaceableItemListSheet, effect)
            }

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isPlaceableItemListSheetOpened)
        }
    }

    @Test
    fun `OnPlaceableItemListSheetSwipeDownアクションでシートが閉じる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnAddPlaceableItemButtonClick)

                val showEffect = awaitItem()
                assertEquals(HomeEffect.ShowPlaceableItemListSheet, showEffect)
            }

            val openedState = awaitItem()
            assertTrue(openedState is HomeState.Stable)
            assertTrue((openedState as HomeState.Stable).isPlaceableItemListSheetOpened)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnPlaceableItemListSheetSwipeDown)

                val hideEffect = awaitItem()
                assertEquals(HomeEffect.HidePlaceableItemListSheet, hideEffect)
            }

            val closedState = awaitItem()
            assertTrue(closedState is HomeState.Stable)
            assertFalse((closedState as HomeState.Stable).isPlaceableItemListSheetOpened)
        }
    }

    @Test
    fun `OnDisplayModelContentClickアクションでUnityにメッセージが送信される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.onAction(HomeAction.OnDisplayModelContentClick(x = 100f, y = 200f))

        verify { AndroidToUnityMessenger.sendMessage(any(), any(), "100.0,200.0") }
    }

    @Test
    fun `OnDisplayModelContentLongClickアクションでUnityにメッセージが送信される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.onAction(HomeAction.OnDisplayModelContentLongClick)

        verify { AndroidToUnityMessenger.sendMessage(any(), any(), "") }
    }

    @Test
    fun `OnDisplayModelContentSwipeLeftアクションでページが切り替わる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertEquals(PageContent.DISPLAY_MODEL, (initialState as HomeState.Stable).currentPage)

            viewModel.onAction(HomeAction.OnDisplayModelContentSwipeLeft)

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertEquals(PageContent.PLACEABLE_ITEM, (updatedState as HomeState.Stable).currentPage)
        }
    }

    @Test
    fun `OnPlaceableItemContentSwipeRightアクションでページが切り替わる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnDisplayModelContentSwipeLeft)

            val switchedState = awaitItem()
            assertTrue(switchedState is HomeState.Stable)
            assertEquals(PageContent.PLACEABLE_ITEM, (switchedState as HomeState.Stable).currentPage)

            viewModel.onAction(HomeAction.OnPlaceableItemContentSwipeRight)

            val backState = awaitItem()
            assertTrue(backState is HomeState.Stable)
            assertEquals(PageContent.DISPLAY_MODEL, (backState as HomeState.Stable).currentPage)
        }
    }

    @Test
    fun `OnPlaceableItemListSheetAppClickアクションでアプリが配置される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.app", label = "App")

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertEquals(0, (initialState as HomeState.Stable).placedItemList.size)

            viewModel.effect.test {
                viewModel.onAction(HomeAction.OnPlaceableItemListSheetAppClick(appInfo))

                val effect = awaitItem()
                assertEquals(HomeEffect.HidePlaceableItemListSheet, effect)
            }

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            val stable = updatedState as HomeState.Stable
            assertEquals(1, stable.placedItemList.size)
            assertTrue(stable.placedItemList[0] is PlacedAppInfo)
            assertEquals(appInfo, (stable.placedItemList[0] as PlacedAppInfo).info)
            assertFalse(stable.isPlaceableItemListSheetOpened)
        }
    }

    @Test
    fun `OnPlaceableItemContentLongClickアクションで編集モードになる`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isEditMode)

            viewModel.onAction(HomeAction.OnPlaceableItemContentLongClick)

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isEditMode)
        }
    }

    @Test
    fun `OnDeletePlaceableItemBadgeClickアクションでアイテムが削除される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.app", label = "App")
        val placeableItem = PlacedAppInfo(
            id = "test-id",
            info = appInfo,
            position = Offset(0f, 0f),
        )

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(listOf(placeableItem)))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertEquals(1, (initialState as HomeState.Stable).placedItemList.size)

            viewModel.onAction(HomeAction.OnDeletePlaceableItemBadgeClick(placeableItem))

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertEquals(0, (updatedState as HomeState.Stable).placedItemList.size)
        }
    }

    @Test
    fun `OnCompleteEditButtonClickアクションで編集モードが終了し設定が保存される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { savePlacedItemsUseCase(any()) } returns Unit

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)

            viewModel.onAction(HomeAction.OnPlaceableItemContentLongClick)

            val editModeState = awaitItem()
            assertTrue(editModeState is HomeState.Stable)
            assertTrue((editModeState as HomeState.Stable).isEditMode)

            viewModel.onAction(HomeAction.OnCompleteEditButtonClick)
            advanceUntilIdle()

            val completedState = awaitItem()
            assertTrue(completedState is HomeState.Stable)
            assertFalse((completedState as HomeState.Stable).isEditMode)

            coVerify { savePlacedItemsUseCase(any()) }
        }
    }

    @Test
    fun `OnOpenDocumentLauncherResultアクションでファイルが選択されなかった場合トーストが表示される`() = runTest {
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(HomeAction.OnOpenDocumentLauncherResult(null))
            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is HomeEffect.ShowToast)
            assertEquals("ファイルが選択されませんでした", (effect as HomeEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnOpenDocumentLauncherResultアクションで有効なファイルが選択された場合モデルが読み込まれる`() = runTest {
        val uri = mockk<Uri>()
        val file = mockk<File>()
        val filePath = "/path/to/model.vrm"

        coEvery { getUserSettingsUseCase() } returns flowOf(Result.success(UserSettings()))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { modelFileManager.deleteCopiedCacheFiles() } returns Unit
        coEvery { modelFileManager.copyVrmFileFromUri(uri) } returns file
        every { file.absolutePath } returns filePath
        coEvery { saveModelFilePathUseCase(any()) } returns Unit

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is HomeState.Stable)
            assertFalse((initialState as HomeState.Stable).isModelLoading)

            viewModel.onAction(HomeAction.OnOpenDocumentLauncherResult(uri))
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertTrue(updatedState is HomeState.Stable)
            assertTrue((updatedState as HomeState.Stable).isModelLoading)

            coVerify { saveModelFilePathUseCase(ModelFilePath(filePath)) }
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load home data")
        coEvery { getUserSettingsUseCase() } returns flowOf(Result.failure(error))
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getPlacedItemsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = HomeViewModel(
            getUserSettingsUseCase,
            getFavoriteAppsUseCase,
            getPlacedItemsUseCase,
            savePlacedItemsUseCase,
            getModelChangeWarningStatusUseCase,
            markModelChangeWarningShownUseCase,
            modelFileManager,
            widgetManager,
            saveModelFilePathUseCase,
            saveModelSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(HomeState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is HomeState.Loading)

            val errorState = awaitItem()
            assertTrue(errorState is HomeState.Error)
            assertEquals(error, (errorState as HomeState.Error).error)
        }
    }
}
