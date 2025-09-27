package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetModelSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetModelSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetModelSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのモデル設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(Result.success(UserSettings()))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val modelSettings = result.getOrThrow()
            assertEquals(1.0f, modelSettings.scale, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更されたモデル設定を取得できること`() = runTest {
        val customModelSettings = ModelSettings(scale = 1.5f)
        every { mockRepository.userSettings } returns flowOf(
            Result.success(UserSettings(modelSettings = customModelSettings)),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val modelSettings = result.getOrThrow()
            assertEquals(1.5f, modelSettings.scale, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `モデル設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            modelSettings = ModelSettings(scale = 2.0f),
        )
        every { mockRepository.userSettings } returns flowOf(
            Result.success(initialSettings),
            Result.success(updatedSettings),
        )

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(1.0f, firstResult.getOrThrow().scale, 0.001f)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals(2.0f, secondResult.getOrThrow().scale, 0.001f)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = ModelSettings(scale = 1.0f)
        val userSettings = UserSettings(modelSettings = sameSettings)
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
