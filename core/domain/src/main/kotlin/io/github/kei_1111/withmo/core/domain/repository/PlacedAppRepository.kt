package io.github.kei_1111.withmo.core.domain.repository

import io.github.kei_1111.withmo.core.model.PlacedAppInfo
import kotlinx.coroutines.flow.Flow

interface PlacedAppRepository {

    /**
     * 配置されたアプリのリストを取得
     */
    val placedAppsInfo: Flow<List<PlacedAppInfo>>

    /**
     * 配置されたアプリのリストを更新
     * @param placedAppInfos 新しい配置アプリのリスト
     */
    suspend fun updatePlacedApps(placedAppInfos: List<PlacedAppInfo>)
}
