package io.github.kei_1111.withmo.feature.onboarding.viewmodel.finish

import app.cash.turbine.test
import io.github.kei_1111.withmo.core.domain.usecase.MarkOnboardingShownUseCase
import io.github.kei_1111.withmo.feature.onboarding.screens.finish.FinishAction
import io.github.kei_1111.withmo.feature.onboarding.screens.finish.FinishEffect
import io.github.kei_1111.withmo.feature.onboarding.screens.finish.FinishViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class FinishViewModelTest {

    private lateinit var markOnboardingShownUseCase: MarkOnboardingShownUseCase
    private lateinit var viewModel: FinishViewModel

    @Before
    fun setUp() {
        markOnboardingShownUseCase = mockk()
        viewModel = FinishViewModel(markOnboardingShownUseCase)
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
        }

        coVerify(exactly = 1) { markOnboardingShownUseCase() }
    }
}
