package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
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

class GetClockSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetClockSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetClockSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトの時計設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assertEquals(true, result.isClockShown)
            assertEquals(ClockType.TOP_DATE, result.clockType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更された時計設定を取得できること`() = runTest {
        val customClockSettings = ClockSettings(
            isClockShown = false,
            clockType = ClockType.HORIZONTAL_DATE,
        )
        every { mockRepository.userSettings } returns flowOf(
            UserSettings(clockSettings = customClockSettings),
        )

        useCase().test {
            val result = awaitItem()
            assertEquals(false, result.isClockShown)
            assertEquals(ClockType.HORIZONTAL_DATE, result.clockType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `時計表示の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            clockSettings = ClockSettings(isClockShown = false),
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            assertEquals(true, awaitItem().isClockShown)
            assertEquals(false, awaitItem().isClockShown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `時計タイプの変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            clockSettings = ClockSettings(clockType = ClockType.HORIZONTAL_DATE),
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            assertEquals(ClockType.TOP_DATE, awaitItem().clockType)
            assertEquals(ClockType.HORIZONTAL_DATE, awaitItem().clockType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = ClockSettings(
            isClockShown = true,
            clockType = ClockType.TOP_DATE,
        )
        val userSettings = UserSettings(clockSettings = sameSettings)
        every { mockRepository.userSettings } returns flowOf(userSettings, userSettings, userSettings)

        useCase().test {
            awaitItem()
            awaitComplete()
        }
    }
}
