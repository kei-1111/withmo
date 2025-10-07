package io.github.kei_1111.withmo.feature.onboarding.screen.welcome.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.feature.onboarding.R

@Composable
internal fun WelcomeScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_welcome_logo),
            contentDescription = "Welcome",
            modifier = Modifier.size(250.dp),
        )
        Text(
            text = "Let's get started!",
            color = WithmoTheme.colorScheme.onSurface,
            style = WithmoTheme.typography.displayMedium,
        )
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = "お気に入りアプリの登録と表示モデル設定をして",
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
            Text(
                text = "今すぐwithmoを始めよう！",
                color = WithmoTheme.colorScheme.onSurface,
                style = WithmoTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
@Preview
private fun WelcomeScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        WelcomeScreenContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun WelcomeScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        WelcomeScreenContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
