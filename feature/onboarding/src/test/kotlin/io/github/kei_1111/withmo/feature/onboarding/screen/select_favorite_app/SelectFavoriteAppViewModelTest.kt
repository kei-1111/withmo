package io.github.kei_1111.withmo.feature.onboarding.screen.select_favorite_app

import android.graphics.drawable.Drawable
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveFavoriteAppsUseCase
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
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
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SelectFavoriteAppViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getFavoriteAppsUseCase: GetFavoriteAppsUseCase
    private lateinit var saveFavoriteAppsUseCase: SaveFavoriteAppsUseCase
    private lateinit var viewModel: SelectFavoriteAppViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        getFavoriteAppsUseCase = mockk()
        saveFavoriteAppsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val finalState = awaitItem()
            assertTrue(finalState is SelectFavoriteAppState.Stable)

            val stableState = finalState as SelectFavoriteAppState.Stable
            assertEquals(2, stableState.selectedAppList.size)
            assertEquals("com.example.app1", stableState.selectedAppList[0].info.packageName)
            assertEquals("com.example.app2", stableState.selectedAppList[1].info.packageName)
        }
    }

    @Test
    fun `OnAllAppListAppClickアクションで新しいアプリがお気に入りリストに追加される`() = runTest {
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.newapp", label = "New App")

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SelectFavoriteAppState.Stable)
            assertEquals(0, (initialState as SelectFavoriteAppState.Stable).selectedAppList.size)

            viewModel.onAction(SelectFavoriteAppAction.OnAllAppListAppClick(appInfo))

            val updatedState = awaitItem()
            assertTrue(updatedState is SelectFavoriteAppState.Stable)
            val stableState = updatedState as SelectFavoriteAppState.Stable
            assertEquals(1, stableState.selectedAppList.size)
            assertEquals(appInfo.packageName, stableState.selectedAppList[0].info.packageName)
        }
    }

    @Test
    fun `OnAllAppListAppClickアクションで既に追加されているアプリは追加されない`() = runTest {
        val mockIcon = AppIcon(
            foregroundIcon = mockk<Drawable>(),
            backgroundIcon = null,
        )
        val appInfo = AppInfo(appIcon = mockIcon, packageName = "com.example.app1", label = "App1")
        val favoriteAppList = listOf(
            FavoriteAppInfo(info = appInfo, favoriteOrder = 0),
        )
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(favoriteAppList))

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SelectFavoriteAppState.Stable)
            assertEquals(1, (initialState as SelectFavoriteAppState.Stable).selectedAppList.size)

            viewModel.onAction(SelectFavoriteAppAction.OnAllAppListAppClick(appInfo))

            expectNoEvents()
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

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SelectFavoriteAppState.Stable)
            assertEquals(2, (initialState as SelectFavoriteAppState.Stable).selectedAppList.size)

            viewModel.onAction(SelectFavoriteAppAction.OnFavoriteAppListAppClick(appInfo1))

            val updatedState = awaitItem()
            assertTrue(updatedState is SelectFavoriteAppState.Stable)
            val stableState = updatedState as SelectFavoriteAppState.Stable
            assertEquals(1, stableState.selectedAppList.size)
            assertEquals(appInfo2.packageName, stableState.selectedAppList[0].info.packageName)
            assertEquals(0, stableState.selectedAppList[0].favoriteOrder)
        }
    }

    @Test
    fun `OnAppSearchQueryChangeアクションで検索クエリが更新される`() = runTest {
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.success(emptyList()))

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        val query = "test query"

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SelectFavoriteAppState.Stable)
            assertEquals("", (initialState as SelectFavoriteAppState.Stable).appSearchQuery)

            viewModel.onAction(SelectFavoriteAppAction.OnAppSearchQueryChange(query))

            val updatedState = awaitItem()
            assertTrue(updatedState is SelectFavoriteAppState.Stable)
            assertEquals(query, (updatedState as SelectFavoriteAppState.Stable).appSearchQuery)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでお気に入りアプリが保存されNavigateBackエフェクトが送信される`() = runTest {
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
        coEvery { saveFavoriteAppsUseCase(any()) } returns Unit

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SelectFavoriteAppAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(SelectFavoriteAppEffect.NavigateBack, effect)

            advanceUntilIdle()

            coVerify { saveFavoriteAppsUseCase(persistentListOf(favoriteAppList[0])) }
        }
    }

    @Test
    fun `OnNextButtonClickアクションでお気に入りアプリが保存されNavigateSelectDisplayModelエフェクトが送信される`() = runTest {
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
        coEvery { saveFavoriteAppsUseCase(any()) } returns Unit

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SelectFavoriteAppAction.OnNextButtonClick)

            val effect = awaitItem()
            assertEquals(SelectFavoriteAppEffect.NavigateSelectDisplayModel, effect)

            advanceUntilIdle()

            coVerify { saveFavoriteAppsUseCase(persistentListOf(favoriteAppList[0])) }
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load favorite apps")
        coEvery { getFavoriteAppsUseCase() } returns flowOf(Result.failure(error))

        viewModel = SelectFavoriteAppViewModel(
            getFavoriteAppsUseCase,
            saveFavoriteAppsUseCase,
        )

        viewModel.state.test {
            assertEquals(SelectFavoriteAppState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SelectFavoriteAppState.Error)
            assertEquals(error, (state as SelectFavoriteAppState.Error).error)
        }
    }
}
