package io.github.kei_1111.withmo.feature.setting.screen.notification

import android.util.Log
import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.permission.PermissionChecker
import io.github.kei_1111.withmo.core.domain.usecase.GetNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.domain.usecase.SaveNotificationSettingsUseCase
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
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
class NotificationSettingsViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var getNotificationSettingsUseCase: GetNotificationSettingsUseCase
    private lateinit var saveNotificationSettingsUseCase: SaveNotificationSettingsUseCase
    private lateinit var permissionChecker: PermissionChecker
    private lateinit var viewModel: NotificationSettingsViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        mockkStatic(Log::class)
        every { Log.e(any(), any(), any()) } returns 0
        getNotificationSettingsUseCase = mockk()
        saveNotificationSettingsUseCase = mockk()
        permissionChecker = mockk()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        unmockkStatic(Log::class)
    }

    @Test
    fun `初期状態で通知設定を取得して表示する`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = false,
        )
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(notificationSettings))

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(NotificationSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is NotificationSettingsState.Stable)
            val stableState = state as NotificationSettingsState.Stable
            assertTrue(stableState.notificationSettings.isNotificationAnimationEnabled)
            assertFalse(stableState.notificationSettings.isNotificationBadgeEnabled)
            assertFalse(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsNotificationAnimationEnabledSwitchChangeアクションで通知アニメーション有効状態が更新される`() = runTest {
        val initialSettings = NotificationSettings(
            isNotificationAnimationEnabled = false,
            isNotificationBadgeEnabled = false,
        )
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(NotificationSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is NotificationSettingsState.Stable)
            assertFalse((initialState as NotificationSettingsState.Stable).notificationSettings.isNotificationAnimationEnabled)

            viewModel.onAction(NotificationSettingsAction.OnIsNotificationAnimationEnabledSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is NotificationSettingsState.Stable)
            val stableState = updatedState as NotificationSettingsState.Stable
            assertTrue(stableState.notificationSettings.isNotificationAnimationEnabled)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnIsNotificationBadgeEnabledSwitchChangeアクションで通知バッジ有効状態が更新される`() = runTest {
        val initialSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = false,
        )
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(initialSettings))

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(NotificationSettingsState.Idle, awaitItem())

            val initialState = awaitItem()
            assertTrue(initialState is NotificationSettingsState.Stable)
            assertFalse((initialState as NotificationSettingsState.Stable).notificationSettings.isNotificationBadgeEnabled)

            viewModel.onAction(NotificationSettingsAction.OnIsNotificationBadgeEnabledSwitchChange(true))

            val updatedState = awaitItem()
            assertTrue(updatedState is NotificationSettingsState.Stable)
            val stableState = updatedState as NotificationSettingsState.Stable
            assertTrue(stableState.notificationSettings.isNotificationBadgeEnabled)
            assertTrue(stableState.isSaveButtonEnabled)
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで設定が保存されNavigateBackエフェクトが送信される`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = true,
        )
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(notificationSettings))
        coEvery { saveNotificationSettingsUseCase(any()) } returns Unit

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(NotificationSettingsAction.OnSaveButtonClick)

            val toastEffect = awaitItem()
            assertTrue(toastEffect is NotificationSettingsEffect.ShowToast)
            assertEquals("保存しました", (toastEffect as NotificationSettingsEffect.ShowToast).message)

            val navigateEffect = awaitItem()
            assertEquals(NotificationSettingsEffect.NavigateBack, navigateEffect)

            advanceUntilIdle()

            coVerify { saveNotificationSettingsUseCase(notificationSettings) }
        }
    }

    @Test
    fun `OnSaveButtonClickアクションで保存が失敗した場合エラートーストが表示される`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = true,
        )
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(notificationSettings))
        coEvery { saveNotificationSettingsUseCase(any()) } throws Exception("Save failed")

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(NotificationSettingsAction.OnSaveButtonClick)

            val effect = awaitItem()
            assertTrue(effect is NotificationSettingsEffect.ShowToast)
            assertEquals("保存に失敗しました", (effect as NotificationSettingsEffect.ShowToast).message)
        }
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(NotificationSettings()))

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(NotificationSettingsAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(NotificationSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `OnCheckPermissionOnResumeアクションで権限がない場合NavigateBackエフェクトが送信される`() = runTest {
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(NotificationSettings()))
        every { permissionChecker.isNotificationListenerEnabled() } returns false

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(NotificationSettingsAction.OnCheckPermissionOnResume)

            val effect = awaitItem()
            assertEquals(NotificationSettingsEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `OnCheckPermissionOnResumeアクションで権限がある場合エフェクトは送信されない`() = runTest {
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.success(NotificationSettings()))
        every { permissionChecker.isNotificationListenerEnabled() } returns true

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )
        advanceUntilIdle()

        viewModel.effect.test {
            viewModel.onAction(NotificationSettingsAction.OnCheckPermissionOnResume)

            expectNoEvents()
        }
    }

    @Test
    fun `初期化時にエラーが発生した場合エラー状態になる`() = runTest {
        val error = Exception("Failed to load settings")
        coEvery { getNotificationSettingsUseCase() } returns flowOf(Result.failure(error))

        viewModel = NotificationSettingsViewModel(
            getNotificationSettingsUseCase,
            saveNotificationSettingsUseCase,
            permissionChecker,
        )

        viewModel.state.test {
            assertEquals(NotificationSettingsState.Idle, awaitItem())

            val state = awaitItem()
            assertTrue(state is NotificationSettingsState.Error)
            assertEquals(error, (state as NotificationSettingsState.Error).error)
        }
    }
}
