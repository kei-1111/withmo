package io.github.kei_1111.withmo.core.designsystem.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

@Composable
fun WithmoIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Box(
        modifier = modifier
            .withmoShadow(
                shape = CircleShape,
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = CircleShape,
            )
            .safeClickable { onClick() },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WithmoIconButtonLightPreview() {
    DesignSystemLightPreviewEnvironment {
        WithmoIconButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun WithmoIconButtonDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        WithmoIconButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}
