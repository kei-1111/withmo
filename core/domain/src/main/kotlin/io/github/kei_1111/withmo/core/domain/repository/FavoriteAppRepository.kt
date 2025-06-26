package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.FavoriteAppInfo
import kotlinx.coroutines.flow.Flow

interface FavoriteAppRepository {

    /**
     * お気に入りアプリのリストを順序順で取得
     */
    val favoriteAppsInfo: Flow<List<FavoriteAppInfo>>

    /**
     * お気に入りアプリのリストを更新
     * @param favoriteAppInfos 新しいお気に入りアプリのリスト
     */
    suspend fun updateFavoriteApps(favoriteAppInfos: List<FavoriteAppInfo>)
}
