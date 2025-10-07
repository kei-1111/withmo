package io.github.kei_1111.withmo.feature.setting.screens.app_icon

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveAppIconSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.unmockkStatic
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
class AppIconSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getAppIconSettingsUseCase: GetAppIconSettingsUseCase
    private lateinit var saveAppIconSettingsUseCase: SaveAppIconSettingsUseCase
    private lateinit var viewModel: AppIconSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getAppIconSettingsUseCase = mockk()
        saveAppIconSettingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でアプリアイコン設定を取得して表示する`() = runTest {
        val appIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 20f,
        )
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(appIconSettings))

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(AppIconSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is AppIconSettingsState.Stable)
            val stableState = state as AppIconSettingsState.Stable
            assertEquals(AppIconShape.RoundedCorner, stableState.appIconSettings.appIconShape)
            assertEquals(20f, stableState.appIconSettings.roundedCornerPercent)
            assertFalse(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnAppIconShapeRadioButtonClickアクションでアイコン形状が更新される`() = runTest {
        val initialSettings = AppIconSettings(
            appIconShape = AppIconShape.Circle,
            roundedCornerPercent = 20f,
        )
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(AppIconSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is AppIconSettingsState.Stable)
            assertEquals(AppIconShape.Circle, (initialState as AppIconSettingsState.Stable).appIconSettings.appIconShape)

            viewModel.onAction(AppIconSettingsAction.OnAppIconShapeRadioButtonClick(AppIconShape.RoundedCorner))

            val updatedState = awaitItem()
            assertTrue(updatedState is AppIconSettingsState.Stable)
            val stableState = updatedState as AppIconSettingsState.Stable
            assertEquals(AppIconShape.RoundedCorner, stableState.appIconSettings.appIconShape)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnRoundedCornerPercentSliderChangeアクションで角丸パーセントが更新される`() = runTest {
        val initialSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 20f,
        )
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(AppIconSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is AppIconSettingsState.Stable)
            assertEquals(20f, (initialState as AppIconSettingsState.Stable).appIconSettings.roundedCornerPercent)

            viewModel.onAction(AppIconSettingsAction.OnRoundedCornerPercentSliderChange(30f))

            val updatedState = awaitItem()
            assertTrue(updatedState is AppIconSettingsState.Stable)
            val stableState = updatedState as AppIconSettingsState.Stable
            assertEquals(30f, stableState.appIconSettings.roundedCornerPercent)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val appIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 20f,
        )
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(appIconSettings))
        coEvery { saveAppIconSettingsUseCase(any()) } returns Unit

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(AppIconSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is AppIconSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as AppIconSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(AppIconSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveAppIconSettingsUseCase(appIconSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val appIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 20f,
        )
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(appIconSettings))
        coEvery { saveAppIconSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(AppIconSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is AppIconSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as AppIconSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.success(AppIconSettings()))

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(AppIconSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(AppIconSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getAppIconSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = AppIconSettingsViewModel(
            getAppIconSettingsUseCase,
            saveAppIconSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(AppIconSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is AppIconSettingsState.Error)
            assertEquals(error, (state as AppIconSettingsState.Error).error)
        }
    }
}
