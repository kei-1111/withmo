@file:Suppress("MagicNumber")

package io.github.kei_1111.withmo.core.designsystem.component.theme

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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

private const val ThemeAnimationDuration = 500

@Suppress("LongMethod")
@Composable
private fun animatedColorScheme(
    targetScheme: ColorScheme,
): ColorScheme {
    val primary by animateColorAsState(
        targetValue = targetScheme.primary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "primary",
    )
    val onPrimary by animateColorAsState(
        targetValue = targetScheme.onPrimary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onPrimary",
    )
    val primaryContainer by animateColorAsState(
        targetValue = targetScheme.primaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "primaryContainer",
    )
    val onPrimaryContainer by animateColorAsState(
        targetValue = targetScheme.onPrimaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onPrimaryContainer",
    )
    val secondary by animateColorAsState(
        targetValue = targetScheme.secondary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "secondary",
    )
    val onSecondary by animateColorAsState(
        targetValue = targetScheme.onSecondary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onSecondary",
    )
    val secondaryContainer by animateColorAsState(
        targetValue = targetScheme.secondaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "secondaryContainer",
    )
    val onSecondaryContainer by animateColorAsState(
        targetValue = targetScheme.onSecondaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onSecondaryContainer",
    )
    val tertiary by animateColorAsState(
        targetValue = targetScheme.tertiary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "tertiary",
    )
    val onTertiary by animateColorAsState(
        targetValue = targetScheme.onTertiary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onTertiary",
    )
    val tertiaryContainer by animateColorAsState(
        targetValue = targetScheme.tertiaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "tertiaryContainer",
    )
    val onTertiaryContainer by animateColorAsState(
        targetValue = targetScheme.onTertiaryContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onTertiaryContainer",
    )
    val error by animateColorAsState(
        targetValue = targetScheme.error,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "error",
    )
    val onError by animateColorAsState(
        targetValue = targetScheme.onError,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onError",
    )
    val errorContainer by animateColorAsState(
        targetValue = targetScheme.errorContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "errorContainer",
    )
    val onErrorContainer by animateColorAsState(
        targetValue = targetScheme.onErrorContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onErrorContainer",
    )
    val background by animateColorAsState(
        targetValue = targetScheme.background,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "background",
    )
    val onBackground by animateColorAsState(
        targetValue = targetScheme.onBackground,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onBackground",
    )
    val surface by animateColorAsState(
        targetValue = targetScheme.surface,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surface",
    )
    val onSurface by animateColorAsState(
        targetValue = targetScheme.onSurface,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onSurface",
    )
    val surfaceVariant by animateColorAsState(
        targetValue = targetScheme.surfaceVariant,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceVariant",
    )
    val onSurfaceVariant by animateColorAsState(
        targetValue = targetScheme.onSurfaceVariant,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "onSurfaceVariant",
    )
    val outline by animateColorAsState(
        targetValue = targetScheme.outline,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "outline",
    )
    val outlineVariant by animateColorAsState(
        targetValue = targetScheme.outlineVariant,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "outlineVariant",
    )
    val scrim by animateColorAsState(
        targetValue = targetScheme.scrim,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "scrim",
    )
    val inverseSurface by animateColorAsState(
        targetValue = targetScheme.inverseSurface,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "inverseSurface",
    )
    val inverseOnSurface by animateColorAsState(
        targetValue = targetScheme.inverseOnSurface,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "inverseOnSurface",
    )
    val inversePrimary by animateColorAsState(
        targetValue = targetScheme.inversePrimary,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "inversePrimary",
    )
    val surfaceDim by animateColorAsState(
        targetValue = targetScheme.surfaceDim,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceDim",
    )
    val surfaceBright by animateColorAsState(
        targetValue = targetScheme.surfaceBright,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceBright",
    )
    val surfaceContainerLowest by animateColorAsState(
        targetValue = targetScheme.surfaceContainerLowest,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceContainerLowest",
    )
    val surfaceContainerLow by animateColorAsState(
        targetValue = targetScheme.surfaceContainerLow,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceContainerLow",
    )
    val surfaceContainer by animateColorAsState(
        targetValue = targetScheme.surfaceContainer,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceContainer",
    )
    val surfaceContainerHigh by animateColorAsState(
        targetValue = targetScheme.surfaceContainerHigh,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
        label = "surfaceContainerHigh",
    )
    val surfaceContainerHighest by animateColorAsState(
        targetValue = targetScheme.surfaceContainerHighest,
        animationSpec = tween(durationMillis = ThemeAnimationDuration),
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
        surfaceTint = targetScheme.surfaceTint,
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

    MaterialTheme(
        colorScheme = animatedColorScheme,
        shapes = Shapes,
        typography = Typography,
        content = content,
    )
}
