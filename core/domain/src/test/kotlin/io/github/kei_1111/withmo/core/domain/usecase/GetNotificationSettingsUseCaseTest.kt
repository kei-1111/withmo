package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetNotificationSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetNotificationSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetNotificationSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトの通知設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val notificationSettings = result.getOrThrow()
            assertEquals(false, notificationSettings.isNotificationAnimationEnabled)
            assertEquals(false, notificationSettings.isNotificationBadgeEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更された通知設定を取得できること`() = runTest {
        val customNotificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = true,
        )
        every { mockRepository.userSettings } returns flowOf(
            UserSettings(notificationSettings = customNotificationSettings),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val notificationSettings = result.getOrThrow()
            assertEquals(true, notificationSettings.isNotificationAnimationEnabled)
            assertEquals(true, notificationSettings.isNotificationBadgeEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `通知設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            notificationSettings = NotificationSettings(isNotificationAnimationEnabled = true),
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(false, firstResult.getOrThrow().isNotificationAnimationEnabled)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(true, secondResult.getOrThrow().isNotificationAnimationEnabled)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = NotificationSettings()
        val userSettings = UserSettings(notificationSettings = sameSettings)
        every { mockRepository.userSettings } returns flowOf(userSettings, userSettings, userSettings)

        useCase().test {
            awaitItem()
            awaitComplete()
        }
    }

    @Test
    fun `エラーが発生した場合Result_failureが返されること`() = runTest {
        val exception = RuntimeException("Database error")
        every { mockRepository.userSettings } returns flow { throw exception }

        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
