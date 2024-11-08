package com.example.withmo.domain.model.user_settings

data class SortSettings(
    val sortType: SortType = SortType.ALPHABETICAL,
)

enum class SortType {
    USE_COUNT,
    ALPHABETICAL,
}
