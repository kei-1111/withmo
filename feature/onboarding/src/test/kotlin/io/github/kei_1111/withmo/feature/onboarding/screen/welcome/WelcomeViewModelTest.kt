package io.github.kei_1111.withmo.feature.onboarding.screen.welcome

import app.cash.turbine.test
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class WelcomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: WelcomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = WelcomeViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
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
