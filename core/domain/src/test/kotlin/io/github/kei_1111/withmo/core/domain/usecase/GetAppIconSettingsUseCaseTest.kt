package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetAppIconSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetAppIconSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetAppIconSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのアプリアイコン設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(Result.success(UserSettings()))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val appIconSettings = result.getOrThrow()
            assertEquals(AppIconShape.Circle, appIconSettings.appIconShape)
            assertEquals(20f, appIconSettings.roundedCornerPercent, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更されたアプリアイコン設定を取得できること`() = runTest {
        val customAppIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 25f,
        )
        every { mockRepository.userSettings } returns flowOf(
            Result.success(UserSettings(appIconSettings = customAppIconSettings)),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val appIconSettings = result.getOrThrow()
            assertEquals(AppIconShape.RoundedCorner, appIconSettings.appIconShape)
            assertEquals(25f, appIconSettings.roundedCornerPercent, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `アプリアイコン設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            appIconSettings = AppIconSettings(appIconShape = AppIconShape.Square),
        )
        every { mockRepository.userSettings } returns flowOf(
            Result.success(initialSettings),
            Result.success(updatedSettings),
        )

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(AppIconShape.Circle, firstResult.getOrThrow().appIconShape)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(AppIconShape.Square, secondResult.getOrThrow().appIconShape)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = AppIconSettings(appIconShape = AppIconShape.Circle)
        val userSettings = UserSettings(appIconSettings = sameSettings)
        every { mockRepository.userSettings } returns flowOf(
            Result.success(userSettings),
            Result.success(userSettings),
            Result.success(userSettings),
        )

        useCase().test {
            awaitItem()
            awaitComplete()
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
