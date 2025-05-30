package io.github.kei_1111.withmo.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import io.github.kei_1111.withmo.feature.home.HomeScreen
import io.github.kei_1111.withmo.feature.onboarding.OnboardingScreen
import io.github.kei_1111.withmo.ui.screens.app_icon_settings.AppIconSettingsScreen
import io.github.kei_1111.withmo.ui.screens.clock_settings.ClockSettingsScreen
import io.github.kei_1111.withmo.ui.screens.favorite_app_settings.FavoriteAppSettingsScreen
import io.github.kei_1111.withmo.ui.screens.notification_settings.NotificationSettingsScreen
import io.github.kei_1111.withmo.ui.screens.settings.SettingsScreen
import io.github.kei_1111.withmo.ui.screens.side_button_settings.SideButtonSettingsScreen
import io.github.kei_1111.withmo.ui.screens.sort_settings.SortSettingsScreen
import io.github.kei_1111.withmo.ui.screens.theme_settings.ThemeSettingsScreen

@Suppress("LongMethod")
@Composable
fun WithmoNavHost(
    navController: NavHostController,
    startDestination: Screen,
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    currentDestination?.hierarchy?.any { it.route == Screen.ThemeSettings::class.qualifiedName }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier,
        enterTransition = { slideInHorizontally { it } },
        exitTransition = { slideOutHorizontally { it } },
    ) {
        composable<Screen.Onboarding>(
            exitTransition = { slideOutVertically { it } },
        ) {
            OnboardingScreen(
                onFinishButtonClick = { navController.navigate(Screen.Home) },
            )
        }
        composable<Screen.Home>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            HomeScreen(
                onNavigateSettingsButtonClick = { navController.navigate(Screen.Settings) },
            )
        }
        composable<Screen.Settings>(
            enterTransition = {
                if (initialState.checkScreen(Screen.Home)) {
                    slideInVertically { it }
                } else { EnterTransition.None }
            },
            exitTransition = {
                if (targetState.checkScreen(Screen.Home)) {
                    slideOutVertically { it }
                } else { ExitTransition.None }
            },
        ) {
            SettingsScreen(
                onNavigateClockSettingsButtonClick = { navController.navigate(Screen.ClockSettings) },
                onNavigateAppIconSettingsButtonClick = { navController.navigate(Screen.AppIconSettings) },
                onNavigateFavoriteAppSettingsButtonClick = { navController.navigate(Screen.FavoriteAppSettings) },
                onNavigateSideButtonSettingsButtonClick = { navController.navigate(Screen.SideButtonSettings) },
                onNavigateSortSettingsButtonClick = { navController.navigate(Screen.SortSettings) },
                onNavigateNotificationSettingsButtonClick = { navController.navigate(Screen.NotificationSettings) },
                onNavigateThemeSettingsButtonClick = { navController.navigate(Screen.ThemeSettings) },
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.ClockSettings> {
            ClockSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.AppIconSettings> {
            AppIconSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.FavoriteAppSettings> {
            FavoriteAppSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.SideButtonSettings> {
            SideButtonSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.SortSettings> {
            SortSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.NotificationSettings> {
            NotificationSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
        composable<Screen.ThemeSettings> {
            ThemeSettingsScreen(
                onBackButtonClick = { navController.popBackStack() },
            )
        }
    }
}

fun NavBackStackEntry.checkScreen(screen: Screen): Boolean {
    return destination.route == screen::class.qualifiedName
}
