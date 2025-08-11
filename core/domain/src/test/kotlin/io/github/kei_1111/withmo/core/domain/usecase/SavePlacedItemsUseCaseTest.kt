package io.github.kei_1111.withmo.core.domain.usecase

import android.appwidget.AppWidgetProviderInfo
import androidx.compose.ui.geometry.Offset
import io.github.kei_1111.withmo.core.domain.repository.PlacedAppRepository
import io.github.kei_1111.withmo.core.domain.repository.PlacedWidgetRepository
import io.github.kei_1111.withmo.core.model.AppIcon
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.PlaceableItem
import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import io.github.kei_1111.withmo.core.model.PlacedWidgetInfo
import io.github.kei_1111.withmo.core.model.WidgetInfo
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SavePlacedItemsUseCaseTest {

    private lateinit var mockPlacedAppRepository: PlacedAppRepository
    private lateinit var mockPlacedWidgetRepository: PlacedWidgetRepository
    private lateinit var useCase: SavePlacedItemsUseCase

    @Before
    fun setup() {
        mockPlacedAppRepository = mockk(relaxed = true)
        mockPlacedWidgetRepository = mockk(relaxed = true)
        useCase = SavePlacedItemsUseCaseImpl(mockPlacedAppRepository, mockPlacedWidgetRepository)
    }

    @Test
    fun `空の配置アイテムリストを保存できること`() = runTest {
        val emptyList = emptyList<PlaceableItem>()

        useCase(emptyList)

        coVerify { mockPlacedAppRepository.updatePlacedApps(emptyList()) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(emptyList()) }
    }

    @Test
    fun `配置されたアプリのみを保存できること`() = runTest {
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

        useCase(placedApps)

        coVerify { mockPlacedAppRepository.updatePlacedApps(placedApps) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(emptyList()) }
    }

    @Test
    fun `配置されたウィジェットのみを保存できること`() = runTest {
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

        useCase(placedWidgets)

        coVerify { mockPlacedAppRepository.updatePlacedApps(emptyList()) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(placedWidgets) }
    }

    @Test
    fun `配置されたアプリとウィジェットを両方保存できること`() = runTest {
        val placedApp = PlacedAppInfo(
            id = "app1",
            position = Offset(100f, 200f),
            info = AppInfo(
                appIcon = mockk<AppIcon>(),
                label = "TestApp",
                packageName = "com.example.testapp",
            ),
        )
        val placedWidget = PlacedWidgetInfo(
            info = WidgetInfo(
                id = 123,
                info = mockk<AppWidgetProviderInfo>(),
            ),
            width = 200,
            height = 100,
            position = Offset(50f, 150f),
        )
        val mixedItems = listOf<PlaceableItem>(placedApp, placedWidget)

        useCase(mixedItems)

        coVerify { mockPlacedAppRepository.updatePlacedApps(listOf(placedApp)) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(listOf(placedWidget)) }
    }

    @Test
    fun `複数の配置アプリを保存できること`() = runTest {
        val placedApps = listOf(
            PlacedAppInfo(
                id = "app1",
                position = Offset(100f, 200f),
                info = AppInfo(
                    appIcon = mockk<AppIcon>(),
                    label = "TestApp1",
                    packageName = "com.example.testapp1",
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

        useCase(placedApps)

        coVerify { mockPlacedAppRepository.updatePlacedApps(placedApps) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(emptyList()) }
    }

    @Test
    fun `複数の配置ウィジェットを保存できること`() = runTest {
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
            PlacedWidgetInfo(
                info = WidgetInfo(
                    id = 456,
                    info = mockk<AppWidgetProviderInfo>(),
                ),
                width = 300,
                height = 150,
                position = Offset(250f, 350f),
            ),
        )

        useCase(placedWidgets)

        coVerify { mockPlacedAppRepository.updatePlacedApps(emptyList()) }
        coVerify { mockPlacedWidgetRepository.updatePlacedWidgets(placedWidgets) }
    }
}
