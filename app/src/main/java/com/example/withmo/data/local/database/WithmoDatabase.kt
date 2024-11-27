package com.example.withmo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.withmo.data.local.dao.AppInfoDao
import com.example.withmo.data.local.dao.WidgetInfoDao
import com.example.withmo.data.local.entity.AppInfoEntity
import com.example.withmo.data.local.entity.WidgetInfoEntity

@Database(
    entities = [AppInfoEntity::class, WidgetInfoEntity::class],
    version = 7,
    exportSchema = false,
)
abstract class WithmoDatabase : RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
    abstract fun widgetInfoDao(): WidgetInfoDao
}
