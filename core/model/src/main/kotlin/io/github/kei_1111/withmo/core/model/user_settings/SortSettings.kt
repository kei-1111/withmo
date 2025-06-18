package io.github.kei_1111.withmo.core.model.user_settings

import io.github.kei_1111.withmo.core.model.WithmoAppInfo

data class SortSettings(
    val sortType: SortType = SortType.ALPHABETICAL,
)

enum class SortType {
    USE_COUNT,
    ALPHABETICAL,
}

fun sortAppList(
    sortType: SortType,
    appList: List<WithmoAppInfo>,
): List<WithmoAppInfo> {
    return when (sortType) {
        SortType.USE_COUNT -> appList.sortedByDescending { it.info.useCount }
        SortType.ALPHABETICAL -> appList.sortedBy { it.info.label }
    }
}
