package io.github.kei_1111.withmo.ui.screens.onboarding.component.contents

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.ui.component.BodyMediumText
import io.github.kei_1111.withmo.ui.component.DisplayMediumText
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingScreenDimensions
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingAction
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Composable
internal fun FinishContent(
    onEvent: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    BackHandler {
        onEvent(OnboardingAction.OnPreviousButtonClick)
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
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun FinishContentBottomAppBar(
    onEvent: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingAction.OnPreviousButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
        OnboardingBottomAppBarNextButton(
            text = "はじめる",
            onClick = { onEvent(OnboardingAction.OnNextButtonClick) },
            modifier = Modifier.weight(Weights.Medium),
        )
    }
}
