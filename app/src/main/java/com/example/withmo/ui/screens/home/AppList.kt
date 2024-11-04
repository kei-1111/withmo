package com.example.withmo.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.ImeAction
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.AppItem
import com.example.withmo.ui.component.CenteredMessage
import com.example.withmo.ui.component.WithmoTextField
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Composable
fun AppList(
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
                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(UiConfig.AppListScreenGridColums),
                    verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                    horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                    contentPadding = PaddingValues(
                        top = UiConfig.ExtraSmallPadding,
                        bottom = UiConfig.MediumPadding,
                    ),
                ) {
                    items(resultAppList.size) { index ->
                        AppItem(
                            appInfo = resultAppList[index],
                            appIconShape = appIconShape,
                            onClick = { onClick(resultAppList[index]) },
                            onLongClick = { onLongClick(resultAppList[index]) },
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                }
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
private fun WithmoSearchTextField(
    value: String,
    onValueChange: (String) -> Unit,
    action: () -> Unit,
    modifier: Modifier = Modifier,
) {
    WithmoTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        label = "アプリを検索",
        action = action,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = { action() }),
    )
}
