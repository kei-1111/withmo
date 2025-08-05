package io.github.kei_1111.withmo.core.domain.usecase

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.repository.UserSettingsRepository
import io.github.kei_1111.withmo.core.model.user_settings.SortSettings
import io.github.kei_1111.withmo.core.model.user_settings.SortType
import io.github.kei_1111.withmo.core.model.user_settings.UserSettings
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetSortSettingsUseCaseTest {

    private lateinit var mockRepository: UserSettingsRepository
    private lateinit var useCase: GetSortSettingsUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetSortSettingsUseCaseImpl(mockRepository)
    }

    @Test
    fun `デフォルトのソート設定を取得できること`() = runTest {
        every { mockRepository.userSettings } returns flowOf(UserSettings())

        useCase().test {
            val result = awaitItem()
            assertEquals(SortType.ALPHABETICAL, result.sortType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `変更されたソート設定を取得できること`() = runTest {
        val customSortSettings = SortSettings(sortType = SortType.USE_COUNT)
        every { mockRepository.userSettings } returns flowOf(
            UserSettings(sortSettings = customSortSettings)
        )

        useCase().test {
            val result = awaitItem()
            assertEquals(SortType.USE_COUNT, result.sortType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `ソート設定の変更が反映されること`() = runTest {
        val initialSettings = UserSettings()
        val updatedSettings = UserSettings(
            sortSettings = SortSettings(sortType = SortType.USE_COUNT)
        )
        every { mockRepository.userSettings } returns flowOf(initialSettings, updatedSettings)

        useCase().test {
            assertEquals(SortType.ALPHABETICAL, awaitItem().sortType)
            assertEquals(SortType.USE_COUNT, awaitItem().sortType)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `distinctUntilChangedが機能すること_同じ値では流れない`() = runTest {
        val sameSettings = SortSettings(sortType = SortType.ALPHABETICAL)
        val userSettings = UserSettings(sortSettings = sameSettings)
        every { mockRepository.userSettings } returns flowOf(userSettings, userSettings, userSettings)

        useCase().test {
            awaitItem()
            awaitComplete()
        }
    }
}