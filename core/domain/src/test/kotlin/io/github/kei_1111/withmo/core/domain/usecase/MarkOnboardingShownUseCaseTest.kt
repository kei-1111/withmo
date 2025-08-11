package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MarkOnboardingShownUseCaseTest {

    private lateinit var mockRepository: OneTimeEventRepository
    private lateinit var useCase: MarkOnboardingShownUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = MarkOnboardingShownUseCaseImpl(mockRepository)
    }

    @Test
    fun `オンボーディングの表示済みステータスをマークできること`() = runTest {
        useCase()

        coVerify { mockRepository.markOnboardingFirstShown() }
    }

    @Test
    fun `複数回実行してもリポジトリが呼ばれること`() = runTest {
        useCase()
        useCase()

        coVerify(exactly = 2) { mockRepository.markOnboardingFirstShown() }
    }

    @Test
    fun `マーク後の値がtrueになること`() = runTest {
        val stateFlow = MutableStateFlow(false)
        every { mockRepository.isOnboardingFirstShown } returns stateFlow
        coEvery { mockRepository.markOnboardingFirstShown() } answers {
            stateFlow.value = true
        }

        val initialValue = mockRepository.isOnboardingFirstShown.first()
        assertEquals(false, initialValue)

        useCase()

        val updatedValue = mockRepository.isOnboardingFirstShown.first()
        assertEquals(true, updatedValue)
    }
}
