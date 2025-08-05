package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.AppIconSettings
import io.github.kei_1111.withmo.core.model.user_settings.AppIconShape
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveAppIconSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveAppIconSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveAppIconSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `アプリアイコン設定を保存できること`() = runTest {
        val newAppIconSettings = AppIconSettings(
            appIconShape = AppIconShape.RoundedCorner,
            roundedCornerPercent = 30f
        )

        useCase(newAppIconSettings)

        coVerify { mockRepository.saveAppIconSettings(newAppIconSettings) }
    }

    @Test
    fun `異なるアプリアイコン設定を連続で保存できること`() = runTest {
        val setting1 = AppIconSettings(appIconShape = AppIconShape.Square)
        val setting2 = AppIconSettings(appIconShape = AppIconShape.RoundedCorner, roundedCornerPercent = 50f)

        useCase(setting1)
        useCase(setting2)

        coVerify { mockRepository.saveAppIconSettings(setting1) }
        coVerify { mockRepository.saveAppIconSettings(setting2) }
    }

    @Test
    fun `角丸パーセンテージのみを変更できること`() = runTest {
        val settings = AppIconSettings(appIconShape = AppIconShape.Circle, roundedCornerPercent = 75f)
        
        useCase(settings)

        coVerify { mockRepository.saveAppIconSettings(settings) }
    }
}