package com.example.withmo.ui.screens.onboarding.content

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
import com.example.withmo.R
import com.example.withmo.ui.component.BodyMediumText
import com.example.withmo.ui.component.DisplayMediumText
import com.example.withmo.ui.screens.onboarding.OnboardingBottomAppBarNextButton
import com.example.withmo.ui.screens.onboarding.OnboardingUiEvent
import com.example.withmo.ui.theme.UiConfig

@Composable
fun WelcomeContent(
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
                painter = painterResource(id = R.drawable.onboarding_welcome_logo),
                contentDescription = "Welcome",
                modifier = Modifier.size(UiConfig.OnboardingImageSize),
            )
            Spacer(modifier = Modifier.height(UiConfig.MediumPadding))
            DisplayMediumText("Let's get started!")
            Spacer(modifier = Modifier.height(UiConfig.MediumPadding))
            BodyMediumText("お気に入りアプリの登録と表示モデル設定をして")
            BodyMediumText("今すぐwithmoを始めよう！")
        }
        WelcomeContentBottomAppBar(
            navigateToNextPage = { onEvent(OnboardingUiEvent.NavigateToNextPage) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(UiConfig.MediumPadding),
        )
    }
}

@Composable
private fun WelcomeContentBottomAppBar(
    navigateToNextPage: () -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingBottomAppBarNextButton(
        text = "次へ",
        onClick = navigateToNextPage,
        modifier = modifier,
    )
}
