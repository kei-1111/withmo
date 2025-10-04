package io.github.kei_1111.withmo.core.ui.navigation

import kotlinx.serialization.Serializable

interface NavigationRoute

interface Graph : NavigationRoute

interface Screen : NavigationRoute

@Serializable
data object Home : Screen

@Serializable
data object OnboardingGraph : Graph

@Serializable
data object Welcome : Screen

@Serializable
data object SelectFavoriteApp : Screen

@Serializable
data object SelectDisplayModel : Screen

@Serializable
data object Finish : Screen

@Serializable
data object SettingsGraph : Graph

@Serializable
data object Settings : Screen

@Serializable
data object NotificationSettings : Screen

@Serializable
data object ClockSettings : Screen

@Serializable
data object AppIconSettings : Screen

@Serializable
data object FavoriteAppSettings : Screen

@Serializable
data object SideButtonSettings : Screen

@Serializable
data object SortSettings : Screen

@Serializable
data object ThemeSettings : Screen
