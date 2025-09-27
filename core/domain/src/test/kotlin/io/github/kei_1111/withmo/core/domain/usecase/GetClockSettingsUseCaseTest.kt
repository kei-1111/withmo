package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
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
            assert(result.isSuccess)
            val clockSettings = result.getOrThrow()
            assertEquals(true, clockSettings.isClockShown)
            assertEquals(ClockType.TOP_DATE, clockSettings.clockType)
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
            assert(result.isSuccess)
            val clockSettings = result.getOrThrow()
            assertEquals(false, clockSettings.isClockShown)
            assertEquals(ClockType.HORIZONTAL_DATE, clockSettings.clockType)
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
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(true, firstResult.getOrThrow().isClockShown)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(false, secondResult.getOrThrow().isClockShown)
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
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(ClockType.TOP_DATE, firstResult.getOrThrow().clockType)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(ClockType.HORIZONTAL_DATE, secondResult.getOrThrow().clockType)
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
