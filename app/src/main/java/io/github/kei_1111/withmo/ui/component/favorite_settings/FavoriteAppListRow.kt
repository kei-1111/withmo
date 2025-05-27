package io.github.kei_1111.withmo.ui.component.favorite_settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import io.github.kei_1111.withmo.core.common.AppConstants
import io.github.kei_1111.withmo.domain.model.AppInfo
import io.github.kei_1111.withmo.ui.theme.dimensions.Paddings
import io.github.kei_1111.withmo.ui.theme.dimensions.Weights
import kotlinx.collections.immutable.ImmutableList

@Composable
fun FavoriteAppListRow(
    favoriteAppList: ImmutableList<AppInfo>,
    removeSelectedAppList: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    appIconShape: Shape = CircleShape,
) {
    Row(
        modifier = modifier
            .padding(
                horizontal = Paddings.Medium,
                vertical = Paddings.Small,
            ),
        horizontalArrangement = Arrangement.spacedBy(Paddings.Large),
    ) {
        favoriteAppList.forEach {
            FavoriteAppSelectorItem(
                appInfo = it,
                isSelected = true,
                addSelectedAppList = { },
                removeSelectedAppList = { removeSelectedAppList(it) },
                onClick = { removeSelectedAppList(it) },
                backgroundColor = MaterialTheme.colorScheme.surface,
                modifier = Modifier.weight(Weights.Medium),
                appIconShape = appIconShape,
            )
        }

        repeat(AppConstants.FavoriteAppListMaxSize - favoriteAppList.size) {
            EmptyAppItem(
                modifier = Modifier.weight(Weights.Medium),
            )
        }
    }
}
