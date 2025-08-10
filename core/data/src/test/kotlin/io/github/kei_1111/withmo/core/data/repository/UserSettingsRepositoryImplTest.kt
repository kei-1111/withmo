package io.github.kei_1111.withmo.core.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.emptyPreferences
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserSettingsRepositoryImplTest {

    private lateinit var mockDataStore: DataStore<Preferences>
    private lateinit var testDispatcher: TestDispatcher

    @Before
    fun setup() {
        mockDataStore = mockk()
        testDispatcher = UnconfinedTestDispatcher()
    }

    @Test
    fun `デフォルト設定を取得できること`() = runTest(testDispatcher) {
        every { mockDataStore.data } returns flowOf(emptyPreferences())

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.userSettings.test {
            val result = awaitItem()
            assertEquals(false, result.notificationSettings.isNotificationAnimationEnabled)
            assertEquals(false, result.notificationSettings.isNotificationBadgeEnabled)
            assertEquals(true, result.clockSettings.isClockShown)
            assertEquals(ClockType.TOP_DATE, result.clockSettings.clockType)
            assertEquals(AppIconShape.Circle, result.appIconSettings.appIconShape)
            assertEquals(20f, result.appIconSettings.roundedCornerPercent)
            assertEquals(SortType.ALPHABETICAL, result.sortSettings.sortType)
            assertEquals(true, result.sideButtonSettings.isShowScaleSliderButtonShown)
            assertEquals(ThemeType.TIME_BASED, result.themeSettings.themeType)
            assertEquals(null, result.modelFilePath.path)
            assertEquals(1.0f, result.modelSettings.scale)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `通知設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = true,
        )

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveNotificationSettings(notificationSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `時計設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val clockSettings = ClockSettings(
            isClockShown = false,
            clockType = ClockType.HORIZONTAL_DATE,
        )

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveClockSettings(clockSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `アプリアイコン設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val appIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 15f,
        )

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveAppIconSettings(appIconSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `ソート設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val sortSettings = SortSettings(sortType = SortType.USE_COUNT)

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveSortSettings(sortSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `サイドボタン設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveSideButtonSettings(sideButtonSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `テーマ設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val themeSettings = ThemeSettings(themeType = ThemeType.DARK)

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveThemeSettings(themeSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `モデルファイルパスを保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val modelFilePath = ModelFilePath("/storage/emulated/0/model.vrm")

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveModelFilePath(modelFilePath)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `nullのモデルファイルパスを保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val modelFilePath = ModelFilePath(null)

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveModelFilePath(modelFilePath)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }

    @Test
    fun `モデル設定を保存できること`() = runTest(testDispatcher) {
        val mockDataStore = mockk<DataStore<Preferences>>(relaxed = true)
        val updatedPreferences = emptyPreferences()

        coEvery { mockDataStore.updateData(any()) } returns updatedPreferences

        val modelSettings = ModelSettings(scale = 1.5f)

        val repository = UserSettingsRepositoryImpl(mockDataStore, testDispatcher)

        repository.saveModelSettings(modelSettings)

        coVerify(exactly = 1) { mockDataStore.updateData(any()) }
    }
}
