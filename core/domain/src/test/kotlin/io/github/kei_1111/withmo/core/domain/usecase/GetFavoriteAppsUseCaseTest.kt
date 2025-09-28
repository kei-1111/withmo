package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetFavoriteAppsUseCaseTest {

    private lateinit var mockRepository: FavoriteAppRepository
    private lateinit var useCase: GetFavoriteAppsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetFavoriteAppsUseCaseImpl(mockRepository)
    }

    @Test
    fun `空のお気に入りアプリリストを取得できること`() = runTest {
        every { mockRepository.favoriteAppsInfo } returns flowOf(Result.success(emptyList<FavoriteAppInfo>()))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val favoriteApps = result.getOrThrow()
            assertEquals(0, favoriteApps.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `お気に入りアプリリストを取得できること`() = runTest {
        val favoriteApps = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App1",
                    packageName = "com.example.app1",
                ),
                favoriteOrder = 0,
            ),
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App2",
                    packageName = "com.example.app2",
                ),
                favoriteOrder = 1,
            ),
        )
        every { mockRepository.favoriteAppsInfo } returns flowOf(Result.success(favoriteApps))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val favoriteApps = result.getOrThrow()
            assertEquals(2, favoriteApps.size)
            assertEquals("App1", favoriteApps[0].info.label)
            assertEquals("com.example.app1", favoriteApps[0].info.packageName)
            assertEquals(0, favoriteApps[0].favoriteOrder)
            assertEquals("App2", favoriteApps[1].info.label)
            assertEquals("com.example.app2", favoriteApps[1].info.packageName)
            assertEquals(1, favoriteApps[1].favoriteOrder)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `お気に入りアプリリストの変更が反映されること`() = runTest {
        val initialApps = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App1",
                    packageName = "com.example.app1",
                ),
                favoriteOrder = 0,
            ),
        )
        val updatedApps = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App1",
                    packageName = "com.example.app1",
                ),
                favoriteOrder = 0,
            ),
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App2",
                    packageName = "com.example.app2",
                ),
                favoriteOrder = 1,
            ),
        )
        every { mockRepository.favoriteAppsInfo } returns flowOf(
            Result.success(initialApps),
            Result.success(updatedApps),
        )

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(1, firstResult.getOrThrow().size)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(2, secondResult.getOrThrow().size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `順序が変更されたお気に入りアプリリストを取得できること`() = runTest {
        val favoriteApps = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App2",
                    packageName = "com.example.app2",
                ),
                favoriteOrder = 0,
            ),
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App1",
                    packageName = "com.example.app1",
                ),
                favoriteOrder = 1,
            ),
        )
        every { mockRepository.favoriteAppsInfo } returns flowOf(Result.success(favoriteApps))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val favoriteApps = result.getOrThrow()
            assertEquals(2, favoriteApps.size)
            assertEquals("App2", favoriteApps[0].info.label)
            assertEquals(0, favoriteApps[0].favoriteOrder)
            assertEquals("App1", favoriteApps[1].info.label)
            assertEquals(1, favoriteApps[1].favoriteOrder)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `エラーが発生した場合Result_failureが返されること`() = runTest {
        val exception = RuntimeException("Repository error")
        every { mockRepository.favoriteAppsInfo } returns flowOf(Result.failure(exception))

        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
