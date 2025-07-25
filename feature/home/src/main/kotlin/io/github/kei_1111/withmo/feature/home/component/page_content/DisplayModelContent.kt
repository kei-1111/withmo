@file:Suppress("TooManyFunctions")

package io.github.kei_1111.withmo.feature.home.component.page_content

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
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
import io.github.kei_1111.withmo.core.designsystem.component.LabelSmallText
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.toDateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.core.util.FileUtils
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.R

@Composable
internal fun DisplayModelContent(
    state: HomeState,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentTime = LocalCurrentTime.current

    val isDefaultModelFile =
        state.currentUserSettings.modelFilePath.path?.let { FileUtils.isDefaultModelFile(it) }

    val topPaddingValue = WindowInsets.safeDrawing.asPaddingValues().calculateTopPadding()

    Box(
        modifier = modifier
            .padding(top = topPaddingValue)
            .padding(horizontal = Paddings.Medium),
    ) {
        if (state.currentUserSettings.clockSettings.isClockShown) {
            WithmoClock(
                clockType = state.currentUserSettings.clockSettings.clockType,
                dateTimeInfo = currentTime.toDateTimeInfo(),
                modifier = Modifier.align(Alignment.TopStart),
            )
        }

        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
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
            modifier = Modifier.size(CommonDimensions.AppIconSize),
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
            modifier = Modifier.size(CommonDimensions.AppIconSize),
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
            modifier = Modifier.size(CommonDimensions.AppIconSize),
        ) {
            Icon(
                imageVector = Icons.Rounded.ChangeCircle,
                contentDescription = null,
                modifier = Modifier.size(IconSizes.Large),
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
            modifier = Modifier.size(CommonDimensions.AppIconSize),
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier.size(IconSizes.Large),
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
            .size(CommonDimensions.AppIconSize + Paddings.Large),
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

@Preview
@Composable
private fun DisplayModelContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        DisplayModelContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun DisplayModelContentDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        DisplayModelContent(
            state = HomeState(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun NavigateSettingsButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        NavigateSettingsButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun NavigateSettingsButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        NavigateSettingsButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SetDefaultModelButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        SetDefaultModelButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun SetDefaultModelButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        SetDefaultModelButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun OpenDocumentButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        OpenDocumentButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun OpenDocumentButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        OpenDocumentButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ShowScaleSliderButtonLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ShowScaleSliderButton(
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun ShowScaleSliderButtonDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ShowScaleSliderButton(
            onClick = {},
        )
    }
}
