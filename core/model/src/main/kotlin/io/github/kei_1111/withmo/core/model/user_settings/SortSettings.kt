package io.github.kei_1111.withmo.core.model.user_settings

import io.github.kei_1111.withmo.core.model.AppInfo

data class SortSettings(
    val sortType: SortType = SortType.ALPHABETICAL,
)

enum class SortType {
    USE_COUNT,
    ALPHABETICAL,
}

fun sortAppList(
    sortType: SortType,
    appList: List<AppInfo>,
): List<AppInfo> {
    return when (sortType) {
        SortType.USE_COUNT -> appList.sortedByDescending { it.useCount }
        SortType.ALPHABETICAL -> appList.sortedBy { it.label }
    }
}
