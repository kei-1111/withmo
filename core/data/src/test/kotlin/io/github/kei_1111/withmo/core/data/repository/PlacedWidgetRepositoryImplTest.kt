package io.github.kei_1111.withmo.core.data.repository

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo
import android.util.Log
import androidx.compose.ui.geometry.Offset
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.data.local.dao.PlacedWidgetDao
import io.github.kei_1111.withmo.core.data.local.entity.PlacedWidgetEntity
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PlacedWidgetRepositoryImplTest {

    private lateinit var mockPlacedWidgetDao: PlacedWidgetDao
    private lateinit var mockAppWidgetManager: AppWidgetManager
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockPlacedWidgetDao = mockk()
        mockAppWidgetManager = mockk(relaxed = true)
        testDispatcher = UnconfinedTestDispatcher()
        
        mockkStatic(Log::class)
        every { Log.e(any(), any()) } returns 0
    }
    
    @After
    fun tearDown() {
        unmockkStatic(Log::class)
    }

    @Test
    fun `空の配置ウィジェットリストを取得できること`() = runTest(testDispatcher) {
        every { mockPlacedWidgetDao.getAll() } returns flowOf(emptyList())
        every { mockAppWidgetManager.installedProviders } returns emptyList()
        
        val repository = PlacedWidgetRepositoryImpl(
            placedWidgetDao = mockPlacedWidgetDao,
            appWidgetManager = mockAppWidgetManager,
            ioDispatcher = testDispatcher
        )

        repository.placedWidgetsInfo.test {
            val result = awaitItem()
            assertEquals(0, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置ウィジェットリストを取得できること`() = runTest(testDispatcher) {
        val placedWidgetEntities = listOf(
            PlacedWidgetEntity(
                id = 1,
                appWidgetProviderClassName = "com.example.widget.ExampleWidget",
                width = 4,
                height = 2,
                positionX = 100f,
                positionY = 200f
            ),
            PlacedWidgetEntity(
                id = 2,
                appWidgetProviderClassName = "com.example.widget.AnotherWidget",
                width = 2,
                height = 1,
                positionX = 300f,
                positionY = 400f
            )
        )

        val mockProvider1 = mockk<android.content.ComponentName>(relaxed = true)
        every { mockProvider1.className } returns "com.example.widget.ExampleWidget"
        
        val mockProvider2 = mockk<android.content.ComponentName>(relaxed = true)
        every { mockProvider2.className } returns "com.example.widget.AnotherWidget"
        
        val mockProviderInfo1 = mockk<AppWidgetProviderInfo>(relaxed = true)
        mockProviderInfo1.provider = mockProvider1
        
        val mockProviderInfo2 = mockk<AppWidgetProviderInfo>(relaxed = true)
        mockProviderInfo2.provider = mockProvider2
        
        every { mockPlacedWidgetDao.getAll() } returns flowOf(placedWidgetEntities)
        every { mockAppWidgetManager.installedProviders } returns listOf(mockProviderInfo1, mockProviderInfo2)
        
        val repository = PlacedWidgetRepositoryImpl(
            placedWidgetDao = mockPlacedWidgetDao,
            appWidgetManager = mockAppWidgetManager,
            ioDispatcher = testDispatcher
        )

        repository.placedWidgetsInfo.test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("1", result[0].id)
            assertEquals(1, result[0].info.id)
            assertEquals(4, result[0].width)
            assertEquals(2, result[0].height)
            assertEquals(Offset(100f, 200f), result[0].position)
            assertEquals("2", result[1].id)
            assertEquals(2, result[1].info.id)
            assertEquals(2, result[1].width)
            assertEquals(1, result[1].height)
            assertEquals(Offset(300f, 400f), result[1].position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `削除されたウィジェットは除外されること`() = runTest(testDispatcher) {
        val placedWidgetEntities = listOf(
            PlacedWidgetEntity(
                id = 1,
                appWidgetProviderClassName = "com.example.widget.ExampleWidget",
                width = 4,
                height = 2,
                positionX = 100f,
                positionY = 200f
            ),
            PlacedWidgetEntity(
                id = 2,
                appWidgetProviderClassName = "com.example.widget.DeletedWidget",
                width = 2,
                height = 1,
                positionX = 300f,
                positionY = 400f
            )
        )

        val mockProvider1 = mockk<android.content.ComponentName>(relaxed = true)
        every { mockProvider1.className } returns "com.example.widget.ExampleWidget"
        
        val mockProviderInfo1 = mockk<AppWidgetProviderInfo>(relaxed = true)
        mockProviderInfo1.provider = mockProvider1
        
        every { mockPlacedWidgetDao.getAll() } returns flowOf(placedWidgetEntities)
        every { mockAppWidgetManager.installedProviders } returns listOf(mockProviderInfo1)
        
        val repository = PlacedWidgetRepositoryImpl(
            placedWidgetDao = mockPlacedWidgetDao,
            appWidgetManager = mockAppWidgetManager,
            ioDispatcher = testDispatcher
        )

        repository.placedWidgetsInfo.test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("1", result[0].id)
            assertEquals(1, result[0].info.id)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置ウィジェットリストを更新できること`() = runTest(testDispatcher) {
        val mockProvider1 = mockk<android.content.ComponentName>(relaxed = true)
        every { mockProvider1.className } returns "com.example.widget.ExampleWidget"
        
        val mockProvider2 = mockk<android.content.ComponentName>(relaxed = true)
        every { mockProvider2.className } returns "com.example.widget.AnotherWidget"
        
        val mockProviderInfo1 = mockk<AppWidgetProviderInfo>(relaxed = true)
        mockProviderInfo1.provider = mockProvider1
        
        val mockProviderInfo2 = mockk<AppWidgetProviderInfo>(relaxed = true)
        mockProviderInfo2.provider = mockProvider2
        
        val placedWidgetInfos = listOf(
            PlacedWidgetInfo(
                info = WidgetInfo(
                    id = 1,
                    info = mockProviderInfo1
                ),
                width = 4,
                height = 2,
                position = Offset(100f, 200f)
            ),
            PlacedWidgetInfo(
                info = WidgetInfo(
                    id = 2,
                    info = mockProviderInfo2
                ),
                width = 2,
                height = 1,
                position = Offset(300f, 400f)
            )
        )

        every { mockPlacedWidgetDao.getAll() } returns flowOf(emptyList())
        coEvery { mockPlacedWidgetDao.deleteAll() } returns Unit
        coEvery { mockPlacedWidgetDao.insertAll(any()) } returns Unit

        val repository = PlacedWidgetRepositoryImpl(
            placedWidgetDao = mockPlacedWidgetDao,
            appWidgetManager = mockAppWidgetManager,
            ioDispatcher = testDispatcher
        )

        repository.updatePlacedWidgets(placedWidgetInfos)

        coVerify { mockPlacedWidgetDao.deleteAll() }
        coVerify { 
            mockPlacedWidgetDao.insertAll(
                match { entities ->
                    entities.size == 2 &&
                    entities[0].id == 1 &&
                    entities[0].appWidgetProviderClassName == "com.example.widget.ExampleWidget" &&
                    entities[0].width == 4 &&
                    entities[0].height == 2 &&
                    entities[0].positionX == 100f &&
                    entities[0].positionY == 200f &&
                    entities[1].id == 2 &&
                    entities[1].appWidgetProviderClassName == "com.example.widget.AnotherWidget" &&
                    entities[1].width == 2 &&
                    entities[1].height == 1 &&
                    entities[1].positionX == 300f &&
                    entities[1].positionY == 400f
                }
            )
        }
    }

    @Test
    fun `空の配置ウィジェットリストで更新できること`() = runTest(testDispatcher) {
        every { mockPlacedWidgetDao.getAll() } returns flowOf(emptyList())
        coEvery { mockPlacedWidgetDao.deleteAll() } returns Unit
        coEvery { mockPlacedWidgetDao.insertAll(any()) } returns Unit

        val repository = PlacedWidgetRepositoryImpl(
            placedWidgetDao = mockPlacedWidgetDao,
            appWidgetManager = mockAppWidgetManager,
            ioDispatcher = testDispatcher
        )

        repository.updatePlacedWidgets(emptyList())

        coVerify { mockPlacedWidgetDao.deleteAll() }
        coVerify { mockPlacedWidgetDao.insertAll(emptyList()) }
    }
}