@file:Suppress("TooManyFunctions")

package io.github.kei_1111.withmo.feature.setting.root.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Apps
import androidx.compose.material.icons.rounded.ErrorOutline
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material.icons.rounded.RadioButtonChecked
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Wallpaper
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.NonRestartableComposable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import io.github.kei_1111.withmo.core.designsystem.component.BodyMediumText
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.CommonDimensions
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.IconSizes
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.ui.modifier.safeClickable
import io.github.kei_1111.withmo.feature.setting.preview.SettingDarkPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.preview.SettingLightPreviewEnvironment
import io.github.kei_1111.withmo.feature.setting.root.SettingsAction
import io.github.kei_1111.withmo.feature.setting.root.SettingsState

@Composable
internal fun SettingsScreenContent(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(Paddings.Medium),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        HomeAppSettings(
            isDefaultHomeApp = state.isDefaultHomeApp,
            onAction = onAction,
        )
        HomeScreenSettings(onAction = onAction)
        NotificationSettings(onAction = onAction)
        WallpaperSettings(onAction = onAction)
        ThemeSettings(onAction = onAction)
    }
}

@Composable
private fun HomeAppSettings(
    onAction: (SettingsAction) -> Unit,
    isDefaultHomeApp: Boolean,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
    ) {
        LabelMediumText(
            text = "ホームアプリの設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = if (isDefaultHomeApp) {
                MaterialTheme.colorScheme.surfaceContainer
            } else {
                MaterialTheme.colorScheme.errorContainer
            },
        ) {
            Column {
                SettingItem(
                    icon = if (isDefaultHomeApp) {
                        Icons.Rounded.Home
                    } else {
                        Icons.Rounded.ErrorOutline
                    },
                    itemName = "デフォルトホームアプリ",
                    onClick = {
                        onAction(SettingsAction.OnNavigateHomeAppSettingButtonClick)
                    },
                    itemColor = if (isDefaultHomeApp) {
                        MaterialTheme.colorScheme.onSurface
                    } else {
                        MaterialTheme.colorScheme.onErrorContainer
                    },
                )
            }
        }
    }
}

@Composable
private fun HomeScreenSettings(
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
    ) {
        LabelMediumText(
            text = "ホーム画面の設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.AccessTime,
                    itemName = "時計",
                    onClick = {
                        onAction(SettingsAction.OnNavigateClockSettingsButtonClick)
                    },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Apps,
                    itemName = "アプリアイコン",
                    onClick = {
                        onAction(SettingsAction.OnNavigateAppIconSettingsButtonClick)
                    },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Star,
                    itemName = "お気に入りアプリ",
                    onClick = {
                        onAction(SettingsAction.OnNavigateFavoriteAppSettingsButtonClick)
                    },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.RadioButtonChecked,
                    itemName = "サイドボタン",
                    onClick = {
                        onAction(SettingsAction.OnNavigateSideButtonSettingsButtonClick)
                    },
                )
                SettingItemDivider()
                SettingItem(
                    icon = Icons.Rounded.Tune,
                    itemName = "並び順",
                    onClick = {
                        onAction(SettingsAction.OnNavigateSortSettingsButtonClick)
                    },
                )
            }
        }
    }
}

@Composable
private fun NotificationSettings(
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
    ) {
        LabelMediumText(
            text = "通知の設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.Notifications,
                    itemName = "通知",
                    onClick = {
                        onAction(SettingsAction.OnNavigateNotificationSettingsButtonClick)
                    },
                )
            }
        }
    }
}

@Composable
private fun WallpaperSettings(
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
    ) {
        LabelMediumText(
            text = "壁紙の設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.Wallpaper,
                    itemName = "壁紙",
                    onClick = {
                        onAction(SettingsAction.OnNavigateWallpaperSettingsButtonClick)
                    },
                )
            }
        }
    }
}

@Composable
private fun ThemeSettings(
    onAction: (SettingsAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
    ) {
        LabelMediumText(
            text = "テーマの設定",
        )
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.surfaceContainer,
        ) {
            Column {
                SettingItem(
                    icon = Icons.Rounded.Palette,
                    itemName = "テーマ",
                    onClick = {
                        onAction(SettingsAction.OnNavigateThemeSettingsButtonClick)
                    },
                )
            }
        }
    }
}

@NonRestartableComposable
@Composable
private fun SettingItemDivider(
    modifier: Modifier = Modifier,
) {
    HorizontalDivider(
        modifier = modifier.padding(
            start = Paddings.Medium + IconSizes.Medium + Paddings.Small,
        ),
    )
}

@Composable
private fun SettingItem(
    icon: ImageVector,
    itemName: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    itemColor: Color = MaterialTheme.colorScheme.onSurface,
) {
    Row(
        modifier = modifier
            .height(CommonDimensions.SettingItemHeight)
            .safeClickable { onClick() }
            .padding(horizontal = Paddings.Medium),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = itemName,
            tint = itemColor,
            modifier = Modifier.size(IconSizes.Medium),
        )
        Spacer(
            modifier = Modifier.padding(Paddings.ExtraSmall),
        )
        BodyMediumText(
            text = itemName,
            modifier = Modifier.weight(Weights.Medium),
            color = itemColor,
        )
        Icon(
            imageVector = Icons.Rounded.KeyboardArrowRight,
            contentDescription = "Next",
            tint = itemColor,
        )
    }
}

@Composable
@Preview
private fun SettingsScreenContentLightPreview() {
    SettingLightPreviewEnvironment {
        SettingsScreenContent(
            state = SettingsState(
                isDefaultHomeApp = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun SettingsScreenContentDarkPreview() {
    SettingDarkPreviewEnvironment {
        SettingsScreenContent(
            state = SettingsState(
                isDefaultHomeApp = true,
            ),
            onAction = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Composable
@Preview
private fun HomeAppSettingsLightPreview() {
    SettingLightPreviewEnvironment {
        HomeAppSettings(
            isDefaultHomeApp = true,
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun HomeAppSettingsDarkPreview() {
    SettingDarkPreviewEnvironment {
        HomeAppSettings(
            isDefaultHomeApp = false,
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun HomeScreenSettingsLightPreview() {
    SettingLightPreviewEnvironment {
        HomeScreenSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun HomeScreenSettingsDarkPreview() {
    SettingDarkPreviewEnvironment {
        HomeScreenSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun NotificationSettingsLightPreview() {
    SettingLightPreviewEnvironment {
        NotificationSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun NotificationSettingsDarkPreview() {
    SettingDarkPreviewEnvironment {
        NotificationSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun WallpaperSettingsLightPreview() {
    SettingLightPreviewEnvironment {
        WallpaperSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun WallpaperSettingsDarkPreview() {
    SettingDarkPreviewEnvironment {
        WallpaperSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun ThemeSettingsLightPreview() {
    SettingLightPreviewEnvironment {
        ThemeSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun ThemeSettingsDarkPreview() {
    SettingDarkPreviewEnvironment {
        ThemeSettings(
            onAction = {},
        )
    }
}

@Composable
@Preview
private fun SettingItemLightPreview() {
    SettingLightPreviewEnvironment {
        SettingItem(
            icon = Icons.Rounded.AccessTime,
            itemName = "時計",
            onClick = {},
        )
    }
}

@Composable
@Preview
private fun SettingItemDarkPreview() {
    SettingDarkPreviewEnvironment {
        SettingItem(
            icon = Icons.Rounded.AccessTime,
            itemName = "時計",
            onClick = {},
        )
    }
}
