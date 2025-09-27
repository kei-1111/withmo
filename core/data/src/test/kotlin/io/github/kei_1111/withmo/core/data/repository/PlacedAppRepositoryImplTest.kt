package io.github.kei_1111.withmo.core.data.repository

import androidx.compose.ui.geometry.Offset
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.data.local.dao.PlacedAppDao
import io.github.kei_1111.withmo.core.data.local.entity.PlacedAppEntity
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
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
class PlacedAppRepositoryImplTest {

    private lateinit var mockPlacedAppDao: PlacedAppDao
    private lateinit var mockAppManager: AppManager
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockPlacedAppDao = mockk(relaxUnitFun = true)
        mockAppManager = mockk()
        testDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `空の配置アプリリストを取得できること`() = runTest(testDispatcher) {
        every { mockPlacedAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList<AppInfo>())

        val repository = PlacedAppRepositoryImpl(
            placedAppDao = mockPlacedAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.placedAppsInfo.test {
            val result = awaitItem()
            assert(result.isSuccess)
            val data = result.getOrThrow()
            assertEquals(0, data.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置アプリリストを取得できること`() = runTest(testDispatcher) {
        val placedAppEntities = listOf(
            PlacedAppEntity("id1", "com.example.app1", 100f, 200f),
            PlacedAppEntity("id2", "com.example.app2", 300f, 400f),
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

        every { mockPlacedAppDao.getAll() } returns flowOf(placedAppEntities)
        every { mockAppManager.appInfoList } returns MutableStateFlow(appInfoList)

        val repository = PlacedAppRepositoryImpl(
            placedAppDao = mockPlacedAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.placedAppsInfo.test {
            val result = awaitItem()
            assert(result.isSuccess)
            val data = result.getOrThrow()
            assertEquals(2, data.size)
            assertEquals("id1", data[0].id)
            assertEquals("com.example.app1", data[0].info.packageName)
            assertEquals("App 1", data[0].info.label)
            assertEquals(Offset(100f, 200f), data[0].position)
            assertEquals("id2", data[1].id)
            assertEquals("com.example.app2", data[1].info.packageName)
            assertEquals("App 2", data[1].info.label)
            assertEquals(Offset(300f, 400f), data[1].position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `アンインストールされたアプリは除外されること`() = runTest(testDispatcher) {
        val placedAppEntities = listOf(
            PlacedAppEntity("id1", "com.example.app1", 100f, 200f),
            PlacedAppEntity("id2", "com.example.uninstalled", 300f, 400f),
        )
        val appInfoList = listOf(
            AppInfo(
                appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                label = "App 1",
                packageName = "com.example.app1",
                useCount = 10,
            ),
        )

        every { mockPlacedAppDao.getAll() } returns flowOf(placedAppEntities)
        every { mockAppManager.appInfoList } returns MutableStateFlow(appInfoList)

        val repository = PlacedAppRepositoryImpl(
            placedAppDao = mockPlacedAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.placedAppsInfo.test {
            val result = awaitItem()
            assert(result.isSuccess)
            val data = result.getOrThrow()
            assertEquals(1, data.size)
            assertEquals("id1", data[0].id)
            assertEquals("com.example.app1", data[0].info.packageName)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置アプリリストを更新できること`() = runTest(testDispatcher) {
        coEvery { mockPlacedAppDao.deleteAll() } returns Unit
        coEvery { mockPlacedAppDao.insertAll(any()) } returns Unit
        every { mockPlacedAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList())

        val repository = PlacedAppRepositoryImpl(
            placedAppDao = mockPlacedAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        val placedAppInfos = listOf(
            PlacedAppInfo(
                id = "id1",
                info = AppInfo(
                    appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                    label = "App 1",
                    packageName = "com.example.app1",
                    useCount = 10,
                ),
                position = Offset(100f, 200f),
            ),
            PlacedAppInfo(
                id = "id2",
                info = AppInfo(
                    appIcon = AppIcon(foregroundIcon = mockk(), backgroundIcon = null),
                    label = "App 2",
                    packageName = "com.example.app2",
                    useCount = 5,
                ),
                position = Offset(300f, 400f),
            ),
        )

        repository.updatePlacedApps(placedAppInfos)

        coVerify { mockPlacedAppDao.deleteAll() }
        coVerify {
            mockPlacedAppDao.insertAll(
                match { entities ->
                    entities.size == 2 &&
                        entities[0].id == "id1" &&
                        entities[0].packageName == "com.example.app1" &&
                        entities[0].positionX == 100f &&
                        entities[0].positionY == 200f &&
                        entities[1].id == "id2" &&
                        entities[1].packageName == "com.example.app2" &&
                        entities[1].positionX == 300f &&
                        entities[1].positionY == 400f
                },
            )
        }
    }

    @Test
    fun `空の配置アプリリストで更新できること`() = runTest(testDispatcher) {
        coEvery { mockPlacedAppDao.deleteAll() } returns Unit
        coEvery { mockPlacedAppDao.insertAll(any()) } returns Unit
        every { mockPlacedAppDao.getAll() } returns flowOf(emptyList())
        every { mockAppManager.appInfoList } returns MutableStateFlow(emptyList())

        val repository = PlacedAppRepositoryImpl(
            placedAppDao = mockPlacedAppDao,
            appManager = mockAppManager,
            ioDispatcher = testDispatcher,
        )

        repository.updatePlacedApps(emptyList())

        coVerify { mockPlacedAppDao.deleteAll() }
        coVerify { mockPlacedAppDao.insertAll(emptyList()) }
    }
}
