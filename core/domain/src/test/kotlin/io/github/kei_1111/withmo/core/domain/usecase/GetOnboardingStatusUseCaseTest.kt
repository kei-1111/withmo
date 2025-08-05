package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetOnboardingStatusUseCaseTest {

    private lateinit var mockRepository: OneTimeEventRepository
    private lateinit var useCase: GetOnboardingStatusUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetOnboardingStatusUseCaseImpl(mockRepository)
    }

    @Test
    fun `初期状態でfalseを取得できること`() = runTest {
        every { mockRepository.isOnboardingFirstShown } returns flowOf(false)

        val result = useCase()

        assertEquals(false, result)
    }

    @Test
    fun `オンボーディング表示済みでtrueを取得できること`() = runTest {
        every { mockRepository.isOnboardingFirstShown } returns flowOf(true)

        val result = useCase()

        assertEquals(true, result)
    }

    @Test
    fun `複数の値がある場合に最初の値を取得できること`() = runTest {
        every { mockRepository.isOnboardingFirstShown } returns flowOf(false, true, false)

        val result = useCase()

        assertEquals(false, result)
    }
}