package io.github.kei_1111.withmo.core.designsystem.component

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemDarkPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.preview.DesignSystemLightPreviewEnvironment
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable

@Composable
fun WithmoDialog(
    title: String,
    description: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissRequest: () -> Unit,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
    ) {
        Surface(
            modifier = modifier,
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surface,
        ) {
            WithmoDialogContent(
                title = title,
                description = description,
                dismissButtonText = dismissButtonText,
                confirmButtonText = confirmButtonText,
                onConfirmClick = onConfirmClick,
                onDismissClick = onDismissClick,
            )
        }
    }
}

@Composable
private fun WithmoDialogContent(
    title: String,
    description: String,
    dismissButtonText: String,
    confirmButtonText: String,
    onDismissClick: () -> Unit,
    onConfirmClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(Paddings.Medium),
    ) {
        TitleLargeText(title)
        Spacer(modifier = Modifier.height(Paddings.Small))
        BodyMediumText(description)
        Spacer(modifier = Modifier.height(Paddings.ExtraLarge))
        Row(
            modifier = Modifier.align(Alignment.End),
            horizontalArrangement = Arrangement.spacedBy(Paddings.Large),
        ) {
            BodyMediumText(
                text = dismissButtonText,
                modifier = Modifier
                    .safeClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onDismissClick() },
            )
            BodyMediumText(
                text = confirmButtonText,
                modifier = Modifier
                    .safeClickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() },
                    ) { onConfirmClick() },
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoDialogLightPreview() {
    DesignSystemLightPreviewEnvironment {
        WithmoDialog(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
            onDismissRequest = { },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoDialogDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        WithmoDialog(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
            onDismissRequest = { },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoDialogContentLightPreview() {
    DesignSystemLightPreviewEnvironment {
        WithmoDialogContent(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview
private fun WithmoDialogContentDarkPreview() {
    DesignSystemDarkPreviewEnvironment {
        WithmoDialogContent(
            title = "タイトル",
            description = "ダイアログの内容がここに入ります。",
            dismissButtonText = "キャンセル",
            confirmButtonText = "確認",
            onDismissClick = { },
            onConfirmClick = { },
        )
    }
}
