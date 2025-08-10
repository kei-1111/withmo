package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ThemeSettings
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveThemeSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveThemeSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveThemeSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `テーマ設定を保存できること`() = runTest {
        val themeSettings = ThemeSettings(themeType = ThemeType.DARK)

        useCase(themeSettings)

        coVerify { mockRepository.saveThemeSettings(themeSettings) }
    }

    @Test
    fun `異なるテーマ設定を連続で保存できること`() = runTest {
        val setting1 = ThemeSettings(themeType = ThemeType.LIGHT)
        val setting2 = ThemeSettings(themeType = ThemeType.DARK)

        useCase(setting1)
        useCase(setting2)

        coVerify { mockRepository.saveThemeSettings(setting1) }
        coVerify { mockRepository.saveThemeSettings(setting2) }
    }
}
