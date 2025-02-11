package io.github.kei_1111.withmo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kei_1111.withmo.data.local.dao.AppInfoDao
import io.github.kei_1111.withmo.data.local.dao.WidgetInfoDao
import io.github.kei_1111.withmo.data.local.entity.AppInfoEntity
import io.github.kei_1111.withmo.data.local.entity.WidgetInfoEntity

@Database(
    entities = [AppInfoEntity::class, WidgetInfoEntity::class],
    version = 7,
    exportSchema = false,
)
abstract class WithmoDatabase : RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
    abstract fun widgetInfoDao(): WidgetInfoDao
}
