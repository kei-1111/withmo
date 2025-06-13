package io.github.kei_1111.withmo.core.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import io.github.kei_1111.withmo.core.data.local.dao.WithmoAppInfoDao
import io.github.kei_1111.withmo.core.data.local.dao.WithmoWidgetInfoDao
import io.github.kei_1111.withmo.core.data.local.entity.WithmoAppInfoEntity
import io.github.kei_1111.withmo.core.data.local.entity.WithmoWidgetInfoEntity

@Database(
    entities = [WithmoAppInfoEntity::class, WithmoWidgetInfoEntity::class],
    version = 8,
    exportSchema = false,
)
abstract class WithmoDatabase : RoomDatabase() {
    abstract fun withmoAppInfoDao(): WithmoAppInfoDao
    abstract fun withmoWidgetInfoDao(): WithmoWidgetInfoDao
}
