package io.github.kei_1111.withmo.feature.onboarding.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
internal fun OnboardingBottomAppBarPreviousButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
    ) {
        BodyMediumText(text = "戻る")
    }
}

@Composable
@Preview
private fun OnboardingBottomAppBarPreviousButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        OnboardingBottomAppBarPreviousButton(
            onClick = {},
            modifier = Modifier.height(CommonDimensions.SettingItemHeight),
        )
    }
}

@Composable
@Preview
private fun OnboardingBottomAppBarPreviousButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        OnboardingBottomAppBarPreviousButton(
            onClick = {},
            modifier = Modifier.height(CommonDimensions.SettingItemHeight),
        )
    }
}
