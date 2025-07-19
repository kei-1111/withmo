package io.github.kei_1111.withmo.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import io.github.kei_1111.withmo.core.model.AppInfo
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

val LocalAppList = compositionLocalOf<ImmutableList<AppInfo>> { persistentListOf() }

@Composable
fun AppListProvider(
    appList: ImmutableList<AppInfo>,
    content: @Composable () -> Unit,
) {
    CompositionLocalProvider(
        LocalAppList provides appList,
    ) {
        content()
    }
}
