package io.github.kei_1111.withmo.feature.setting.screen.sort

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.manager.AppManager
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetSortSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveSortSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
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
class SortSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getSortSettingsUseCase: GetSortSettingsUseCase
    private lateinit var saveSortSettingsUseCase: SaveSortSettingsUseCase
    private lateinit var appManager: AppManager
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var viewModel: SortSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getSortSettingsUseCase = mockk()
        saveSortSettingsUseCase = mockk()
        appManager = mockk()
        permissionChecker = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態でソート設定を取得して表示する`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(sortSettings))

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SortSettingsState.Stable)
            val stableState = state as SortSettingsState.Stable
            assertEquals(SortType.ALPHABETICAL, stableState.sortSettings.sortType)
            assertFalse(stableState.isSaveButtonEnabled)
            assertFalse(stableState.isUsageStatsPermissionDialogVisible)
        }
    }

    @Test
    fun `OnSortTypeRadioButtonClickアクションでソートタイプが更新される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.USE_COUNT)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertEquals(SortType.USE_COUNT, (initialState as SortSettingsState.Stable).sortSettings.sortType)

            viewModel.onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.ALPHABETICAL))

            val updatedState = awaitItem()
            assertTrue(updatedState is SortSettingsState.Stable)
            val stableState = updatedState as SortSettingsState.Stable
            assertEquals(SortType.ALPHABETICAL, stableState.sortSettings.sortType)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSortTypeRadioButtonClickアクションでUSE_COUNTを選択し権限がある場合ソートタイプが更新される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns true
        coEvery { appManager.updateUsageCounts() } returns Unit

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertEquals(SortType.ALPHABETICAL, (initialState as SortSettingsState.Stable).sortSettings.sortType)

            viewModel.onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.USE_COUNT))
            advanceUntilIdle()

            val updatedState = awaitItem()
            assertTrue(updatedState is SortSettingsState.Stable)
            val stableState = updatedState as SortSettingsState.Stable
            assertEquals(SortType.USE_COUNT, stableState.sortSettings.sortType)
            assertTrue(stableState.isSaveButtonEnabled)
            assertFalse(stableState.isUsageStatsPermissionDialogVisible)

            coVerify { appManager.updateUsageCounts() }
        }
    }

    @Test
    fun `OnSortTypeRadioButtonClickアクションでUSE_COUNTを選択し権限がない場合ダイアログが表示される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns false

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertFalse((initialState as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)

            viewModel.onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.USE_COUNT))

            val updatedState = awaitItem()
            assertTrue(updatedState is SortSettingsState.Stable)
            val stableState = updatedState as SortSettingsState.Stable
            assertTrue(stableState.isUsageStatsPermissionDialogVisible)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(sortSettings))
        coEvery { saveSortSettingsUseCase(any()) } returns Unit

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SortSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is SortSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as SortSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(SortSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveSortSettingsUseCase(sortSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(sortSettings))
        coEvery { saveSortSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SortSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is SortSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as SortSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(SortSettings()))

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SortSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(SortSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `OnUsageStatsPermissionDialogConfirmアクションでダイアログが非表示になりRequestUsageStatsPermissionエフェクトが送信される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns false

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertFalse((initialState as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)

            viewModel.onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.USE_COUNT))

            val stateWithDialog = awaitItem()
            assertTrue(stateWithDialog is SortSettingsState.Stable)
            assertTrue((stateWithDialog as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)

            viewModel.effect.test {
                viewModel.onAction(SortSettingsAction.OnUsageStatsPermissionDialogConfirm)

                val effect = awaitItem()
                assertEquals(SortSettingsEffect.RequestUsageStatsPermission, effect)
            }

            val stateAfterConfirm = awaitItem()
            assertTrue(stateAfterConfirm is SortSettingsState.Stable)
            assertFalse((stateAfterConfirm as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)
        }
    }

    @Test
    fun `OnUsageStatsPermissionDialogDismissアクションでダイアログが非表示になる`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns false

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertFalse((initialState as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)

            viewModel.onAction(SortSettingsAction.OnSortTypeRadioButtonClick(SortType.USE_COUNT))

            val stateWithDialog = awaitItem()
            assertTrue(stateWithDialog is SortSettingsState.Stable)
            assertTrue((stateWithDialog as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)

            viewModel.onAction(SortSettingsAction.OnUsageStatsPermissionDialogDismiss)

            val stateAfterDismiss = awaitItem()
            assertTrue(stateAfterDismiss is SortSettingsState.Stable)
            assertFalse((stateAfterDismiss as SortSettingsState.Stable).isUsageStatsPermissionDialogVisible)
        }
    }

    @Test
    fun `OnUsageStatsPermissionResultアクションで権限がある場合ソートタイプが更新され成功トーストが表示される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns true
        coEvery { appManager.updateUsageCounts() } returns Unit

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is SortSettingsState.Stable)
            assertEquals(SortType.ALPHABETICAL, (initialState as SortSettingsState.Stable).sortSettings.sortType)

            viewModel.effect.test {
                viewModel.onAction(SortSettingsAction.OnUsageStatsPermissionResult)
                advanceUntilIdle()

                val effect = awaitItem()
                assertTrue(effect is SortSettingsEffect.ShowToast)
                assertEquals("使用回数取得の権限が許可されました", (effect as SortSettingsEffect.ShowToast).message)
            }

            val updatedState = awaitItem()
            assertTrue(updatedState is SortSettingsState.Stable)
            assertEquals(SortType.USE_COUNT, (updatedState as SortSettingsState.Stable).sortSettings.sortType)
        }

        coVerify { appManager.updateUsageCounts() }
    }

    @Test
    fun `OnUsageStatsPermissionResultアクションで権限がない場合エラートーストが表示される`() = runTest {
        val initialSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.success(initialSettings))
        every { permissionChecker.isUsageStatsPermissionGranted() } returns false

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SortSettingsAction.OnUsageStatsPermissionResult)

            val effect = awaitItem()
            assertTrue(effect is SortSettingsEffect.ShowToast)
            assertEquals("使用回数取得の権限が必要です", (effect as SortSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getSortSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = SortSettingsViewModel(
            getSortSettingsUseCase,
            saveSortSettingsUseCase,
            appManager,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(SortSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is SortSettingsState.Error)
            assertEquals(error, (state as SortSettingsState.Error).error)
        }
    }
}
