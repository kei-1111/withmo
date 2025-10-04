package io.github.kei_1111.withmo.feature.onboarding.screens.finish.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
internal fun FinishScreenContent(
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Image(
            painter = painterResource(id = R.drawable.onboarding_finish_logo),
            contentDescription = "Finish",
            modifier = Modifier.size(250.dp),
        )
        Text(
            text = "Completed!",
            color = WithmoTheme.colorScheme.onSurface,
            style = WithmoTheme.typography.displayMedium,
        )
        Text(
            text = "設定が完了しました！",
            color = WithmoTheme.colorScheme.onSurface,
            style = WithmoTheme.typography.bodyMedium,
        )
    }
}

@Composable
@Preview
private fun FinishScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        FinishScreenContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun FinishScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        FinishScreenContent(
            modifier = Modifier.fillMaxSize(),
        )
    }
}
