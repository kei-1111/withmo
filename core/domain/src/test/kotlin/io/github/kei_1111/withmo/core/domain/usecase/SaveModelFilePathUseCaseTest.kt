package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ModelFilePath
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveModelFilePathUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveModelFilePathUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveModelFilePathUseCaseImpl(mockRepository)
    }

    @Test
    fun `モデルファイルパスを保存できること`() = runTest {
        val modelFilePath = ModelFilePath("/storage/emulated/0/model.vrm")

        useCase(modelFilePath)

        coVerify { mockRepository.saveModelFilePath(modelFilePath) }
    }

    @Test
    fun `nullのモデルファイルパスを保存できること`() = runTest {
        val modelFilePath = ModelFilePath(null)

        useCase(modelFilePath)

        coVerify { mockRepository.saveModelFilePath(modelFilePath) }
    }

    @Test
    fun `空文字のモデルファイルパスを保存できること`() = runTest {
        val modelFilePath = ModelFilePath("")

        useCase(modelFilePath)

        coVerify { mockRepository.saveModelFilePath(modelFilePath) }
    }

    @Test
    fun `長いパスのモデルファイルパスを保存できること`() = runTest {
        val longPath = "/storage/emulated/0/Android/data/io.github.kei_1111.withmo/files/models/very_long_model_name_with_many_characters.vrm"
        val modelFilePath = ModelFilePath(longPath)

        useCase(modelFilePath)

        coVerify { mockRepository.saveModelFilePath(modelFilePath) }
    }
}
