package io.github.kei_1111.withmo.feature.onboarding.select_display_model.component

import android.R.attr.text
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.modifier.dashedBorder
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable
import io.github.kei_1111.withmo.feature.onboarding.select_display_model.SelectDisplayModelAction
import io.github.kei_1111.withmo.feature.onboarding.select_display_model.SelectDisplayModelState

@RequiresApi(Build.VERSION_CODES.R)
@Composable
internal fun SelectDisplayModelScreenContent(
    state: SelectDisplayModelState.Stable,
    onAction: (SelectDisplayModelAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SelectDisplayModelArea(
            state = state,
            onClick = { onAction(SelectDisplayModelAction.OnSelectDisplayModelAreaClick) },
        )
        BodyMediumText(
            text = if (state.isDefaultModel) "デフォルトモデル" else state.modelFileName,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@Composable
private fun SelectDisplayModelArea(
    state: SelectDisplayModelState.Stable,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .size(250.dp)
            .then(
                if (state.modelFileThumbnail != null && !state.isDefaultModel) {
                    Modifier.border(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                        width = 2.dp,
                    )
                } else {
                    Modifier.dashedBorder(
                        color = MaterialTheme.colorScheme.primary,
                        shape = MaterialTheme.shapes.medium,
                        strokeWidth = 2.dp,
                        gapLength = 8.dp,
                    )
                },
            )
            .clip(MaterialTheme.shapes.medium)
            .safeClickable { onClick() },
        shape = MaterialTheme.shapes.medium,
    ) {
        if (state.modelFileThumbnail != null && !state.isDefaultModel) {
            Image(
                bitmap = state.modelFileThumbnail.asImageBitmap(),
                contentDescription = "Model Thumbnail",
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                IconButton(
                    onClick = {},
                    modifier = Modifier
                        .border(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                            shape = CircleShape,
                        )
                        .size(25.dp),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        contentColor = MaterialTheme.colorScheme.primary,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Add,
                        contentDescription = "Add",
                    )
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    BodyMediumText(
                        text = "表示モデルの選択",
                        color = MaterialTheme.colorScheme.primary,
                    )
                    Text(
                        text = "VRMファイルを選択してください",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.labelMedium,
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelScreenContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SelectDisplayModelScreenContent(
            state = SelectDisplayModelState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.R)
@Composable
@Preview
private fun SelectDisplayModelScreenContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SelectDisplayModelScreenContent(
            state = SelectDisplayModelState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}
