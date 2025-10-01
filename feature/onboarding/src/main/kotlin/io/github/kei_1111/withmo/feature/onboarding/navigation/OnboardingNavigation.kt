package io.github.kei_1111.withmo.feature.onboarding.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.kei_1111.withmo.core.ui.navigation.Finish
import io.github.kei_1111.withmo.core.ui.navigation.Home
import io.github.kei_1111.withmo.core.ui.navigation.OnboardingGraph
import io.github.kei_1111.withmo.core.ui.navigation.SelectDisplayModel
import io.github.kei_1111.withmo.core.ui.navigation.SelectFavoriteApp
import io.github.kei_1111.withmo.core.ui.navigation.Welcome
import io.github.kei_1111.withmo.core.ui.navigation.checkScreen
import io.github.kei_1111.withmo.core.ui.navigation.slideInHorizontallyFrom
import io.github.kei_1111.withmo.core.ui.navigation.slideOutHorizontallyTo
import io.github.kei_1111.withmo.feature.onboarding.screens.finish.FinishScreen
import io.github.kei_1111.withmo.feature.onboarding.screens.select_display_model.SelectDisplayModelScreen
import io.github.kei_1111.withmo.feature.onboarding.screens.select_favorite_app.SelectFavoriteAppScreen
import io.github.kei_1111.withmo.feature.onboarding.screens.welcome.WelcomeScreen

fun NavHostController.navigateSelectFavoriteApp() = navigate(SelectFavoriteApp)

fun NavHostController.navigateSelectDisplayModel() = navigate(SelectDisplayModel)

fun NavHostController.navigateFinish() = navigate(Finish)

fun NavGraphBuilder.onboardingGraph(
    navigateBack: () -> Unit,
    navigateSelectFavoriteApp: () -> Unit,
    navigateSelectDisplayModel: () -> Unit,
    navigateFinish: () -> Unit,
    navigateHome: () -> Unit,
) {
    navigation<OnboardingGraph>(startDestination = Welcome) {
        composable<Welcome>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            WelcomeScreen(
                navigateSelectFavoriteApp = navigateSelectFavoriteApp,
            )
        }
        composable<SelectFavoriteApp>(
            enterTransition = { slideInHorizontallyFrom(Welcome) },
            exitTransition = { slideOutHorizontallyTo(Welcome) },
        ) {
            SelectFavoriteAppScreen(
                navigateBack = navigateBack,
                navigateSelectDisplayModel = navigateSelectDisplayModel,
            )
        }
        composable<SelectDisplayModel>(
            enterTransition = { slideInHorizontallyFrom(SelectFavoriteApp) },
            exitTransition = { slideOutHorizontallyTo(SelectFavoriteApp) },
        ) {
            SelectDisplayModelScreen(
                navigateBack = navigateBack,
                navigateFinish = navigateFinish,
            )
        }
        composable<Finish>(
            enterTransition = { slideInHorizontallyFrom(SelectDisplayModel) },
            exitTransition = {
                if (targetState.checkScreen(Home)) {
                    slideOutVertically { it }
                } else {
                    slideOutHorizontallyTo(SelectDisplayModel)
                }
            },
        ) {
            FinishScreen(
                navigateBack = navigateBack,
                navigateHome = navigateHome,
            )
        }
    }
}
