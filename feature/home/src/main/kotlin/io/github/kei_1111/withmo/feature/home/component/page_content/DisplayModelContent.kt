@file:Suppress("TooManyFunctions")

package io.github.kei_1111.withmo.feature.home.component.page_content

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.component.LabelSmallText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.util.FileUtils
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.R
import io.github.kei_1111.withmo.feature.home.preview.HomeDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.home.preview.HomeLightPreviewEnvironment

@Composable
internal fun DisplayModelContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDefaultModelFile =
        state.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) }

    Row(
        modifier = modifier
            .padding(horizontal = Paddings.Medium),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (state.currentUserSettings.sideButtonSettings.isNavigateSettingsButtonShown) {
            NavigateSettingsButton(
                onClick = { onAction(HomeAction.OnNavigateSettingsButtonClick) },
            )
        }
        Spacer(
            modifier = Modifier.weight(Weights.Medium),
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Paddings.Large, Alignment.Bottom),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (
                state.currentUserSettings.sideButtonSettings.isSetDefaultModelButtonShown &&
                isDefaultModelFile == false
            ) {
                SetDefaultModelButton(
                    onClick = { onAction(HomeAction.OnSetDefaultModelButtonClick) },
                )
            }
            if (state.currentUserSettings.sideButtonSettings.isOpenDocumentButtonShown) {
                OpenDocumentButton(
                    onClick = { onAction(HomeAction.OnOpenDocumentButtonClick) },
                )
            }
            if (state.currentUserSettings.sideButtonSettings.isShowScaleSliderButtonShown) {
                ShowScaleSliderButton(
                    onClick = { onAction(HomeAction.OnShowScaleSliderButtonClick) },
                )
            }
        }
    }
}

@Composable
private fun NavigateSettingsButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideButtonContainer(
        label = "アプリ設定",
        modifier = modifier,
    ) {
        WithmoIconButton(
            onClick = onClick,
            modifier = Modifier.size(AppConstants.DefaultAppIconSize.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.withmo_icon_wide),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun SetDefaultModelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideButtonContainer(
        label = "デフォルト",
        modifier = modifier,
    ) {
        WithmoIconButton(
            onClick = onClick,
            modifier = Modifier.size(AppConstants.DefaultAppIconSize.dp),
        ) {
            Image(
                painter = painterResource(R.drawable.alicia_icon),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Composable
private fun OpenDocumentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideButtonContainer(
        label = "モデル変更",
        modifier = modifier,
    ) {
        WithmoIconButton(
            onClick = onClick,
            modifier = Modifier.size(AppConstants.DefaultAppIconSize.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ChangeCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun ShowScaleSliderButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SideButtonContainer(
        label = "サイズ変更",
        modifier = modifier,
    ) {
        WithmoIconButton(
            onClick = onClick,
            modifier = Modifier.size(AppConstants.DefaultAppIconSize.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
        }
    }
}

@Composable
private fun SideButtonContainer(
    label: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .height(AppConstants.DefaultAppIconSize.dp + Paddings.Large),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        Spacer(modifier = Modifier.weight(Weights.Medium))
        LabelSmallText(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DisplayModelContentLightPreview() {
    HomeLightPreviewEnvironment {
        DisplayModelContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun DisplayModelContentDarkPreview() {
    HomeDarkPreviewEnvironment {
        DisplayModelContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun NavigateSettingsButtonLightPreview() {
    HomeLightPreviewEnvironment {
        NavigateSettingsButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun NavigateSettingsButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        NavigateSettingsButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun SetDefaultModelButtonLightPreview() {
    HomeLightPreviewEnvironment {
        SetDefaultModelButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun SetDefaultModelButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        SetDefaultModelButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun OpenDocumentButtonLightPreview() {
    HomeLightPreviewEnvironment {
        OpenDocumentButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun OpenDocumentButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        OpenDocumentButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ShowScaleSliderButtonLightPreview() {
    HomeLightPreviewEnvironment {
        ShowScaleSliderButton(
            onClick = {},
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
private fun ShowScaleSliderButtonDarkPreview() {
    HomeDarkPreviewEnvironment {
        ShowScaleSliderButton(
            onClick = {},
        )
    }
}
