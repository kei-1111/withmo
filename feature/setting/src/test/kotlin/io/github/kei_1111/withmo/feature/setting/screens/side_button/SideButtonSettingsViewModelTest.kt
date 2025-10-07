package io.github.kei_1111.withmo.feature.setting.screens.side_button

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSideButtonSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
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
class SideButtonSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getSideButtonSettingsUseCase: GetSideButtonSettingsUseCase
    private lateinit var saveSideButtonSettingsUseCase: SaveSideButtonSettingsUseCase
    private lateinit var viewModel: SideButtonSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getSideButtonSettingsUseCase = mockk()
        saveSideButtonSettingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でサイドボタン設定を取得して表示する`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = true,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = false,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(sideButtonSettings))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SideButtonSettingsState.Stable)
            val stableState = state as SideButtonSettingsState.Stable
            assertTrue(stableState.sideButtonSettings.isShowScaleSliderButtonShown)
            assertFalse(stableState.sideButtonSettings.isOpenDocumentButtonShown)
            assertTrue(stableState.sideButtonSettings.isSetDefaultModelButtonShown)
            assertFalse(stableState.sideButtonSettings.isNavigateSettingsButtonShown)
            assertFalse(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsShowScaleSliderButtonShownSwitchChangeアクションで表示スケールスライダーボタンの表示状態が更新される`() = runTest {
        val initialSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SideButtonSettingsState.Stable)
            assertFalse((initialState as SideButtonSettingsState.Stable).sideButtonSettings.isShowScaleSliderButtonShown)

            viewModel.onAction(SideButtonSettingsAction.OnIsShowScaleSliderButtonShownSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is SideButtonSettingsState.Stable)
            val stableState = updatedState as SideButtonSettingsState.Stable
            assertTrue(stableState.sideButtonSettings.isShowScaleSliderButtonShown)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsOpenDocumentButtonShownSwitchChangeアクションでドキュメントを開くボタンの表示状態が更新される`() = runTest {
        val initialSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SideButtonSettingsState.Stable)
            assertFalse((initialState as SideButtonSettingsState.Stable).sideButtonSettings.isOpenDocumentButtonShown)

            viewModel.onAction(SideButtonSettingsAction.OnIsOpenDocumentButtonShownSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is SideButtonSettingsState.Stable)
            val stableState = updatedState as SideButtonSettingsState.Stable
            assertTrue(stableState.sideButtonSettings.isOpenDocumentButtonShown)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsSetDefaultModelButtonShownSwitchChangeアクションでデフォルトモデル設定ボタンの表示状態が更新される`() = runTest {
        val initialSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SideButtonSettingsState.Stable)
            assertFalse((initialState as SideButtonSettingsState.Stable).sideButtonSettings.isSetDefaultModelButtonShown)

            viewModel.onAction(SideButtonSettingsAction.OnIsSetDefaultModelButtonShownSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is SideButtonSettingsState.Stable)
            val stableState = updatedState as SideButtonSettingsState.Stable
            assertTrue(stableState.sideButtonSettings.isSetDefaultModelButtonShown)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsNavigateSettingsButtonShownSwitchChangeアクションで設定画面へ遷移ボタンの表示状態が更新される`() = runTest {
        val initialSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SideButtonSettingsState.Stable)
            assertFalse((initialState as SideButtonSettingsState.Stable).sideButtonSettings.isNavigateSettingsButtonShown)

            viewModel.onAction(SideButtonSettingsAction.OnIsNavigateSettingsButtonShownSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is SideButtonSettingsState.Stable)
            val stableState = updatedState as SideButtonSettingsState.Stable
            assertTrue(stableState.sideButtonSettings.isNavigateSettingsButtonShown)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = true,
            isOpenDocumentButtonShown = true,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = true,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(sideButtonSettings))
        coEvery { saveSideButtonSettingsUseCase(any()) } returns Unit

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SideButtonSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is SideButtonSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as SideButtonSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(SideButtonSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveSideButtonSettingsUseCase(sideButtonSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = true,
            isOpenDocumentButtonShown = true,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = true,
        )
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(sideButtonSettings))
        coEvery { saveSideButtonSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SideButtonSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is SideButtonSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as SideButtonSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.success(SideButtonSettings()))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SideButtonSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(SideButtonSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getSideButtonSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = SideButtonSettingsViewModel(
            getSideButtonSettingsUseCase,
            saveSideButtonSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(SideButtonSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SideButtonSettingsState.Error)
            assertEquals(error, (state as SideButtonSettingsState.Error).error)
        }
    }
}
