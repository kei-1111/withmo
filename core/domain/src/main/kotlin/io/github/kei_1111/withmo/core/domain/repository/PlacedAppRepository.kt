package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.PlacedApp
import kotlinx.coroutines.flow.Flow

interface PlacedAppRepository {

    /**
     * 配置されたアプリのリストを取得
     */
    val placedApps: Flow<List<PlacedApp>>

    /**
     * 配置されたアプリのリストを更新
     * @param placedApps 新しい配置アプリのリスト
     */
    suspend fun updatePlacedApps(placedApps: List<PlacedApp>)
}
