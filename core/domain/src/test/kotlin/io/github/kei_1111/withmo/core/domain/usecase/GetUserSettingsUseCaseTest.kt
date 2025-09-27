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
import org.junit.Assert.assertTrue
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
        every { mockRepository.userSettings } returns flowOf(Result.success(UserSettings()))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val userSettings = result.getOrThrow()
            assertEquals(true, userSettings.clockSettings.isClockShown)
            assertEquals(AppIconShape.Circle, userSettings.appIconSettings.appIconShape)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `カスタマイズされたユーザー設定を取得できること`() = runTest {
        val customSettings = UserSettings(
            clockSettings = ClockSettings(isClockShown = false, clockType = ClockType.HORIZONTAL_DATE),
            appIconSettings = AppIconSettings(appIconShape = AppIconShape.Square, roundedCornerPercent = 50f),
        )
        every { mockRepository.userSettings } returns flowOf(Result.success(customSettings))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val userSettings = result.getOrThrow()
            assertEquals(false, userSettings.clockSettings.isClockShown)
            assertEquals(ClockType.HORIZONTAL_DATE, userSettings.clockSettings.clockType)
            assertEquals(AppIconShape.Square, userSettings.appIconSettings.appIconShape)
            assertEquals(50f, userSettings.appIconSettings.roundedCornerPercent, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ユーザー設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            clockSettings = ClockSettings(isClockShown = false),
        )
        every { mockRepository.userSettings } returns flowOf(
            Result.success(initialSettings),
            Result.success(updatedSettings),
        )

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(true, firstResult.getOrThrow().clockSettings.isClockShown)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(false, secondResult.getOrThrow().clockSettings.isClockShown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `エラーが発生した場合Result_failureが返されること`() = runTest {
        val exception = RuntimeException("Database error")
        every { mockRepository.userSettings } returns flowOf(Result.failure(exception))

        useCase().test {
            val result = awaitItem()
            assertTrue(result.isFailure)
            assertEquals(exception, result.exceptionOrNull())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
