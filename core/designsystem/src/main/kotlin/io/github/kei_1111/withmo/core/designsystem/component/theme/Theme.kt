@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.core.designsystem.component.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.staticCompositionLocalOf
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.core.util.TimeUtils

private val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
)

private val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
)

private const val THEME_ANIMATION_DURATION = 500

@Suppress("LongMethod")
@Composable
private fun animatedColorScheme(
    targetScheme: ColorScheme,
): ColorScheme {
    val primary by animateColorAsState(
        targetValue = targetScheme.primary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "primary",
    )
    val onPrimary by animateColorAsState(
        targetValue = targetScheme.onPrimary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onPrimary",
    )
    val primaryContainer by animateColorAsState(
        targetValue = targetScheme.primaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "primaryContainer",
    )
    val onPrimaryContainer by animateColorAsState(
        targetValue = targetScheme.onPrimaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onPrimaryContainer",
    )
    val secondary by animateColorAsState(
        targetValue = targetScheme.secondary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "secondary",
    )
    val onSecondary by animateColorAsState(
        targetValue = targetScheme.onSecondary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onSecondary",
    )
    val secondaryContainer by animateColorAsState(
        targetValue = targetScheme.secondaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "secondaryContainer",
    )
    val onSecondaryContainer by animateColorAsState(
        targetValue = targetScheme.onSecondaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onSecondaryContainer",
    )
    val tertiary by animateColorAsState(
        targetValue = targetScheme.tertiary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "tertiary",
    )
    val onTertiary by animateColorAsState(
        targetValue = targetScheme.onTertiary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onTertiary",
    )
    val tertiaryContainer by animateColorAsState(
        targetValue = targetScheme.tertiaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "tertiaryContainer",
    )
    val onTertiaryContainer by animateColorAsState(
        targetValue = targetScheme.onTertiaryContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onTertiaryContainer",
    )
    val error by animateColorAsState(
        targetValue = targetScheme.error,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "error",
    )
    val onError by animateColorAsState(
        targetValue = targetScheme.onError,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onError",
    )
    val errorContainer by animateColorAsState(
        targetValue = targetScheme.errorContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "errorContainer",
    )
    val onErrorContainer by animateColorAsState(
        targetValue = targetScheme.onErrorContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onErrorContainer",
    )
    val background by animateColorAsState(
        targetValue = targetScheme.background,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "background",
    )
    val onBackground by animateColorAsState(
        targetValue = targetScheme.onBackground,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onBackground",
    )
    val surface by animateColorAsState(
        targetValue = targetScheme.surface,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surface",
    )
    val onSurface by animateColorAsState(
        targetValue = targetScheme.onSurface,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onSurface",
    )
    val surfaceVariant by animateColorAsState(
        targetValue = targetScheme.surfaceVariant,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceVariant",
    )
    val onSurfaceVariant by animateColorAsState(
        targetValue = targetScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "onSurfaceVariant",
    )
    val surfaceTint by animateColorAsState(
        targetValue = targetScheme.surfaceTint,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceTint",
    )
    val outline by animateColorAsState(
        targetValue = targetScheme.outline,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "outline",
    )
    val outlineVariant by animateColorAsState(
        targetValue = targetScheme.outlineVariant,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "outlineVariant",
    )
    val scrim by animateColorAsState(
        targetValue = targetScheme.scrim,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "scrim",
    )
    val inverseSurface by animateColorAsState(
        targetValue = targetScheme.inverseSurface,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "inverseSurface",
    )
    val inverseOnSurface by animateColorAsState(
        targetValue = targetScheme.inverseOnSurface,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "inverseOnSurface",
    )
    val inversePrimary by animateColorAsState(
        targetValue = targetScheme.inversePrimary,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "inversePrimary",
    )
    val surfaceDim by animateColorAsState(
        targetValue = targetScheme.surfaceDim,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceDim",
    )
    val surfaceBright by animateColorAsState(
        targetValue = targetScheme.surfaceBright,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceBright",
    )
    val surfaceContainerLowest by animateColorAsState(
        targetValue = targetScheme.surfaceContainerLowest,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceContainerLowest",
    )
    val surfaceContainerLow by animateColorAsState(
        targetValue = targetScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceContainerLow",
    )
    val surfaceContainer by animateColorAsState(
        targetValue = targetScheme.surfaceContainer,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceContainer",
    )
    val surfaceContainerHigh by animateColorAsState(
        targetValue = targetScheme.surfaceContainerHigh,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceContainerHigh",
    )
    val surfaceContainerHighest by animateColorAsState(
        targetValue = targetScheme.surfaceContainerHighest,
        animationSpec = tween(durationMillis = THEME_ANIMATION_DURATION),
        label = "surfaceContainerHighest",
    )

    return ColorScheme(
        primary = primary,
        onPrimary = onPrimary,
        primaryContainer = primaryContainer,
        onPrimaryContainer = onPrimaryContainer,
        inversePrimary = inversePrimary,
        secondary = secondary,
        onSecondary = onSecondary,
        secondaryContainer = secondaryContainer,
        onSecondaryContainer = onSecondaryContainer,
        tertiary = tertiary,
        onTertiary = onTertiary,
        tertiaryContainer = tertiaryContainer,
        onTertiaryContainer = onTertiaryContainer,
        background = background,
        onBackground = onBackground,
        surface = surface,
        onSurface = onSurface,
        surfaceVariant = surfaceVariant,
        onSurfaceVariant = onSurfaceVariant,
        surfaceTint = surfaceTint,
        inverseSurface = inverseSurface,
        inverseOnSurface = inverseOnSurface,
        error = error,
        onError = onError,
        errorContainer = errorContainer,
        onErrorContainer = onErrorContainer,
        outline = outline,
        outlineVariant = outlineVariant,
        scrim = scrim,
        surfaceBright = surfaceBright,
        surfaceDim = surfaceDim,
        surfaceContainer = surfaceContainer,
        surfaceContainerHigh = surfaceContainerHigh,
        surfaceContainerHighest = surfaceContainerHighest,
        surfaceContainerLow = surfaceContainerLow,
        surfaceContainerLowest = surfaceContainerLowest,
    )
}

internal val LocalColorScheme = staticCompositionLocalOf { lightScheme }
internal val LocalTypography = staticCompositionLocalOf { Typography }
internal val LocalShapes = staticCompositionLocalOf { Shapes }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WithmoTheme(
    themeType: ThemeType,
    content: @Composable () -> Unit,
) {
    val currentTime = LocalCurrentTime.current

    val targetColorScheme = when (themeType) {
        ThemeType.TIME_BASED -> {
            if (TimeUtils.isNight(currentTime.toLocalTime())) {
                darkScheme
            } else {
                lightScheme
            }
        }
        ThemeType.LIGHT -> lightScheme
        ThemeType.DARK -> darkScheme
    }

    val animatedColorScheme = animatedColorScheme(targetColorScheme)
    val rippleConfiguration = RippleConfiguration(color = animatedColorScheme.primary)

    CompositionLocalProvider(
        LocalColorScheme provides animatedColorScheme,
        LocalTypography provides Typography,
        LocalShapes provides Shapes,
        LocalRippleConfiguration provides rippleConfiguration,
    ) { content() }
}

object WithmoTheme {
    val colorScheme: ColorScheme
        @Composable @ReadOnlyComposable
        get() = LocalColorScheme.current

    val typography: WithmoTypography
        @Composable @ReadOnlyComposable
        get() = LocalTypography.current

    val shapes: Shapes
        @Composable @ReadOnlyComposable
        get() = LocalShapes.current
}
