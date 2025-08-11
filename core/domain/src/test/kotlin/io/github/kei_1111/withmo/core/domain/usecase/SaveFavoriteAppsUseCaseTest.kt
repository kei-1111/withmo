package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.FavoriteAppRepository
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveFavoriteAppsUseCaseTest {

    private lateinit var mockRepository: FavoriteAppRepository
    private lateinit var useCase: SaveFavoriteAppsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveFavoriteAppsUseCaseImpl(mockRepository)
    }

    @Test
    fun `お気に入りアプリリストを保存できること`() = runTest {
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

        useCase(favoriteApps)

        coVerify { mockRepository.updateFavoriteApps(favoriteApps) }
    }

    @Test
    fun `空のお気に入りアプリリストを保存できること`() = runTest {
        val emptyList = emptyList<FavoriteAppInfo>()

        useCase(emptyList)

        coVerify { mockRepository.updateFavoriteApps(emptyList) }
    }

    @Test
    fun `単一のお気に入りアプリを保存できること`() = runTest {
        val singleApp = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "SingleApp",
                    packageName = "com.example.single",
                ),
                favoriteOrder = 0,
            ),
        )

        useCase(singleApp)

        coVerify { mockRepository.updateFavoriteApps(singleApp) }
    }

    @Test
    fun `順序が変更されたお気に入りアプリリストを保存できること`() = runTest {
        val reorderedApps = listOf(
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

        useCase(reorderedApps)

        coVerify { mockRepository.updateFavoriteApps(reorderedApps) }
    }

    @Test
    fun `最大4個のお気に入りアプリリストを保存できること`() = runTest {
        val maxFavoriteApps = (0..3).map { index ->
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "App$index",
                    packageName = "com.example.app$index",
                ),
                favoriteOrder = index,
            )
        }

        useCase(maxFavoriteApps)

        coVerify { mockRepository.updateFavoriteApps(maxFavoriteApps) }
    }
}
