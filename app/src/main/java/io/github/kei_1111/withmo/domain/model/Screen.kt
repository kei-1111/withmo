package io.github.kei_1111.withmo.domain.model

sealed interface Screen {
    data object Onboarding : Screen
    data object Home : Screen
    data object Settings : Screen
    data object NotificationSettings : Screen
    data object ClockSettings : Screen
    data object AppIconSettings : Screen
    data object FavoriteAppSettings : Screen
    data object SideButtonSettings : Screen
    data object SortSettings : Screen
    data object ThemeSettings : Screen
}
