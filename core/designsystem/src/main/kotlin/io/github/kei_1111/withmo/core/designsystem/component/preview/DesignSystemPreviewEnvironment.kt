package io.github.kei_1111.withmo.core.designsystem.component.preview

import androidx.compose.runtime.Composable
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.preview.PreviewEnvironment

@Composable
fun DesignSystemLightPreviewEnvironment(
    content: @Composable () -> Unit,
) {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.LIGHT) {
            content()
        }
    }
}

@Composable
fun DesignSystemDarkPreviewEnvironment(
    content: @Composable () -> Unit,
) {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.DARK) {
            content()
        }
    }
}
