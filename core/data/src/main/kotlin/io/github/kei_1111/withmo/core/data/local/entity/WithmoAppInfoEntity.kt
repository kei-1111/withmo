package io.github.kei_1111.withmo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withmo_app_info")
data class WithmoAppInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "notification")
    val notification: Boolean,

    @ColumnInfo(name = "use_count")
    val useCount: Int,

    @ColumnInfo(name = "favorite_order")
    val favoriteOrder: String,

    @ColumnInfo(name = "position_x")
    val positionX: Float?,

    @ColumnInfo(name = "position_y")
    val positionY: Float?,
)
