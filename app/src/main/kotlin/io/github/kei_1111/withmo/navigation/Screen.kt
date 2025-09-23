package io.github.kei_1111.withmo.navigation

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object Welcome : Screen

    @Serializable
    data object SelectFavoriteApps : Screen

    @Serializable
    data object SelectDisplayModel : Screen

    @Serializable
    data object Finish : Screen

    @Serializable
    data object Home : Screen

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
}
