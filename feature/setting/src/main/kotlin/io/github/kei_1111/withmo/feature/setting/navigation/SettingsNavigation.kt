package io.github.kei_1111.withmo.feature.setting.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import io.github.kei_1111.withmo.core.ui.navigation.AppIconSettings
import io.github.kei_1111.withmo.core.ui.navigation.ClockSettings
import io.github.kei_1111.withmo.core.ui.navigation.FavoriteAppSettings
import io.github.kei_1111.withmo.core.ui.navigation.Home
import io.github.kei_1111.withmo.core.ui.navigation.NotificationSettings
import io.github.kei_1111.withmo.core.ui.navigation.Settings
import io.github.kei_1111.withmo.core.ui.navigation.SettingsGraph
import io.github.kei_1111.withmo.core.ui.navigation.SideButtonSettings
import io.github.kei_1111.withmo.core.ui.navigation.SortSettings
import io.github.kei_1111.withmo.core.ui.navigation.ThemeSettings
import io.github.kei_1111.withmo.core.ui.navigation.slideInVerticallyFrom
import io.github.kei_1111.withmo.core.ui.navigation.slideOutVerticallyTo
import io.github.kei_1111.withmo.feature.setting.screen.app_icon.AppIconSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.clock.ClockSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.favorite_app.FavoriteAppSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.notification.NotificationSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.root.SettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.side_button.SideButtonSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.sort.SortSettingsScreen
import io.github.kei_1111.withmo.feature.setting.screen.theme.ThemeSettingsScreen

fun NavHostController.navigateSettings() = navigate(SettingsGraph)

fun NavHostController.navigateClockSettings() = navigate(ClockSettings)

fun NavHostController.navigateAppIconSettings() = navigate(AppIconSettings)

fun NavHostController.navigateFavoriteAppSettings() = navigate(FavoriteAppSettings)

fun NavHostController.navigateSideButtonSettings() = navigate(SideButtonSettings)

fun NavHostController.navigateSortSettings() = navigate(SortSettings)

fun NavHostController.navigateNotificationSettings() = navigate(NotificationSettings)

fun NavHostController.navigateThemeSettings() = navigate(ThemeSettings)

fun NavGraphBuilder.settingsGraph(
    navigateBack: () -> Unit,
    navigateClockSettings: () -> Unit,
    navigateAppIconSettings: () -> Unit,
    navigateFavoriteAppSettings: () -> Unit,
    navigateSideButtonSettings: () -> Unit,
    navigateSortSettings: () -> Unit,
    navigateNotificationSettings: () -> Unit,
    navigateThemeSettings: () -> Unit,
) {
    navigation<SettingsGraph>(startDestination = Settings) {
        composable<Settings>(
            enterTransition = { slideInVerticallyFrom(Home) },
            exitTransition = { slideOutVerticallyTo(Home) },
        ) {
            SettingsScreen(
                navigateClockSettings = navigateClockSettings,
                navigateAppIconSettings = navigateAppIconSettings,
                navigateFavoriteAppSettings = navigateFavoriteAppSettings,
                navigateSideButtonSettings = navigateSideButtonSettings,
                navigateSortSettings = navigateSortSettings,
                navigateNotificationSettings = navigateNotificationSettings,
                navigateThemeSettings = navigateThemeSettings,
                navigateBack = navigateBack,
            )
        }
        composable<ClockSettings> {
            ClockSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<AppIconSettings> {
            AppIconSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<FavoriteAppSettings> {
            FavoriteAppSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<SideButtonSettings> {
            SideButtonSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<SortSettings> {
            SortSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<NotificationSettings> {
            NotificationSettingsScreen(
                navigateBack = navigateBack,
            )
        }
        composable<ThemeSettings> {
            ThemeSettingsScreen(
                navigateBack = navigateBack,
            )
        }
    }
}
