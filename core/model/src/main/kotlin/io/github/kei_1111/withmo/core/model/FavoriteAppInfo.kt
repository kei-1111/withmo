package io.github.kei_1111.withmo.core.model

/**
 * お気に入りアプリ情報を表すクラス
 * @param info アプリの基本情報
 * @param favoriteOrder お気に入りの順序（0から始まる）
 */
data class FavoriteAppInfo(
    val info: AppInfo,
    val favoriteOrder: Int,
)
