package io.github.kei_1111.withmo.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kei_1111.withmo.core.data.local.dao.FavoriteAppDao
import io.github.kei_1111.withmo.core.data.local.dao.PlacedAppDao
import io.github.kei_1111.withmo.core.data.local.dao.WithmoWidgetInfoDao
import io.github.kei_1111.withmo.core.data.local.entity.FavoriteAppEntity
import io.github.kei_1111.withmo.core.data.local.entity.PlacedAppEntity
import io.github.kei_1111.withmo.core.data.local.entity.WithmoWidgetInfoEntity

@Database(
    entities = [WithmoWidgetInfoEntity::class, FavoriteAppEntity::class, PlacedAppEntity::class],
    version = 10,
    exportSchema = false,
)
abstract class WithmoDatabase : RoomDatabase() {
    abstract fun withmoWidgetInfoDao(): WithmoWidgetInfoDao
    abstract fun favoriteAppDao(): FavoriteAppDao
    abstract fun placedAppDao(): PlacedAppDao
}
