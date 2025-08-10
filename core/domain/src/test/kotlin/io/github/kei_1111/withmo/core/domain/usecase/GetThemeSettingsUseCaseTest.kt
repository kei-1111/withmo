package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetThemeSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetThemeSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetThemeSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのテーマ設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assertEquals(ThemeType.TIME_BASED, result.themeType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更されたテーマ設定を取得できること`() = runTest {
        val customThemeSettings = ThemeSettings(themeType = ThemeType.DARK)
        every { mockRepository.userSettings } returns flowOf(
            UserSettings(themeSettings = customThemeSettings),
        )

        useCase().test {
            val result = awaitItem()
            assertEquals(ThemeType.DARK, result.themeType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `テーマ設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            themeSettings = ThemeSettings(themeType = ThemeType.LIGHT),
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            assertEquals(ThemeType.TIME_BASED, awaitItem().themeType)
            assertEquals(ThemeType.LIGHT, awaitItem().themeType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = ThemeSettings(themeType = ThemeType.TIME_BASED)
        val userSettings = UserSettings(themeSettings = sameSettings)
        every { mockRepository.userSettings } returns flowOf(userSettings, userSettings, userSettings)

        useCase().test {
            awaitItem()
            awaitComplete()
        }
    }
}
