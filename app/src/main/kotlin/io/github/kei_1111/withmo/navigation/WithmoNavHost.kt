package io.github.kei_1111.withmo.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import io.github.kei_1111.withmo.core.ui.navigation.NavigationRoute
import io.github.kei_1111.withmo.feature.home.navigation.homeGraph
import io.github.kei_1111.withmo.feature.home.navigation.navigateHome
import io.github.kei_1111.withmo.feature.onboarding.navigation.navigateFinish
import io.github.kei_1111.withmo.feature.onboarding.navigation.navigateSelectDisplayModel
import io.github.kei_1111.withmo.feature.onboarding.navigation.navigateSelectFavoriteApp
import io.github.kei_1111.withmo.feature.onboarding.navigation.onboardingGraph
import io.github.kei_1111.withmo.feature.setting.navigation.navigateAppIconSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateClockSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateFavoriteAppSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateNotificationSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateSideButtonSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateSortSettings
import io.github.kei_1111.withmo.feature.setting.navigation.navigateThemeSettings
import io.github.kei_1111.withmo.feature.setting.navigation.settingsGraph

@Composable
fun WithmoNavHost(
    navController: NavHostController,
    startDestination: NavigationRoute,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        onboardingGraph(
            navigateBack = navController::popBackStack,
            navigateSelectFavoriteApp = navController::navigateSelectFavoriteApp,
            navigateSelectDisplayModel = navController::navigateSelectDisplayModel,
            navigateFinish = navController::navigateFinish,
            navigateHome = navController::navigateHome,
        )

        homeGraph(
            navigateSettings = navController::navigateSettings,
        )

        settingsGraph(
            navigateBack = navController::popBackStack,
            navigateClockSettings = navController::navigateClockSettings,
            navigateAppIconSettings = navController::navigateAppIconSettings,
            navigateFavoriteAppSettings = navController::navigateFavoriteAppSettings,
            navigateSideButtonSettings = navController::navigateSideButtonSettings,
            navigateSortSettings = navController::navigateSortSettings,
            navigateNotificationSettings = navController::navigateNotificationSettings,
            navigateThemeSettings = navController::navigateThemeSettings,
        )
    }
}
