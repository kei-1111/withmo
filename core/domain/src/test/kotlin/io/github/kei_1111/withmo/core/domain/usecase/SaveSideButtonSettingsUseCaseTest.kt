package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SideButtonSettings
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveSideButtonSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveSideButtonSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveSideButtonSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `サイドボタン設定を保存できること`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = true,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = true,
        )

        useCase(sideButtonSettings)

        coVerify { mockRepository.saveSideButtonSettings(sideButtonSettings) }
    }

    @Test
    fun `デフォルト値のサイドボタン設定を保存できること`() = runTest {
        val sideButtonSettings = SideButtonSettings()

        useCase(sideButtonSettings)

        coVerify { mockRepository.saveSideButtonSettings(sideButtonSettings) }
    }

    @Test
    fun `全てのボタンを非表示にする設定を保存できること`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = false,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = false,
            isNavigateSettingsButtonShown = false,
        )

        useCase(sideButtonSettings)

        coVerify { mockRepository.saveSideButtonSettings(sideButtonSettings) }
    }

    @Test
    fun `部分的に変更したサイドボタン設定を保存できること`() = runTest {
        val sideButtonSettings = SideButtonSettings(
            isShowScaleSliderButtonShown = true,
            isOpenDocumentButtonShown = false,
            isSetDefaultModelButtonShown = true,
            isNavigateSettingsButtonShown = false,
        )

        useCase(sideButtonSettings)

        coVerify { mockRepository.saveSideButtonSettings(sideButtonSettings) }
    }
}
