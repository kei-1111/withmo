package com.example.withmo.domain.model

sealed interface Screen {
    data object Home : Screen
    data object Settings : Screen
}
