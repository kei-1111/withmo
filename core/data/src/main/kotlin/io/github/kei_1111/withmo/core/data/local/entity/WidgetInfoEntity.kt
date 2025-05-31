package io.github.kei_1111.withmo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "widget_info")
data class WidgetInfoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "app_widget_provider_class_name")
    val appWidgetProviderClassName: String,

    @ColumnInfo(name = "position_x")
    val positionX: Float,

    @ColumnInfo(name = "position_y")
    val positionY: Float,

    @ColumnInfo(name = "width")
    val width: Int,

    @ColumnInfo(name = "height")
    val height: Int,
)
