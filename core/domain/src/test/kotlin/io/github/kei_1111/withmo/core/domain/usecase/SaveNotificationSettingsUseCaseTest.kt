package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.NotificationSettings
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveNotificationSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveNotificationSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveNotificationSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `通知設定を保存できること`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = true,
        )

        useCase(notificationSettings)

        coVerify { mockRepository.saveNotificationSettings(notificationSettings) }
    }

    @Test
    fun `デフォルト値の通知設定を保存できること`() = runTest {
        val notificationSettings = NotificationSettings()

        useCase(notificationSettings)

        coVerify { mockRepository.saveNotificationSettings(notificationSettings) }
    }

    @Test
    fun `通知アニメーションのみ有効な設定を保存できること`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = true,
            isNotificationBadgeEnabled = false,
        )

        useCase(notificationSettings)

        coVerify { mockRepository.saveNotificationSettings(notificationSettings) }
    }

    @Test
    fun `通知バッジのみ有効な設定を保存できること`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = false,
            isNotificationBadgeEnabled = true,
        )

        useCase(notificationSettings)

        coVerify { mockRepository.saveNotificationSettings(notificationSettings) }
    }

    @Test
    fun `両方無効な通知設定を保存できること`() = runTest {
        val notificationSettings = NotificationSettings(
            isNotificationAnimationEnabled = false,
            isNotificationBadgeEnabled = false,
        )

        useCase(notificationSettings)

        coVerify { mockRepository.saveNotificationSettings(notificationSettings) }
    }
}
