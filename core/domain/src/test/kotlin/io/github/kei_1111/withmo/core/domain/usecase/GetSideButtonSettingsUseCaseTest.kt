package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
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

class GetSideButtonSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetSideButtonSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetSideButtonSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのサイドボタン設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val sideButtonSettings = result.getOrThrow()
            assertEquals(true, sideButtonSettings.isShowScaleSliderButtonShown)
            assertEquals(true, sideButtonSettings.isOpenDocumentButtonShown)
            assertEquals(true, sideButtonSettings.isSetDefaultModelButtonShown)
            assertEquals(true, sideButtonSettings.isNavigateSettingsButtonShown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更されたサイドボタン設定を取得できること`() = runTest {
        val customSideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = true,
        )
        every { mockRepository.userSettings } returns flowOf(
            UserSettings(sideButtonSettings = customSideButtonSettings),
        )

        useCase().test {
            val result = awaitItem()
            assert(result.isSuccess)
            val sideButtonSettings = result.getOrThrow()
            assertEquals(false, sideButtonSettings.isShowScaleSliderButtonShown)
            assertEquals(false, sideButtonSettings.isOpenDocumentButtonShown)
            assertEquals(true, sideButtonSettings.isSetDefaultModelButtonShown)
            assertEquals(true, sideButtonSettings.isNavigateSettingsButtonShown)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `サイドボタン設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            sideButtonSettings = SideButtonSettings(
                isShowScaleSliderButtonShown = false,
                isOpenDocumentButtonShown = true,
                isSetDefaultModelButtonShown = false,
                isNavigateSettingsButtonShown = true,
            ),
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            val firstResult = awaitItem()
            assert(firstResult.isSuccess)
            val first = firstResult.getOrThrow()
            assertEquals(true, first.isShowScaleSliderButtonShown)
            assertEquals(true, first.isSetDefaultModelButtonShown)

            val secondResult = awaitItem()
            assert(secondResult.isSuccess)
            val second = secondResult.getOrThrow()
            assertEquals(false, second.isShowScaleSliderButtonShown)
            assertEquals(false, second.isSetDefaultModelButtonShown)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = true,
            isOpenDocumentButtonShown = true,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = true,
        )
        val userSettings = UserSettings(sideButtonSettings = sameSettings)
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
