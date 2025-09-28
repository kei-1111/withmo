package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class GetModelFilePathUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetModelFilePathUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetModelFilePathUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのモデルファイルパスを取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(Result.success(UserSettings()))

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val modelFilePath = result.getOrThrow()
            assertEquals(null, modelFilePath.path)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `設定されたモデルファイルパスを取得できること`() = runTest {
        val customModelFilePath = ModelFilePath("/storage/emulated/0/model.vrm")
        every { mockRepository.userSettings } returns flowOf(
            Result.success(UserSettings(modelFilePath = customModelFilePath)),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val modelFilePath = result.getOrThrow()
            assertEquals("/storage/emulated/0/model.vrm", modelFilePath.path)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `モデルファイルパスの変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            modelFilePath = ModelFilePath("/storage/emulated/0/new_model.vrm"),
        )
        every { mockRepository.userSettings } returns flowOf(
            Result.success(initialSettings),
            Result.success(updatedSettings),
        )

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            assertEquals(null, firstResult.getOrThrow().path)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            assertEquals("/storage/emulated/0/new_model.vrm", secondResult.getOrThrow().path)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `空文字のモデルファイルパスを取得できること`() = runTest {
        val emptyModelFilePath = ModelFilePath("")
        every { mockRepository.userSettings } returns flowOf(
            Result.success(UserSettings(modelFilePath = emptyModelFilePath)),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val modelFilePath = result.getOrThrow()
            assertEquals("", modelFilePath.path)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameModelFilePath = ModelFilePath("/storage/emulated/0/model.vrm")
        val userSettings = UserSettings(modelFilePath = sameModelFilePath)
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
