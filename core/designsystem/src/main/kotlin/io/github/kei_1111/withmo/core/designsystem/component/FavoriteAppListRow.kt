package io.github.kei_1111.withmo.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.WithmoTheme
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import io.github.kei_1111.withmo.core.model.user_settings.ThemeType
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun FavoriteAppListRow(
    favoriteAppInfoList: ImmutableList<FavoriteAppInfo>,
    removeSelectedAppList: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        favoriteAppInfoList.forEach {
            FavoriteAppSelectorItem(
                appInfo = it.info,
                isSelected = true,
                addSelectedAppList = { },
                removeSelectedAppList = { removeSelectedAppList(it.info) },
                onClick = { removeSelectedAppList(it.info) },
                backgroundColor = WithmoTheme.colorScheme.surface,
                modifier = Modifier.weight(1f),
                appIconShape = appIconShape,
            )
        }

        repeat(AppConstants.FAVORITE_APP_LIST_MAX_SIZE - favoriteAppInfoList.size) {
            EmptyAppItem(
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Preview
@Composable
private fun FavoriteAppListRowLightPreview() {
    WithmoTheme(themeType = ThemeType.LIGHT) {
        FavoriteAppListRow(
            favoriteAppInfoList = emptyList<FavoriteAppInfo>().toPersistentList(),
            removeSelectedAppList = { },
        )
    }
}

@Preview
@Composable
private fun FavoriteAppListRowDarkPreview() {
    WithmoTheme(themeType = ThemeType.DARK) {
        FavoriteAppListRow(
            favoriteAppInfoList = emptyList<FavoriteAppInfo>().toPersistentList(),
            removeSelectedAppList = { },
        )
    }
}
