package io.github.kei_1111.withmo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placed_app")
data class PlacedAppEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String, // UUID

    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "position_x")
    val positionX: Float,

    @ColumnInfo(name = "position_y")
    val positionY: Float,
)
