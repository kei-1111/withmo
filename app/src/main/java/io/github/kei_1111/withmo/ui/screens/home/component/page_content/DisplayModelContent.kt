package io.github.kei_1111.withmo.ui.screens.home.component.page_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChangeCircle
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.R
import io.github.kei_1111.withmo.common.AppConstants
import io.github.kei_1111.withmo.ui.component.WithmoIconButton
import io.github.kei_1111.withmo.ui.screens.home.HomeUiEvent
import io.github.kei_1111.withmo.ui.screens.home.HomeUiState
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.ShadowElevations
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
        modifier = modifier,
        verticalAlignment = Alignment.Bottom,
    ) {
        if (uiState.currentUserSettings.sideButtonSettings.isNavigateSettingsButtonShown) {
            Box(
                modifier = Modifier.weight(Weights.Medium),
                contentAlignment = Alignment.Center,
            ) {
                NavigateSettingsButton(
                    onClick = { onEvent(HomeUiEvent.OnNavigateSettingsButtonClick) },
                )
            }
        }
        Spacer(
            modifier = Modifier.weight(Weights.Large),
        )
        Column(
            modifier = Modifier.weight(Weights.Medium),
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
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .size(AppConstants.DefaultAppIconSize.dp)
            .clickable { onClick() },
        shape = CircleShape,
        shadowElevation = ShadowElevations.Medium,
    ) {
        Image(
            painter = painterResource(R.drawable.withmo_icon_wide),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun SetDefaultModelButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .clip(CircleShape)
            .size(AppConstants.DefaultAppIconSize.dp)
            .clickable { onClick() },
        shape = CircleShape,
        shadowElevation = ShadowElevations.Medium,
    ) {
        Image(
            painter = painterResource(R.drawable.alicia_icon),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Composable
private fun OpenDocumentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoIconButton(
        onClick = { onClick() },
        icon = Icons.Rounded.ChangeCircle,
        modifier = modifier.size(AppConstants.DefaultAppIconSize.dp),
    )
}

@Composable
private fun ShowScaleSliderButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoIconButton(
        onClick = { onClick() },
        icon = Icons.Rounded.Man,
        modifier = modifier.size(AppConstants.DefaultAppIconSize.dp),
    )
}
