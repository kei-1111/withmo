package io.github.kei_1111.withmo.feature.setting.screen.clock

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetClockSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveClockSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
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
class ClockSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getClockSettingsUseCase: GetClockSettingsUseCase
    private lateinit var saveClockSettingsUseCase: SaveClockSettingsUseCase
    private lateinit var viewModel: ClockSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getClockSettingsUseCase = mockk()
        saveClockSettingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態で時計設定を取得して表示する`() = runTest {
        val clockSettings = ClockSettings(
            isClockShown = true,
            clockType = ClockType.TOP_DATE,
        )
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(clockSettings))

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ClockSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is ClockSettingsState.Stable)
            val stableState = state as ClockSettingsState.Stable
            assertTrue(stableState.clockSettings.isClockShown)
            assertEquals(ClockType.TOP_DATE, stableState.clockSettings.clockType)
            assertFalse(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsClockShownSwitchChangeアクションで時計表示状態が更新される`() = runTest {
        val initialSettings = ClockSettings(
            isClockShown = false,
            clockType = ClockType.TOP_DATE,
        )
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ClockSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is ClockSettingsState.Stable)
            assertFalse((initialState as ClockSettingsState.Stable).clockSettings.isClockShown)

            viewModel.onAction(ClockSettingsAction.OnIsClockShownSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is ClockSettingsState.Stable)
            val stableState = updatedState as ClockSettingsState.Stable
            assertTrue(stableState.clockSettings.isClockShown)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnClockTypeRadioButtonClickアクションで時計タイプが更新される`() = runTest {
        val initialSettings = ClockSettings(
            isClockShown = true,
            clockType = ClockType.TOP_DATE,
        )
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ClockSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is ClockSettingsState.Stable)
            assertEquals(ClockType.TOP_DATE, (initialState as ClockSettingsState.Stable).clockSettings.clockType)

            viewModel.onAction(ClockSettingsAction.OnClockTypeRadioButtonClick(ClockType.HORIZONTAL_DATE))

            val updatedState = awaitItem()
            assertTrue(updatedState is ClockSettingsState.Stable)
            val stableState = updatedState as ClockSettingsState.Stable
            assertEquals(ClockType.HORIZONTAL_DATE, stableState.clockSettings.clockType)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val clockSettings = ClockSettings(
            isClockShown = true,
            clockType = ClockType.TOP_DATE,
        )
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(clockSettings))
        coEvery { saveClockSettingsUseCase(any()) } returns Unit

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ClockSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is ClockSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as ClockSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(ClockSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveClockSettingsUseCase(clockSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val clockSettings = ClockSettings(
            isClockShown = true,
            clockType = ClockType.TOP_DATE,
        )
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(clockSettings))
        coEvery { saveClockSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ClockSettingsAction.OnSaveButtonClick)

            advanceUntilIdle()

            val effect = awaitItem()
            assertTrue(effect is ClockSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as ClockSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.success(ClockSettings()))

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ClockSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(ClockSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getClockSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = ClockSettingsViewModel(
            getClockSettingsUseCase,
            saveClockSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ClockSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is ClockSettingsState.Error)
            assertEquals(error, (state as ClockSettingsState.Error).error)
        }
    }
}
