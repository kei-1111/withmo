package com.example.withmo.ui.screens.applist

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeGestures
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import com.example.withmo.R
import com.example.withmo.domain.model.AppInfo
import com.example.withmo.ui.component.homescreen.AppInfoItem
import com.example.withmo.ui.component.until.WithmoTextField
import com.example.withmo.ui.theme.Typography
import com.example.withmo.until.CONTENT_PADDING


@Composable
fun AppListScreen(
    context: Context,
    appList: List<AppInfo>,
    showSetting: () -> Unit,
) {
    var searchApp by remember { mutableStateOf("") }
    var resultAppList by remember { mutableStateOf(appList) }

    val paddingValues = WindowInsets.safeGestures.asPaddingValues()

    LaunchedEffect(appList) {
        resultAppList = appList.filter { appInfo ->
            appInfo.label.contains(searchApp)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = paddingValues.calculateTopPadding(),
                start = dimensionResource(id = R.dimen.large_padding),
                end = dimensionResource(id = R.dimen.large_padding)
            ),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large_padding))
    ) {
        WithmoTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchApp,
            onValueChange = { searchApp = it },
            label = "アプリを検索",
            search = {
                resultAppList = appList.filter { appInfo ->
                    appInfo.label.contains(searchApp)
                }
            }
        )

        if (resultAppList.isNotEmpty()) {
            LazyVerticalGrid(
                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(4),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large_padding)),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.large_padding)),
                contentPadding = PaddingValues(bottom = dimensionResource(id = R.dimen.large_padding))
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


