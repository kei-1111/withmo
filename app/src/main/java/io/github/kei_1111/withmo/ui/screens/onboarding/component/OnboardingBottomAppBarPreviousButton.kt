package io.github.kei_1111.withmo.ui.screens.onboarding.component

import androidx.compose.foundation.layout.height
import androidx.compose.material3.OutlinedButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.theme.dimensions.CommonDimensions

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
