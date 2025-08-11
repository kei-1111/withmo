package io.github.kei_1111.withmo.core.domain.usecase

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetPlacedItemsUseCaseTest {

    private lateinit var mockPlacedAppRepository: PlacedAppRepository
    private lateinit var mockPlacedWidgetRepository: PlacedWidgetRepository
    private lateinit var useCase: GetPlacedItemsUseCase

    @Before
    fun setup() {
        mockPlacedAppRepository = mockk()
        mockPlacedWidgetRepository = mockk()
        useCase = GetPlacedItemsUseCaseImpl(mockPlacedAppRepository, mockPlacedWidgetRepository)
    }

    @Test
    fun `空の配置アイテムリストを取得できること`() = runTest {
        every { mockPlacedAppRepository.placedAppsInfo } returns flowOf(emptyList())
        every { mockPlacedWidgetRepository.placedWidgetsInfo } returns flowOf(emptyList())

        useCase().test {
            val result = awaitItem()
            assertEquals(0, result.size)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置されたアプリのみを取得できること`() = runTest {
        val placedApps = listOf(
            PlacedAppInfo(
                id = "app1",
                position = Offset(100f, 200f),
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "TestApp",
                    packageName = "com.example.testapp",
                ),
            ),
        )
        every { mockPlacedAppRepository.placedAppsInfo } returns flowOf(placedApps)
        every { mockPlacedWidgetRepository.placedWidgetsInfo } returns flowOf(emptyList())

        useCase().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("app1", result[0].id)
            assertEquals(Offset(100f, 200f), result[0].position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置されたウィジェットのみを取得できること`() = runTest {
        val placedWidgets = listOf(
            PlacedWidgetInfo(
                info = WidgetInfo(
                    id = 123,
                    info = mockk<AppWidgetProviderInfo>(),
                ),
                width = 200,
                height = 100,
                position = Offset(50f, 150f),
            ),
        )
        every { mockPlacedAppRepository.placedAppsInfo } returns flowOf(emptyList())
        every { mockPlacedWidgetRepository.placedWidgetsInfo } returns flowOf(placedWidgets)

        useCase().test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("123", result[0].id)
            assertEquals(Offset(50f, 150f), result[0].position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `配置されたアプリとウィジェットを両方取得できること`() = runTest {
        val placedApps = listOf(
            PlacedAppInfo(
                id = "app1",
                position = Offset(100f, 200f),
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "TestApp",
                    packageName = "com.example.testapp",
                ),
            ),
        )
        val placedWidgets = listOf(
            PlacedWidgetInfo(
                info = WidgetInfo(
                    id = 123,
                    info = mockk<AppWidgetProviderInfo>(),
                ),
                width = 200,
                height = 100,
                position = Offset(50f, 150f),
            ),
        )
        every { mockPlacedAppRepository.placedAppsInfo } returns flowOf(placedApps)
        every { mockPlacedWidgetRepository.placedWidgetsInfo } returns flowOf(placedWidgets)

        useCase().test {
            val result = awaitItem()
            assertEquals(2, result.size)

            // ウィジェットが先に来る（combine の順序）
            assertEquals("123", result[0].id)
            assertEquals(Offset(50f, 150f), result[0].position)

            assertEquals("app1", result[1].id)
            assertEquals(Offset(100f, 200f), result[1].position)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Suppress("LongMethod")
    @Test
    fun `配置アイテムの変更が反映されること`() = runTest {
        val appsFlow = MutableStateFlow(
            listOf(
                PlacedAppInfo(
                    id = "app1",
                    position = Offset(100f, 200f),
                    info = AppInfo(
                        appIcon = mockk<AppIcon>(),
                        label = "TestApp",
                        packageName = "com.example.testapp",
                    ),
                ),
            ),
        )
        val widgetsFlow = MutableStateFlow(emptyList<PlacedWidgetInfo>())

        every { mockPlacedAppRepository.placedAppsInfo } returns appsFlow
        every { mockPlacedWidgetRepository.placedWidgetsInfo } returns widgetsFlow

        useCase().test {
            val initial = awaitItem()
            assertEquals(1, initial.size)
            assertEquals("app1", initial[0].id)
            assertEquals(Offset(100f, 200f), initial[0].position)

            widgetsFlow.value = listOf(
                PlacedWidgetInfo(
                    info = WidgetInfo(
                        id = 456,
                        info = mockk<AppWidgetProviderInfo>(),
                    ),
                    width = 150,
                    height = 75,
                    position = Offset(200f, 300f),
                ),
            )

            val intermediate = awaitItem()
            assertEquals(2, intermediate.size)
            assertEquals("456", intermediate[0].id)
            assertEquals("app1", intermediate[1].id)

            appsFlow.value = listOf(
                PlacedAppInfo(
                    id = "app1",
                    position = Offset(150f, 250f),
                    info = AppInfo(
                        appIcon = mockk<AppIcon>(),
                        label = "TestApp",
                        packageName = "com.example.testapp",
                    ),
                ),
                PlacedAppInfo(
                    id = "app2",
                    position = Offset(300f, 400f),
                    info = AppInfo(
                        appIcon = mockk<AppIcon>(),
                        label = "TestApp2",
                        packageName = "com.example.testapp2",
                    ),
                ),
            )

            val updated = awaitItem()
            assertEquals(3, updated.size)

            assertEquals("456", updated[0].id)
            assertEquals(Offset(200f, 300f), updated[0].position)

            assertEquals("app1", updated[1].id)
            assertEquals(Offset(150f, 250f), updated[1].position)
            assertEquals("app2", updated[2].id)
            assertEquals(Offset(300f, 400f), updated[2].position)
            cancelAndIgnoreRemainingEvents()
        }
    }
}
