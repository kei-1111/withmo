package io.github.kei_1111.withmo.core.model

import androidx.compose.ui.geometry.Offset

/**
 * 配置されたアプリ情報を表すクラス
 * @param id 配置アプリの一意識別子（UUID）
 * @param info アプリの基本情報
 * @param position 画面上の配置位置
 */
data class PlacedAppInfo(
    override val id: String,
    override var position: Offset,
    val info: AppInfo,
) : PlaceableItem
