package io.github.kei_1111.withmo.feature.setting.screen.theme

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.GetThemeSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveThemeSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
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
class ThemeSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getThemeSettingsUseCase: GetThemeSettingsUseCase
    private lateinit var saveThemeSettingsUseCase: SaveThemeSettingsUseCase
    private lateinit var viewModel: ThemeSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getThemeSettingsUseCase = mockk()
        saveThemeSettingsUseCase = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でテーマ設定を取得して表示する`() = runTest {
        val themeSettings = ThemeSettings(themeType = ThemeType.LIGHT)
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.success(themeSettings))

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ThemeSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is ThemeSettingsState.Stable)
            val stable = state as ThemeSettingsState.Stable
            assertEquals(ThemeType.LIGHT, stable.themeSettings.themeType)
            assertFalse(stable.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnThemeTypeRadioButtonClickアクションでテーマタイプが更新される`() = runTest {
        val initialSettings = ThemeSettings(themeType = ThemeType.LIGHT)
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ThemeSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is ThemeSettingsState.Stable)
            assertEquals(ThemeType.LIGHT, (initialState as ThemeSettingsState.Stable).themeSettings.themeType)

            viewModel.onAction(ThemeSettingsAction.OnThemeTypeRadioButtonClick(ThemeType.DARK))

            val updatedState = awaitItem()
            assertTrue(updatedState is ThemeSettingsState.Stable)
            val stableState = updatedState as ThemeSettingsState.Stable
            assertEquals(ThemeType.DARK, stableState.themeSettings.themeType)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val themeSettings = ThemeSettings(themeType = ThemeType.DARK)
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.success(themeSettings))
        coEvery { saveThemeSettingsUseCase(any()) } returns Unit

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ThemeSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is ThemeSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as ThemeSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(ThemeSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveThemeSettingsUseCase(themeSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val themeSettings = ThemeSettings(themeType = ThemeType.LIGHT)
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.success(themeSettings))
        coEvery { saveThemeSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ThemeSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is ThemeSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as ThemeSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.success(ThemeSettings()))

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(ThemeSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(ThemeSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getThemeSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = ThemeSettingsViewModel(
            getThemeSettingsUseCase,
            saveThemeSettingsUseCase,
        )

        viewModel.state.test {
            assertEquals(ThemeSettingsState.Idle, awaitItem())

            val errorState = awaitItem()
            assertTrue(errorState is ThemeSettingsState.Error)
            assertEquals(error, (errorState as ThemeSettingsState.Error).error)
        }
    }
}
