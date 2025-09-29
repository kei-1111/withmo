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
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Man
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.designsystem.component.WithmoClock
import io.github.kei_1111.withmo.core.designsystem.component.WithmoIconButton
import io.github.kei_1111.withmo.core.designsystem.component.WithmoVerticalSlider
import io.github.kei_1111.withmo.core.designsystem.component.modifier.withmoShadow
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.toDateTimeInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import io.github.kei_1111.withmo.core.ui.LocalCurrentTime
import io.github.kei_1111.withmo.core.util.FileUtils
import io.github.kei_1111.withmo.feature.home.HomeAction
import io.github.kei_1111.withmo.feature.home.HomeState
import io.github.kei_1111.withmo.feature.home.R

@Composable
internal fun DisplayModelContent(
    state: HomeState.Stable,
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
            .padding(horizontal = 16.dp),
    ) {
        if (state.isChangeModelScaleContentShown) {
            WithmoIconButton(
                onClick = { onAction(HomeAction.OnCloseScaleSliderButtonClick) },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .size(56.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.onSurface,
                )
            }
            ScaleSlider(
                state = state,
                onAction = onAction,
                modifier = Modifier.align(Alignment.CenterEnd),
            )
        }

        if (!state.isChangeModelScaleContentShown) {
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
                Spacer(modifier = Modifier.weight(1f))
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.Bottom),
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
}

@Composable
private fun ScaleSlider(
    state: HomeState.Stable,
    onAction: (HomeAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .withmoShadow(
                shape = MaterialTheme.shapes.medium,
            ),
        shape = MaterialTheme.shapes.medium,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
            WithmoVerticalSlider(
                value = state.currentUserSettings.modelSettings.scale,
                onValueChange = { onAction(HomeAction.OnScaleSliderChange(it)) },
                valueRange = 0.5f..1.5f,
                modifier = Modifier
                    .size(50.dp, 300.dp),
            )
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurface,
            )
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
            modifier = Modifier.size(56.dp),
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
            modifier = Modifier.size(56.dp),
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
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.ChangeCircle,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
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
            modifier = Modifier.size(56.dp),
        ) {
            Icon(
                imageVector = Icons.Rounded.Man,
                contentDescription = null,
                modifier = Modifier.size(32.dp),
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
        modifier = modifier.size(76.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        content()
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.labelSmall,
        )
    }
}

@Preview
@Composable
private fun DisplayModelContentLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        DisplayModelContent(
            state = HomeState.Stable(),
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
            state = HomeState.Stable(),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
private fun ScaleSliderLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        ScaleSlider(
            state = HomeState.Stable(),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ScaleSliderDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        ScaleSlider(
            state = HomeState.Stable(),
            onAction = {},
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
