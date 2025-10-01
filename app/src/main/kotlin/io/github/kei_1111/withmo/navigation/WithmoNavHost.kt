package io.github.kei_1111.withmo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
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
import io.github.kei_1111.withmo.feature.onboarding.finish.FinishScreen
import io.github.kei_1111.withmo.feature.onboarding.select_display_model.SelectDisplayModelScreen
import io.github.kei_1111.withmo.feature.onboarding.select_favorite_app.SelectFavoriteAppScreen
import io.github.kei_1111.withmo.feature.onboarding.welcome.WelcomeScreen
import io.github.kei_1111.withmo.feature.setting.app_icon.AppIconSettingsScreen
import io.github.kei_1111.withmo.feature.setting.clock.ClockSettingsScreen
import io.github.kei_1111.withmo.feature.setting.favorite_app.FavoriteAppSettingsScreen
import io.github.kei_1111.withmo.feature.setting.notification.NotificationSettingsScreen
import io.github.kei_1111.withmo.feature.setting.root.SettingsScreen
import io.github.kei_1111.withmo.feature.setting.side_button.SideButtonSettingsScreen
import io.github.kei_1111.withmo.feature.setting.sort.SortSettingsScreen
import io.github.kei_1111.withmo.feature.setting.theme.ThemeSettingsScreen

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
        composable<Screen.Welcome>(
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None },
        ) {
            WelcomeScreen(
                navigateSelectFavoriteApp = { navController.navigate(Screen.SelectFavoriteApp) },
            )
        }
        composable<Screen.SelectFavoriteApp>(
            enterTransition = { slideInHorizontallyFrom(Screen.Welcome) },
            exitTransition = { slideOutHorizontallyTo(Screen.Welcome) },
        ) {
            SelectFavoriteAppScreen(
                navigateBack = { navController.popBackStack() },
                navigateSelectDisplayModel = { navController.navigate(Screen.SelectDisplayModel) },
            )
        }
        composable<Screen.SelectDisplayModel>(
            enterTransition = { slideInHorizontallyFrom(Screen.SelectFavoriteApp) },
            exitTransition = { slideOutHorizontallyTo(Screen.SelectFavoriteApp) },
        ) {
            SelectDisplayModelScreen(
                navigateBack = { navController.popBackStack() },
                navigateFinish = { navController.navigate(Screen.Finish) },
            )
        }
        composable<Screen.Finish>(
            enterTransition = { slideInHorizontallyFrom(Screen.SelectDisplayModel) },
            exitTransition = {
                if (targetState.checkScreen(Screen.Home)) {
                    slideOutVertically { it }
                } else {
                    slideOutHorizontallyTo(Screen.SelectDisplayModel)
                }
            },
        ) {
            FinishScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = { navController.navigate(Screen.Home) },
            )
        }
        composable<Screen.Home>(
            enterTransition = { fadeIn() },
            exitTransition = { fadeOut() },
        ) {
            HomeScreen(
                navigateSettings = { navController.navigate(Screen.Settings) },
            )
        }
        composable<Screen.Settings>(
            enterTransition = { slideInVerticallyFrom(Screen.Home) },
            exitTransition = { slideOutVerticallyTo(Screen.Home) },
        ) {
            SettingsScreen(
                navigateClockSettings = { navController.navigate(Screen.ClockSettings) },
                navigateAppIconSettings = { navController.navigate(Screen.AppIconSettings) },
                navigateFavoriteAppSettings = { navController.navigate(Screen.FavoriteAppSettings) },
                navigateSideButtonSettings = { navController.navigate(Screen.SideButtonSettings) },
                navigateSortSettings = { navController.navigate(Screen.SortSettings) },
                navigateNotificationSettings = { navController.navigate(Screen.NotificationSettings) },
                navigateThemeSettings = { navController.navigate(Screen.ThemeSettings) },
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.ClockSettings> {
            ClockSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.AppIconSettings> {
            AppIconSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.FavoriteAppSettings> {
            FavoriteAppSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.SideButtonSettings> {
            SideButtonSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.SortSettings> {
            SortSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.NotificationSettings> {
            NotificationSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
        composable<Screen.ThemeSettings> {
            ThemeSettingsScreen(
                navigateBack = { navController.popBackStack() },
            )
        }
    }
}

fun NavBackStackEntry.checkScreen(screen: Screen): Boolean = destination.route == screen::class.qualifiedName

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInHorizontallyFrom(from: Screen): EnterTransition =
    if (initialState.checkScreen(from)) slideInHorizontally { it } else EnterTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutHorizontallyTo(to: Screen): ExitTransition =
    if (targetState.checkScreen(to)) slideOutHorizontally { it } else ExitTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInVerticallyFrom(from: Screen): EnterTransition =
    if (initialState.checkScreen(from)) slideInVertically { it } else EnterTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutVerticallyTo(to: Screen): ExitTransition =
    if (targetState.checkScreen(to)) slideOutVertically { it } else ExitTransition.None
