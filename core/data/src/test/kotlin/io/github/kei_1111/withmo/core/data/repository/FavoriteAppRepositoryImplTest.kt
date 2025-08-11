package io.github.kei_1111.withmo.core.data.repository

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.data.local.dao.FavoriteAppDao
import io.github.kei_1111.withmo.core.data.local.entity.FavoriteAppEntity
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoriteAppRepositoryImplTest {

    private lateinit var mockFavoriteAppDao: FavoriteAppDao
    private lateinit var mockAppManager: AppManager
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockFavoriteAppDao = mockk(relaxUnitFun = true)
        mockAppManager = mockk()
        testDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `空のお気に入りアプリリストを取得できること`() = runTest(testDispatcher) {
        every { mockFavoriteAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList())

        val repository = FavoriteAppRepositoryImpl(
            favoriteAppDao = mockFavoriteAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.favoriteAppsInfo.test {
            val result = awaitItem()
            assertEquals(0, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `お気に入りアプリリストを取得できること`() = runTest(testDispatcher) {
        val favoriteAppEntities = listOf(
            FavoriteAppEntity("com.example.app1", 0),
            FavoriteAppEntity("com.example.app2", 1),
        )
        val appInfoList = listOf(
            AppInfo(
                appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                label = "App 1",
                packageName = "com.example.app1",
                useCount = 10,
            ),
            AppInfo(
                appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                label = "App 2",
                packageName = "com.example.app2",
                useCount = 5,
            ),
            AppInfo(
                appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                label = "App 3",
                packageName = "com.example.app3",
                useCount = 3,
            ),
        )

        every { mockFavoriteAppDao.getAll() } returns flowOf(favoriteAppEntities)
        every { mockAppManager.appInfoList } returns MutableStateFlow(appInfoList)

        val repository = FavoriteAppRepositoryImpl(
            favoriteAppDao = mockFavoriteAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.favoriteAppsInfo.test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("com.example.app1", result[0].info.packageName)
            assertEquals("App 1", result[0].info.label)
            assertEquals(0, result[0].favoriteOrder)
            assertEquals("com.example.app2", result[1].info.packageName)
            assertEquals("App 2", result[1].info.label)
            assertEquals(1, result[1].favoriteOrder)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `アンインストールされたアプリは除外されること`() = runTest(testDispatcher) {
        val favoriteAppEntities = listOf(
            FavoriteAppEntity("com.example.app1", 0),
            FavoriteAppEntity("com.example.uninstalled", 1),
        )
        val appInfoList = listOf(
            AppInfo(
                appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                label = "App 1",
                packageName = "com.example.app1",
                useCount = 10,
            ),
        )

        every { mockFavoriteAppDao.getAll() } returns flowOf(favoriteAppEntities)
        every { mockAppManager.appInfoList } returns MutableStateFlow(appInfoList)

        val repository = FavoriteAppRepositoryImpl(
            favoriteAppDao = mockFavoriteAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.favoriteAppsInfo.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("com.example.app1", result[0].info.packageName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `お気に入りアプリリストを更新できること`() = runTest(testDispatcher) {
        val favoriteAppInfos = listOf(
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                    label = "App 1",
                    packageName = "com.example.app1",
                    useCount = 10,
                ),
                favoriteOrder = 0,
            ),
            FavoriteAppInfo(
                info = AppInfo(
                    appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                    label = "App 2",
                    packageName = "com.example.app2",
                    useCount = 5,
                ),
                favoriteOrder = 1,
            ),
        )

        coEvery { mockFavoriteAppDao.deleteAll() } returns Unit
        coEvery { mockFavoriteAppDao.insertAll(any()) } returns Unit
        every { mockFavoriteAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList())

        val repository = FavoriteAppRepositoryImpl(
            favoriteAppDao = mockFavoriteAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.updateFavoriteApps(favoriteAppInfos)

        coVerify { mockFavoriteAppDao.deleteAll() }
        coVerify {
            mockFavoriteAppDao.insertAll(
                match { entities ->
                    entities.size == 2 &&
                        entities[0].packageName == "com.example.app1" &&
                        entities[0].favoriteOrder == 0 &&
                        entities[1].packageName == "com.example.app2" &&
                        entities[1].favoriteOrder == 1
                },
            )
        }
    }

    @Test
    fun `空のお気に入りアプリリストで更新できること`() = runTest(testDispatcher) {
        coEvery { mockFavoriteAppDao.deleteAll() } returns Unit
        coEvery { mockFavoriteAppDao.insertAll(any()) } returns Unit
        every { mockFavoriteAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList())

        val repository = FavoriteAppRepositoryImpl(
            favoriteAppDao = mockFavoriteAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.updateFavoriteApps(emptyList())

        coVerify { mockFavoriteAppDao.deleteAll() }
        coVerify { mockFavoriteAppDao.insertAll(emptyList()) }
    }
}
