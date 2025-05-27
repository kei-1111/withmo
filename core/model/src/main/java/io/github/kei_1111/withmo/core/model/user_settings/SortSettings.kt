package io.github.kei_1111.withmo.core.model.user_settings

data class SortSettings(
    val sortType: SortType = SortType.ALPHABETICAL,
)

enum class SortType {
    USE_COUNT,
    ALPHABETICAL,
}
