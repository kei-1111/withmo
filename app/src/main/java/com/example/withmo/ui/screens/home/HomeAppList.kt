package com.example.withmo.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.AppItem
import com.example.withmo.ui.component.CenteredMessage
import com.example.withmo.ui.component.LabelMediumText
import com.example.withmo.ui.component.WithmoSearchTextField
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun HomeAppList(
    onClick: (AppInfo) -> Unit,
    appList: ImmutableList<AppInfo>,
    appIconShape: Shape,
    appSearchQuery: String,
    onValueChangeAppSearchQuery: (String) -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (AppInfo) -> Unit = {},
) {
    var resultAppList by remember { mutableStateOf(appList) }

    LaunchedEffect(appList) {
        resultAppList = appList.filter { appInfo ->
            appInfo.label.contains(appSearchQuery, ignoreCase = true)
        }.toPersistentList()
    }

    Surface(
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = WindowInsets.safeGestures.asPaddingValues().calculateTopPadding(),
                    start = UiConfig.MediumPadding,
                    end = UiConfig.MediumPadding,
                ),
            verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            WithmoSearchTextField(
                modifier = Modifier.fillMaxWidth(),
                value = appSearchQuery,
                onValueChange = onValueChangeAppSearchQuery,
                action = {
                    resultAppList = appList.filter { appInfo ->
                        appInfo.label.contains(appSearchQuery, ignoreCase = true)
                    }.toPersistentList()
                },
            )
            if (resultAppList.isNotEmpty()) {
                HomeAppList(
                    appList = resultAppList,
                    appIconShape = appIconShape,
                    onClick = onClick,
                    onLongClick = onLongClick,
                    modifier = Modifier.fillMaxSize(),
                )
            } else {
                CenteredMessage(
                    message = "アプリが見つかりません",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}

@Composable
private fun HomeAppList(
    appList: ImmutableList<AppInfo>,
    appIconShape: Shape,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    val launchableAppList = appList
        .filter { it.packageName != context.packageName }
        .toPersistentList()
    val settingApp = appList
        .filter { it.packageName == context.packageName }
        .toPersistentList()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        if (launchableAppList.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
            ) {
                LabelMediumText(
                    text = "アプリ一覧",
                )
                CustomAppInfoGridLayout(
                    items = launchableAppList,
                    columns = UiConfig.AppListScreenGridColums,
                    appIconShape = appIconShape,
                    onClick = onClick,
                    onLongClick = onLongClick,
                )
            }
        }
        if (settingApp.isNotEmpty()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(UiConfig.ExtraSmallPadding),
            ) {
                LabelMediumText(
                    text = "カスタマイズ",
                )
                CustomAppInfoGridLayout(
                    items = settingApp,
                    columns = UiConfig.AppListScreenGridColums,
                    appIconShape = appIconShape,
                    onClick = onClick,
                    onLongClick = onLongClick,
                )
            }
        }
    }
}

@Composable
fun CustomAppInfoGridLayout(
    items: ImmutableList<AppInfo>,
    columns: Int,
    appIconShape: Shape,
    onClick: (AppInfo) -> Unit,
    onLongClick: (AppInfo) -> Unit,
    modifier: Modifier = Modifier,
    verticalSpacing: Dp = UiConfig.LargePadding,
    horizontalSpacing: Dp = UiConfig.LargePadding,
    contentPadding: PaddingValues = PaddingValues(
        bottom = UiConfig.ExtraSmallPadding,
    ),
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
                            .weight(UiConfig.DefaultWeight),
                        contentAlignment = Alignment.Center,
                    ) {
                        AppItem(
                            appInfo = item,
                            onClick = { onClick(item) },
                            onLongClick = { onLongClick(item) },
                            appIconShape = appIconShape,
                        )
                    }
                }
                if (rowItems.size < columns) {
                    repeat(columns - rowItems.size) {
                        Spacer(modifier = Modifier.weight(UiConfig.DefaultWeight))
                    }
                }
            }
        }
    }
}
