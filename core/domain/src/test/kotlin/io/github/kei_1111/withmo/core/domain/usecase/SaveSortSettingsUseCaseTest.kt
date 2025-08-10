package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveSortSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveSortSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveSortSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `ソート設定を保存できること`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.USE_COUNT)

        useCase(sortSettings)

        coVerify { mockRepository.saveSortSettings(sortSettings) }
    }

    @Test
    fun `デフォルト値のソート設定を保存できること`() = runTest {
        val sortSettings = SortSettings()

        useCase(sortSettings)

        coVerify { mockRepository.saveSortSettings(sortSettings) }
    }

    @Test
    fun `アルファベット順ソート設定を保存できること`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.ALPHABETICAL)

        useCase(sortSettings)

        coVerify { mockRepository.saveSortSettings(sortSettings) }
    }

    @Test
    fun `使用回数順ソート設定を保存できること`() = runTest {
        val sortSettings = SortSettings(sortType = SortType.USE_COUNT)

        useCase(sortSettings)

        coVerify { mockRepository.saveSortSettings(sortSettings) }
    }
}
