package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelSettings
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveModelSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveModelSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveModelSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `モデル設定を保存できること`() = runTest {
        val modelSettings = ModelSettings(scale = 1.5f)

        useCase(modelSettings)

        coVerify { mockRepository.saveModelSettings(modelSettings) }
    }

    @Test
    fun `デフォルト値のモデル設定を保存できること`() = runTest {
        val modelSettings = ModelSettings()

        useCase(modelSettings)

        coVerify { mockRepository.saveModelSettings(modelSettings) }
    }

    @Test
    fun `最小スケール値のモデル設定を保存できること`() = runTest {
        val modelSettings = ModelSettings(scale = 0.5f)

        useCase(modelSettings)

        coVerify { mockRepository.saveModelSettings(modelSettings) }
    }

    @Test
    fun `最大スケール値のモデル設定を保存できること`() = runTest {
        val modelSettings = ModelSettings(scale = 1.5f)

        useCase(modelSettings)

        coVerify { mockRepository.saveModelSettings(modelSettings) }
    }
}
