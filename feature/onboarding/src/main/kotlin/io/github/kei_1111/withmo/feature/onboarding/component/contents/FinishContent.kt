package io.github.kei_1111.withmo.feature.onboarding.component.contents

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.DisplayMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.feature.onboarding.OnboardingAction
import io.github.kei_1111.withmo.feature.onboarding.OnboardingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.onboarding.OnboardingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.onboarding.OnboardingScreenDimensions
import io.github.kei_1111.withmo.feature.onboarding.R
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarPreviousButton

@Composable
internal fun FinishContent(
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onAction(OnboardingAction.OnPreviousButtonClick)
    }

    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(Weights.Medium)
                .padding(Paddings.Medium),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_finish_logo),
                contentDescription = "Finish",
                modifier = Modifier.size(OnboardingScreenDimensions.FinishImageSize),
            )
            Spacer(modifier = Modifier.height(Paddings.Medium))
            DisplayMediumText("Completed!")
            Spacer(modifier = Modifier.height(Paddings.Medium))
            BodyMediumText("設定が完了しました！")
        }
        FinishContentBottomAppBar(
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun FinishContentBottomAppBar(
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onAction(OnboardingAction.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = "はじめる",
            onClick = { onAction(OnboardingAction.OnNextButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun FinishContentLightPreview() {
    OnboardingLightPreviewEnvironment {
        FinishContent(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun FinishContentDarkPreview() {
    OnboardingDarkPreviewEnvironment {
        FinishContent(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun FinishContentBottomAppBarLightPreview() {
    OnboardingLightPreviewEnvironment {
        FinishContentBottomAppBar(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun FinishContentBottomAppBarDarkPreview() {
    OnboardingDarkPreviewEnvironment {
        FinishContentBottomAppBar(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
