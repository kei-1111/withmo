package io.github.kei_1111.withmo.core.ui.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.navigation.NavBackStackEntry

fun NavBackStackEntry.checkScreen(screen: Screen): Boolean = destination.route == screen::class.qualifiedName

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInHorizontallyFrom(from: Screen): EnterTransition =
    if (initialState.checkScreen(from)) slideInHorizontally { it } else EnterTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutHorizontallyTo(to: Screen): ExitTransition =
    if (targetState.checkScreen(to)) slideOutHorizontally { it } else ExitTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideInVerticallyFrom(from: Screen): EnterTransition =
    if (initialState.checkScreen(from)) slideInVertically { it } else EnterTransition.None

fun AnimatedContentTransitionScope<NavBackStackEntry>.slideOutVerticallyTo(to: Screen): ExitTransition =
    if (targetState.checkScreen(to)) slideOutVertically { it } else ExitTransition.None
