package io.github.kei_1111.withmo.feature.onboarding.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.feature.onboarding.preview.OnboardingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.onboarding.preview.OnboardingLightPreviewEnvironment

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
    OnboardingLightPreviewEnvironment {
        OnboardingBottomAppBarPreviousButton(
            onClick = {},
            modifier = Modifier.height(CommonDimensions.SettingItemHeight),
        )
    }
}

@Composable
@Preview
private fun OnboardingBottomAppBarPreviousButtonDarkPreview() {
    OnboardingDarkPreviewEnvironment {
        OnboardingBottomAppBarPreviousButton(
            onClick = {},
            modifier = Modifier.height(CommonDimensions.SettingItemHeight),
        )
    }
}
