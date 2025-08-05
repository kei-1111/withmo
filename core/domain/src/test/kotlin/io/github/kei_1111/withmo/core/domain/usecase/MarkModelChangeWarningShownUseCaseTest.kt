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

class MarkModelChangeWarningShownUseCaseTest {

    private lateinit var mockRepository: OneTimeEventRepository
    private lateinit var useCase: MarkModelChangeWarningShownUseCase

    @Before
    fun setup() {
        mockRepository = mockk(relaxed = true)
        useCase = MarkModelChangeWarningShownUseCaseImpl(mockRepository)
    }

    @Test
    fun `モデル変更警告の表示済みステータスをマークできること`() = runTest {
        useCase()

        coVerify { mockRepository.markModelChangeWarningFirstShown() }
    }

    @Test
    fun `複数回実行してもリポジトリが呼ばれること`() = runTest {
        useCase()
        useCase()

        coVerify(exactly = 2) { mockRepository.markModelChangeWarningFirstShown() }
    }

    @Test
    fun `マーク後の値がtrueになること`() = runTest {
        val stateFlow = MutableStateFlow(false)
        every { mockRepository.isModelChangeWarningFirstShown } returns stateFlow
        coEvery { mockRepository.markModelChangeWarningFirstShown() } answers {
            stateFlow.value = true
        }

        val initialValue = mockRepository.isModelChangeWarningFirstShown.first()
        assertEquals(false, initialValue)

        useCase()

        val updatedValue = mockRepository.isModelChangeWarningFirstShown.first()
        assertEquals(true, updatedValue)
    }
}