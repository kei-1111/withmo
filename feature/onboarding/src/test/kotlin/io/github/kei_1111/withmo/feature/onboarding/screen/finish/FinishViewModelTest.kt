package io.github.kei_1111.withmo.feature.onboarding.screen.finish

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FinishViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var markOnboardingShownUseCase: MarkOnboardingShownUseCase
    private lateinit var viewModel: FinishViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        markOnboardingShownUseCase = mockk()
        viewModel = FinishViewModel(markOnboardingShownUseCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `OnBackButtonClickアクションでNavigateBackエフェクトが送信される`() = runTest {
        viewModel.effect.test {
            viewModel.onAction(FinishAction.OnBackButtonClick)

            val effect = awaitItem()
            assertEquals(FinishEffect.NavigateBack, effect)
        }
    }

    @Test
    fun `OnFinishButtonClickアクションでmarkOnboardingShownUseCaseが呼び出されNavigateHomeエフェクトが送信される`() = runTest {
        coEvery { markOnboardingShownUseCase() } returns Unit

        viewModel.effect.test {
            viewModel.onAction(FinishAction.OnFinishButtonClick)

            val effect = awaitItem()
            assertEquals(FinishEffect.NavigateHome, effect)

            advanceUntilIdle()

            coVerify(exactly = 1) { markOnboardingShownUseCase() }
        }
    }
}
