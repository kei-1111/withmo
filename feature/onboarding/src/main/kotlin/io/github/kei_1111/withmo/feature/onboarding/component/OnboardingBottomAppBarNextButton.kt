package io.github.kei_1111.withmo.feature.onboarding.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.height
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Alphas
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.feature.onboarding.OnboardingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.onboarding.OnboardingLightPreviewEnvironment

@Composable
internal fun OnboardingBottomAppBarNextButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    FilledTonalButton(
        onClick = onClick,
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight),
        enabled = enabled,
    ) {
        BodyMediumText(
            text = text,
            color = if (enabled) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurface.copy(alpha = Alphas.Disabled)
            },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun OnboardingBottomAppBarNextButtonLightPreview() {
    OnboardingLightPreviewEnvironment {
        OnboardingBottomAppBarNextButton(
            text = "次へ",
            onClick = {},
            enabled = true,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun OnboardingBottomAppBarNextButtonDarkPreview() {
    OnboardingDarkPreviewEnvironment {
        OnboardingBottomAppBarNextButton(
            text = "次へ",
            onClick = {},
            enabled = true,
        )
    }
}
