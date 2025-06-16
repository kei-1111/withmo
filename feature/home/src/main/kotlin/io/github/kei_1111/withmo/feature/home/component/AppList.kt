package io.github.kei_1111.withmo.feature.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import io.github.kei_1111.withmo.core.designsystem.component.App
import io.github.kei_1111.withmo.core.designsystem.component.LabelMediumText
import io.github.kei_1111.withmo.core.designsystem.component.theme.DesignConstants
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Paddings
import io.github.kei_1111.withmo.core.designsystem.component.theme.dimensions.Weights
import io.github.kei_1111.withmo.core.model.AppInfo
import io.github.kei_1111.withmo.core.model.WithmoAppInfo
import io.github.kei_1111.withmo.feature.home.HomeAction
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AppList(
    appList: ImmutableList<WithmoAppInfo>,
    appIconShape: Shape,
    isNavigateSettingsButtonShown: Boolean,
    onAppClick: (AppInfo) -> Unit,
    onAppLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.info.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.info.packageName == context.packageName }
        .toPersistentList()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(Paddings.Medium),
    ) {
        if (launchableAppList.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "アプリ一覧",
                )
                CustomAppInfoGridLayout(
                    items = launchableAppList,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    onClick = onAppClick,
                    onLongClick = onAppLongClick,
                )
            }
        }
        if (settingApp.isNotEmpty() && !isNavigateSettingsButtonShown) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Paddings.ExtraSmall),
            ) {
                LabelMediumText(
                    text = "カスタマイズ",
                )
                CustomAppInfoGridLayout(
                    items = settingApp,
                    columns = DesignConstants.AppListGridColums,
                    appIconShape = appIconShape,
                    onClick = onAppClick,
                    onLongClick = onAppLongClick,
                )
            }
        }
    }
}

@Composable
private fun CustomAppInfoGridLayout(
    items: ImmutableList<WithmoAppInfo>,
    columns: Int,
    appIconShape: Shape,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = Paddings.Large,
    horizontalSpacing: Dp = Paddings.Large,
    contentPadding: PaddingValues = PaddingValues(bottom = Paddings.ExtraSmall),
) {
    Column(
        modifier = modifier
            .padding(contentPadding),
        verticalArrangement = Arrangement.spacedBy(verticalSpacing),
    ) {
        items.chunked(columns).forEach { rowItems ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(horizontalSpacing),
            ) {
                rowItems.forEach { item ->
                    Box(
                        modifier = Modifier
                            .weight(Weights.Medium),
                        contentAlignment = Alignment.Center,
                    ) {
                        App(
                            appInfo = item.info,
                            onClick = { onClick(item.info) },
                            onLongClick = { onLongClick(item.info) },
                            appIconShape = appIconShape,
                        )
                    }
                }
                if (rowItems.size < columns) {
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(Weights.Medium))
                    }
                }
            }
        }
    }
}