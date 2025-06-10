package io.github.kei_1111.withmo.feature.home

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.PreviewEnvironment

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeLightPreviewEnvironment(
    content: @Composable () -> Unit,
) {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.LIGHT) {
            content()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HomeDarkPreviewEnvironment(
    content: @Composable () -> Unit,
) {
    PreviewEnvironment {
        WithmoTheme(themeType = ThemeType.DARK) {
            content()
        }
    }
}
