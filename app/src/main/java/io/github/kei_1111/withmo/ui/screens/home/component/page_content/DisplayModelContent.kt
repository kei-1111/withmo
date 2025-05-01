package io.github.kei_1111.withmo.ui.screens.home.component.page_content

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
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.ui.component.LabelSmallText
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import io.github.kei_1111.withmo.utils.FileUtils

@Composable
internal fun DisplayModelContent(
    uiState: HomeUiState,
    onEvent: (HomeUiEvent) -> Unit,
    modifier: Modifier = Modifier,
) {
    val isDefaultModelFile =
        uiState.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) }

    Row(
        modifier = modifier
            .padding(horizontal = Paddings.Medium),
        verticalAlignment = Alignment.Bottom,
    ) {
        if (uiState.currentUserSettings.sideButtonSettings.isNavigateSettingsButtonShown) {
            NavigateSettingsButton(
                onClick = { onEvent(HomeUiEvent.OnNavigateSettingsButtonClick) },
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
                uiState.currentUserSettings.sideButtonSettings.isSetDefaultModelButtonShown &&
                isDefaultModelFile == false
            ) {
                SetDefaultModelButton(
                    onClick = { onEvent(HomeUiEvent.OnSetDefaultModelButtonClick) },
                )
            }
            if (uiState.currentUserSettings.sideButtonSettings.isOpenDocumentButtonShown) {
                OpenDocumentButton(
                    onClick = { onEvent(HomeUiEvent.OnOpenDocumentButtonClick) },
                )
            }
            if (uiState.currentUserSettings.sideButtonSettings.isShowScaleSliderButtonShown) {
                ShowScaleSliderButton(
                    onClick = { onEvent(HomeUiEvent.OnShowScaleSliderButtonClick) },
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
