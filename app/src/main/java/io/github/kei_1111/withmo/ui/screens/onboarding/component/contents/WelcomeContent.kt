package io.github.kei_1111.withmo.ui.screens.onboarding.component.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.kei_1111.withmo.ui.screens.onboarding.OnboardingUiEvent
import io.github.kei_1111.withmo.ui.screens.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights

@Composable
internal fun WelcomeContent(
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
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
                painter = painterResource(id = R.drawable.onboarding_welcome_logo),
                contentDescription = "Welcome",
                modifier = Modifier.size(OnboardingScreenDimensions.WelcomeImageSize),
            )
            Spacer(modifier = Modifier.height(Paddings.Medium))
            DisplayMediumText("Let's get started!")
            Spacer(modifier = Modifier.height(Paddings.Medium))
            BodyMediumText("お気に入りアプリの登録と表示モデル設定をして")
            BodyMediumText("今すぐwithmoを始めよう！")
        }
        WelcomeContentBottomAppBar(
            onEvent = onEvent,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun WelcomeContentBottomAppBar(
    onEvent: (OnboardingUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingBottomAppBarNextButton(
        text = "次へ",
        onClick = { onEvent(OnboardingUiEvent.OnNextButtonClick) },
        modifier = modifier,
    )
}
