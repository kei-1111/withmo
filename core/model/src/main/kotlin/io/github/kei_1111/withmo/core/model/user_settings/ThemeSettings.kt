package io.github.kei_1111.withmo.core.model.user_settings

data class ThemeSettings(
    val themeType: ThemeType = ThemeType.TIME_BASED,
)

enum class ThemeType {
    TIME_BASED,
    LIGHT,
    DARK,
}
