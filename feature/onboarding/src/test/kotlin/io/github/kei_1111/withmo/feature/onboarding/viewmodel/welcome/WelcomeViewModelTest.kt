package io.github.kei_1111.withmo.feature.onboarding.viewmodel.welcome

import app.cash.turbine.test
import io.github.kei_1111.withmo.feature.onboarding.screens.welcome.WelcomeAction
import io.github.kei_1111.withmo.feature.onboarding.screens.welcome.WelcomeEffect
import io.github.kei_1111.withmo.feature.onboarding.screens.welcome.WelcomeViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class WelcomeViewModelTest {

    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        viewModel = WelcomeViewModel()
    }

    @Test
    fun `OnNextButtonClickアクションでNavigateSelectFavoriteAppエフェクトが送信される`() = runTest {
        viewModel.effect.test {
            viewModel.onAction(WelcomeAction.OnNextButtonClick)

            val effect = awaitItem()
            assertEquals(WelcomeEffect.NavigateSelectFavoriteApp, effect)
        }
    }
}
