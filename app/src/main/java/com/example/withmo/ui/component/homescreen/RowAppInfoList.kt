package com.example.withmo.ui.component.homescreen

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.withmo.domain.model.AppInfo

@Composable
fun RowAppList(
    context: Context,
    appList: List<AppInfo>,
    appIconSize: Float,
    appIconPadding: Float,
    showAppName: Boolean,
    showSetting: () -> Unit,
) {
    LazyHorizontalGrid(
        modifier = Modifier
            .fillMaxWidth()
            .height((appIconSize + 22f).dp),
        rows = GridCells.Fixed(1),
        horizontalArrangement = Arrangement.spacedBy(appIconPadding.dp),
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