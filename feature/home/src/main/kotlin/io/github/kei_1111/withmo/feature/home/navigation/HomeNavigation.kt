package io.github.kei_1111.withmo.feature.home.navigation

import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import io.github.kei_1111.withmo.core.ui.navigation.Home
import io.github.kei_1111.withmo.core.ui.navigation.OnboardingGraph
import io.github.kei_1111.withmo.feature.home.screens.HomeScreen

fun NavHostController.navigateHome() = navigate(Home) {
    popUpTo(OnboardingGraph) {
        inclusive = true
    }
}

fun NavGraphBuilder.homeGraph(
    navigateSettings: () -> Unit,
) {
    composable<Home>(
        enterTransition = { fadeIn() },
        exitTransition = { fadeOut() },
    ) {
        HomeScreen(
            navigateSettings = navigateSettings,
        )
    }
}
