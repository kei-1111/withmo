package io.github.kei_1111.withmo.ui.screens.onboarding.content

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
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingBottomAppBarPreviousButton
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.theme.UiConfig

@Composable
fun FinishContent(
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(UiConfig.DefaultWeight)
                .padding(UiConfig.MediumPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_finish_logo),
                contentDescription = "Finish",
                modifier = Modifier.size(UiConfig.OnboardingImageSize),
            )
            Spacer(modifier = Modifier.height(UiConfig.MediumPadding))
            DisplayMediumText("Completed!")
            Spacer(modifier = Modifier.height(UiConfig.MediumPadding))
            BodyMediumText("設定が完了しました！")
        }
        FinishContentBottomAppBar(
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
private fun FinishContentBottomAppBar(
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        OnboardingBottomAppBarPreviousButton(
            onClick = { onEvent(OnboardingUiEvent.NavigateToPreviousPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
        OnboardingBottomAppBarNextButton(
            text = "はじめる",
            onClick = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier.weight(UiConfig.DefaultWeight),
        )
    }
}
