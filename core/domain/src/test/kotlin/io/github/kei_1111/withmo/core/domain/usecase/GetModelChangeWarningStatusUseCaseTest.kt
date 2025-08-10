package io.github.kei_1111.withmo.core.domain.usecase

import io.github.kei_1111.withmo.core.domain.repository.OneTimeEventRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetModelChangeWarningStatusUseCaseTest {

    private lateinit var mockRepository: OneTimeEventRepository
    private lateinit var useCase: GetModelChangeWarningStatusUseCase

    @Before
    fun setup() {
        mockRepository = mockk()
        useCase = GetModelChangeWarningStatusUseCaseImpl(mockRepository)
    }

    @Test
    fun `初期状態でfalseを取得できること`() = runTest {
        every { mockRepository.isModelChangeWarningFirstShown } returns flowOf(false)

        val result = useCase()

        assertEquals(false, result)
    }

    @Test
    fun `警告が表示済みでtrueを取得できること`() = runTest {
        every { mockRepository.isModelChangeWarningFirstShown } returns flowOf(true)

        val result = useCase()

        assertEquals(true, result)
    }
}
