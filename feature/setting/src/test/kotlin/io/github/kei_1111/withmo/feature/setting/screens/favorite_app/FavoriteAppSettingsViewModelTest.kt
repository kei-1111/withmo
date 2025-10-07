package io.github.kei_1111.withmo.feature.setting.screens.favorite_app

import android.graphics.drawable.Drawable
import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.collections.immutable.persistentListOf
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

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteAppSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getFavoriteAppsUseCase: GetFavoriteAppsUseCase
    private lateinit var getAppIconSettingsUseCase: GetAppIconSettingsUseCase
    private lateinit var saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase
    private lateinit var viewModel: FavoriteAppSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getFavoriteAppsUseCase = mockk()
        getAppIconSettingsUseCase = mockk()
        saveFavoriteAppsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でお気に入りアプリリストを取得して表示する`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val favoriteAppList = listOf(
            FavoriteAppInfo(
                info = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1"),
                favoriteOrder = 0,
            ),
            FavoriteAppInfo(
                info = AppInfo(appIcon = mockIcon, packageName = "com.example.app2", label = "App2"),
                favoriteOrder = 1,
            ),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteAppList))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(FavoriteAppSettingsState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is FavoriteAppSettingsState.Loading)

            val finalState = awaitItem()
            assertTrue(finalState is FavoriteAppSettingsState.Stable)

            val stableState = finalState as FavoriteAppSettingsState.Stable
            assertEquals(2, stableState.favoriteAppList.size)
            assertEquals("com.example.app1", stableState.favoriteAppList[0].info.packageName)
            assertEquals("com.example.app2", stableState.favoriteAppList[1].info.packageName)
            assertFalse(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnAllAppListAppClickアクションで新しいアプリがお気に入りリストに追加される`() = runTest {
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )

        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.newapp", label = "New App")

        viewModel.state.test {
            assertEquals(FavoriteAppSettingsState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is FavoriteAppSettingsState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is FavoriteAppSettingsState.Stable)
            assertEquals(0, (initialState as FavoriteAppSettingsState.Stable).favoriteAppList.size)

            viewModel.onAction(FavoriteAppSettingsAction.OnAllAppListAppClick(appInfo))

            val updatedState = awaitItem()
            assertTrue(updatedState is FavoriteAppSettingsState.Stable)
            val stableState = updatedState as FavoriteAppSettingsState.Stable
            assertEquals(1, stableState.favoriteAppList.size)
            assertEquals(appInfo.packageName, stableState.favoriteAppList[0].info.packageName)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnFavoriteAppListAppClickアクションでアプリがお気に入りリストから削除される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo1 = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1")
        val appInfo2 = AppInfo(appIcon = mockIcon, packageName = "com.example.app2", label = "App2")
        val favoriteAppList = listOf(
            FavoriteAppInfo(info = appInfo1, favoriteOrder = 0),
            FavoriteAppInfo(info = appInfo2, favoriteOrder = 1),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteAppList))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(FavoriteAppSettingsState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is FavoriteAppSettingsState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is FavoriteAppSettingsState.Stable)
            assertEquals(2, (initialState as FavoriteAppSettingsState.Stable).favoriteAppList.size)

            viewModel.onAction(FavoriteAppSettingsAction.OnFavoriteAppListAppClick(appInfo1))

            val updatedState = awaitItem()
            assertTrue(updatedState is FavoriteAppSettingsState.Stable)
            val stableState = updatedState as FavoriteAppSettingsState.Stable
            assertEquals(1, stableState.favoriteAppList.size)
            assertEquals(appInfo2.packageName, stableState.favoriteAppList[0].info.packageName)
            assertEquals(0, stableState.favoriteAppList[0].favoriteOrder)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnAppSearchQueryChangeアクションで検索クエリが更新される`() = runTest {
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )

        val query = "test query"

        viewModel.state.test {
            assertEquals(FavoriteAppSettingsState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is FavoriteAppSettingsState.Loading)

            val initialState = awaitItem()
            assertTrue(initialState is FavoriteAppSettingsState.Stable)
            assertEquals("", (initialState as FavoriteAppSettingsState.Stable).appSearchQuery)

            viewModel.onAction(FavoriteAppSettingsAction.OnAppSearchQueryChange(query))

            val updatedState = awaitItem()
            assertTrue(updatedState is FavoriteAppSettingsState.Stable)
            assertEquals(query, (updatedState as FavoriteAppSettingsState.Stable).appSearchQuery)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションでお気に入りアプリが保存されNavigateBackエフェクトが送信される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val favoriteAppList = listOf(
            FavoriteAppInfo(
                info = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1"),
                favoriteOrder = 0,
            ),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteAppList))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))
        coEvery { saveFavoriteAppsUseCase(any()) } returns Unit

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(FavoriteAppSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is FavoriteAppSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as FavoriteAppSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(FavoriteAppSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveFavoriteAppsUseCase(persistentListOf(favoriteAppList[0])) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val favoriteAppList = listOf(
            FavoriteAppInfo(
                info = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1"),
                favoriteOrder = 0,
            ),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteAppList))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))
        coEvery { saveFavoriteAppsUseCase(any()) } throws Exception("Save failed")

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(FavoriteAppSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is FavoriteAppSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as FavoriteAppSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(FavoriteAppSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(FavoriteAppSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.failure(error))
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = FavoriteAppSettingsViewModel(
            getFavoriteAppsUseCase,
            getAppIconSettingsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(FavoriteAppSettingsState.Idle, awaitItem())

            val loadingState = awaitItem()
            assertTrue(loadingState is FavoriteAppSettingsState.Loading)

            val state = awaitItem()
            assertTrue(state is FavoriteAppSettingsState.Error)
            assertEquals(error, (state as FavoriteAppSettingsState.Error).error)
        }
    }
}
