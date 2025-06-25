package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.FavoriteApp
import kotlinx.coroutines.flow.Flow

interface FavoriteAppRepository {

    /**
     * お気に入りアプリのリストを順序順で取得
     */
    val favoriteApps: Flow<List<FavoriteApp>>

    /**
     * お気に入りアプリのリストを更新
     * @param favoriteApps 新しいお気に入りアプリのリスト
     */
    suspend fun updateFavoriteApps(favoriteApps: List<FavoriteApp>)
}
