package io.github.kei_1111.withmo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "withmo_widget_info")
data class PlacedWidgetEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "app_widget_provider_class_name")
    val appWidgetProviderClassName: String,

    @ColumnInfo(name = "width")
    val width: Int,

    @ColumnInfo(name = "height")
    val height: Int,

    @ColumnInfo(name = "position_x")
    val positionX: Float,

    @ColumnInfo(name = "position_y")
    val positionY: Float,
)
