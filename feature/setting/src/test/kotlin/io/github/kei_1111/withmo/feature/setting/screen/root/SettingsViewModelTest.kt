package io.github.kei_1111.withmo.feature.setting.screen.root

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class SettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var viewModel: SettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        permissionChecker = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `初期状態がStableである`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)

        viewModel.state.test {
            val state = awaitItem()
            assertTrue(state is SettingsState.Stable)
            val stableState = state as SettingsState.Stable
            assertTrue(stableState.isDefaultHomeApp)
            assertFalse(stableState.isNotificationPermissionDialogVisible)
        }
    }

    @Test
    fun `OnSettingsScreenLifecycleChangedアクションでisDefaultHomeAppが更新される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState is SettingsState.Stable)
            assertTrue((initialState as SettingsState.Stable).isDefaultHomeApp)

            viewModel.onAction(SettingsAction.OnSettingsScreenLifecycleChanged(isDefaultHomeApp = false))

            val updatedState = awaitItem()
            assertTrue(updatedState is SettingsState.Stable)
            assertFalse((updatedState as SettingsState.Stable).isDefaultHomeApp)
        }
    }

    @Test
    fun `OnNavigateHomeAppSettingButtonClickアクションでOpenHomeAppSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateHomeAppSettingButtonClick)

            val effect = awaitItem()
            assertTrue(effect is SettingsEffect.OpenHomeAppSettings)
        }
    }

    @Test
    fun `OnNavigateClockSettingsButtonClickアクションでNavigateClockSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateClockSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateClockSettings, effect)
        }
    }

    @Test
    fun `OnNavigateAppIconSettingsButtonClickアクションでNavigateAppIconSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateAppIconSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateAppIconSettings, effect)
        }
    }

    @Test
    fun `OnNavigateFavoriteAppSettingsButtonClickアクションでNavigateFavoriteAppSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateFavoriteAppSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateFavoriteAppSettings, effect)
        }
    }

    @Test
    fun `OnNavigateSideButtonSettingsButtonClickアクションでNavigateSideButtonSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateSideButtonSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateSideButtonSettings, effect)
        }
    }

    @Test
    fun `OnNavigateSortSettingsButtonClickアクションでNavigateSortSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateSortSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateSortSettings, effect)
        }
    }

    @Test
    fun `OnNavigateNotificationSettingsButtonClickアクションで権限がある場合NavigateNotificationSettingsエフェクトが送信される`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns true

        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateNotificationSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateNotificationSettings, effect)
        }
    }

    @Test
    fun `OnNavigateNotificationSettingsButtonClickアクションで権限がない場合ダイアログが表示される`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns false

        viewModel = SettingsViewModel(permissionChecker)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState is SettingsState.Stable)
            assertFalse((initialState as SettingsState.Stable).isNotificationPermissionDialogVisible)

            viewModel.onAction(SettingsAction.OnNavigateNotificationSettingsButtonClick)

            val updatedState = awaitItem()
            assertTrue(updatedState is SettingsState.Stable)
            assertTrue((updatedState as SettingsState.Stable).isNotificationPermissionDialogVisible)
        }
    }

    @Test
    fun `OnNavigateWallpaperSettingsButtonClickアクションでOpenWallpaperSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateWallpaperSettingsButtonClick)

            val effect = awaitItem()
            assertTrue(effect is SettingsEffect.OpenWallpaperSettings)
        }
    }

    @Test
    fun `OnNavigateThemeSettingsButtonClickアクションでNavigateThemeSettingsエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNavigateThemeSettingsButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateThemeSettings, effect)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `OnNotificationPermissionDialogConfirmアクションでダイアログが非表示になりRequestNotificationListenerPermissionエフェクトが送信される`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns false

        viewModel = SettingsViewModel(permissionChecker)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState is SettingsState.Stable)
            assertFalse((initialState as SettingsState.Stable).isNotificationPermissionDialogVisible)

            viewModel.onAction(SettingsAction.OnNavigateNotificationSettingsButtonClick)

            val stateWithDialog = awaitItem()
            assertTrue(stateWithDialog is SettingsState.Stable)
            assertTrue((stateWithDialog as SettingsState.Stable).isNotificationPermissionDialogVisible)

            viewModel.effect.test {
                viewModel.onAction(SettingsAction.OnNotificationPermissionDialogConfirm)

                val effect = awaitItem()
                assertTrue(effect is SettingsEffect.RequestNotificationListenerPermission)
            }

            val stateAfterConfirm = awaitItem()
            assertTrue(stateAfterConfirm is SettingsState.Stable)
            assertFalse((stateAfterConfirm as SettingsState.Stable).isNotificationPermissionDialogVisible)
        }
    }

    @Test
    fun `OnNotificationPermissionDialogDismissアクションでダイアログが非表示になる`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns false

        viewModel = SettingsViewModel(permissionChecker)

        viewModel.state.test {
            val initialState = awaitItem()
            assertTrue(initialState is SettingsState.Stable)
            assertFalse((initialState as SettingsState.Stable).isNotificationPermissionDialogVisible)

            viewModel.onAction(SettingsAction.OnNavigateNotificationSettingsButtonClick)

            val stateWithDialog = awaitItem()
            assertTrue(stateWithDialog is SettingsState.Stable)
            assertTrue((stateWithDialog as SettingsState.Stable).isNotificationPermissionDialogVisible)

            viewModel.onAction(SettingsAction.OnNotificationPermissionDialogDismiss)

            val stateAfterDismiss = awaitItem()
            assertTrue(stateAfterDismiss is SettingsState.Stable)
            assertFalse((stateAfterDismiss as SettingsState.Stable).isNotificationPermissionDialogVisible)
        }
    }

    @Test
    fun `OnNotificationListenerPermissionResultアクションで権限がある場合NavigateNotificationSettingsエフェクトが送信される`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns true

        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNotificationListenerPermissionResult)

            val effect = awaitItem()
            assertEquals(SettingsEffect.NavigateNotificationSettings, effect)
        }
    }

    @Test
    fun `OnNotificationListenerPermissionResultアクションで権限がない場合ShowToastエフェクトが送信される`() = runTest {
        every { permissionChecker.isNotificationListenerEnabled() } returns false

        viewModel = SettingsViewModel(permissionChecker)
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(SettingsAction.OnNotificationListenerPermissionResult)

            val effect = awaitItem()
            assertTrue(effect is SettingsEffect.ShowToast)
            assertEquals("通知機能を使用するには\n通知アクセスを許可してください", (effect as SettingsEffect.ShowToast).message)
        }
    }
}
