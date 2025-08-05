package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.ClockSettings
import io.github.kei_1111.withmo.core.model.user_settings.ClockType
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SaveClockSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: SaveClockSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = SaveClockSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `時計設定を保存できること`() = runTest {
        val clockSettings = ClockSettings(
            isClockShown = false,
            clockType = ClockType.HORIZONTAL_DATE
        )

        useCase(clockSettings)

        coVerify { mockRepository.saveClockSettings(clockSettings) }
    }

    @Test
    fun `異なる時計設定を連続で保存できること`() = runTest {
        val setting1 = ClockSettings(isClockShown = false)
        val setting2 = ClockSettings(clockType = ClockType.HORIZONTAL_DATE)

        useCase(setting1)
        useCase(setting2)

        coVerify { mockRepository.saveClockSettings(setting1) }
        coVerify { mockRepository.saveClockSettings(setting2) }
    }
}