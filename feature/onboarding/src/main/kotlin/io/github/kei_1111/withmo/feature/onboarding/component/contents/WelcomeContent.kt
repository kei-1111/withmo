package io.github.kei_1111.withmo.feature.onboarding.component.contents

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import io.github.kei_1111.withmo.feature.onboarding.OnboardingScreenDimensions
import io.github.kei_1111.withmo.feature.onboarding.R
import io.github.kei_1111.withmo.feature.onboarding.component.OnboardingBottomAppBarNextButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
internal fun WelcomeContent(
    onAction: (OnboardingAction) -> Unit,
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
            onAction = onAction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(Paddings.Medium),
        )
    }
}

@Composable
private fun WelcomeContentBottomAppBar(
    onAction: (OnboardingAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    OnboardingBottomAppBarNextButton(
        text = "次へ",
        onClick = { onAction(OnboardingAction.OnNextButtonClick) },
        modifier = modifier,
    )
}

@Composable
@Preview
private fun WelcomeContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WelcomeContent(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun WelcomeContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WelcomeContent(
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun WelcomeContentBottomAppBarLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WelcomeContentBottomAppBar(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
@Preview
private fun WelcomeContentBottomAppBarDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WelcomeContentBottomAppBar(
            onAction = {},
            modifier = Modifier.fillMaxWidth(),
        )
    }
}
