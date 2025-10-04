package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun WithmoCircularProgressIndicator(
    modifier: Modifier = Modifier,
    color: Color = WithmoTheme.colorScheme.primary,
    strokeWidth: Dp = ProgressIndicatorDefaults.CircularStrokeWidth,
    trackColor: Color = ProgressIndicatorDefaults.circularIndeterminateTrackColor,
    strokeCap: StrokeCap = ProgressIndicatorDefaults.CircularIndeterminateStrokeCap,
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeWidth = strokeWidth,
        trackColor = trackColor,
        strokeCap = strokeCap,
    )
}

@Preview
@Composable
private fun WithmoCircularProgressIndicatorLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WithmoCircularProgressIndicator()
    }
}

@Preview
@Composable
private fun WithmoCircularProgressIndicatorDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WithmoCircularProgressIndicator()
    }
}
