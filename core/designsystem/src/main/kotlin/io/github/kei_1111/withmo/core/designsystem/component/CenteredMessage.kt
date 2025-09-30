package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
fun CenteredMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = message,
            color = WithmoTheme.colorScheme.onSurface,
            style = WithmoTheme.typography.bodyMedium,
        )
    }
}

@Preview
@Composable
private fun CenteredMessageLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        CenteredMessage(
            message = "This is a centered message.",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun CenteredMessageDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        CenteredMessage(
            message = "This is a centered message.",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
