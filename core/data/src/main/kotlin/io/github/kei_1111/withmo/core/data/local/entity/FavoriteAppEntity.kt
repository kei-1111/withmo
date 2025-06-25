package io.github.kei_1111.withmo.core.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_app")
data class FavoriteAppEntity(
    @PrimaryKey
    @ColumnInfo(name = "package_name")
    val packageName: String,

    @ColumnInfo(name = "favorite_order")
    val favoriteOrder: Int,
)
