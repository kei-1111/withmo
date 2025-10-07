package io.github.kei_1111.withmo.feature.home.screen.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.WithmoCircularProgressIndicator
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType

@Composable
internal fun ModelLoadingDialog(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        awaitPointerEvent()
                    }
                }
            }
            .padding(16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            modifier = Modifier.size(300.dp, 100.dp),
            shape = WithmoTheme.shapes.medium,
            color = WithmoTheme.colorScheme.surface,
            shadowElevation = 5.dp,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterHorizontally),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "モデルの読込中",
                    color = WithmoTheme.colorScheme.onSurface,
                    style = WithmoTheme.typography.bodyMedium,
                )
                WithmoCircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun ModelLoadingLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ModelLoadingDialog()
    }
}

@Preview
@Composable
private fun ModelLoadingDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ModelLoadingDialog()
    }
}
