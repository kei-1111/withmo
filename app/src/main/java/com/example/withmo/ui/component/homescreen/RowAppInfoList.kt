package com.example.withmo.ui.component.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList

@Composable
fun RowAppList(
    context: Context,
    appList: ImmutableList<AppInfo>,
    appIconSize: Float,
    appIconPadding: Float,
    showAppName: Boolean,
    showSetting: () -> Unit,
    modifier: Modifier = Modifier,
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .height((appIconSize + UiConfig.AppIconTextHeight).dp),
        horizontalArrangement = Arrangement.spacedBy(appIconPadding.dp),
        contentPadding = PaddingValues(horizontal = UiConfig.MediumPadding),
    ) {
        items(appList.size) { index ->
            AppInfoItem(
                context = context,
                appInfo = appList[index],
                appIconSize = appIconSize,
                showAppName = showAppName,
                showSetting = showSetting,
            )
        }
    }
}
