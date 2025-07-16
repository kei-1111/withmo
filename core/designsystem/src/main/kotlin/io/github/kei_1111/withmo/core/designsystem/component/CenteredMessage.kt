package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment

@Composable
fun CenteredMessage(
    message: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        BodyMediumText(
            text = message,
        )
    }
}

@Preview
@Composable
private fun CenteredMessageLightPreview() {
    DesignSystemLightPreviewEnvironment {
        CenteredMessage(
            message = "This is a centered message.",
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun CenteredMessageDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        CenteredMessage(
            message = "This is a centered message.",
            modifier = Modifier.fillMaxSize(),
        )
    }
}
