package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetUserSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetUserSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetUserSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのユーザー設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assertEquals(true, result.clockSettings.isClockShown)
            assertEquals(AppIconShape.Circle, result.appIconSettings.appIconShape)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `カスタマイズされたユーザー設定を取得できること`() = runTest {
        val customSettings = UserSettings(
            clockSettings = ClockSettings(isClockShown = false, clockType = ClockType.HORIZONTAL_DATE),
            appIconSettings = AppIconSettings(appIconShape = AppIconShape.Square, roundedCornerPercent = 50f)
        )
        every { mockRepository.userSettings } returns flowOf(customSettings)

        useCase().test {
            val result = awaitItem()
            assertEquals(false, result.clockSettings.isClockShown)
            assertEquals(ClockType.HORIZONTAL_DATE, result.clockSettings.clockType)
            assertEquals(AppIconShape.Square, result.appIconSettings.appIconShape)
            assertEquals(50f, result.appIconSettings.roundedCornerPercent, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ユーザー設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            clockSettings = ClockSettings(isClockShown = false)
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            assertEquals(true, awaitItem().clockSettings.isClockShown)
            assertEquals(false, awaitItem().clockSettings.isClockShown)
            cancelAndIgnoreRemainingEvents()
        }
    }
}