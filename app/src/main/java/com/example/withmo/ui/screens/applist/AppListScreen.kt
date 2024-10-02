package com.example.withmo.ui.screens.applist

import android.content.Context
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
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.homescreen.AppInfoItem
import com.example.withmo.ui.component.until.WithmoTextField
import com.example.withmo.ui.theme.Typography
import com.example.withmo.ui.theme.UiConfig
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toPersistentList

@Suppress("ModifierMissing")
@Composable
fun AppListScreen(
    context: Context,
    appList: ImmutableList<AppInfo>,
    showSetting: () -> Unit,
) {
    var searchApp by remember { mutableStateOf("") }
    var resultAppList by remember { mutableStateOf(appList) }

    val paddingValues = WindowInsets.safeGestures.asPaddingValues()

    LaunchedEffect(appList) {
        resultAppList = appList.filter { appInfo ->
            appInfo.label.contains(searchApp)
        }.toPersistentList()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = UiConfig.MediumPadding,
                end = UiConfig.MediumPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(UiConfig.MediumPadding),
    ) {
        WithmoSearchTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchApp,
            onValueChange = { searchApp = it },
            action = {
                resultAppList = appList.filter { appInfo ->
                    appInfo.label.contains(searchApp)
                }.toPersistentList()
            },
        )

        if (resultAppList.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(UiConfig.AppListScreenGridColums),
                verticalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                horizontalArrangement = Arrangement.spacedBy(UiConfig.LargePadding),
                contentPadding = PaddingValues(bottom = UiConfig.LargePadding),
            ) {
                items(resultAppList.size) { index ->
                    AppInfoItem(
                        context = context,
                        appInfo = resultAppList[index],
                        showSetting = showSetting,
                    )
                }
            }
        } else {
            Text(
                modifier = Modifier.fillMaxWidth(),
                text = "アプリが見つかりませんでした",
                style = Typography.bodyMedium.copy(
                    textAlign = TextAlign.Center,
                ),
            )
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
