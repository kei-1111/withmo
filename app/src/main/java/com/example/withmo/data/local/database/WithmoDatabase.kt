package com.example.withmo.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.withmo.data.local.dao.AppInfoDao
import com.example.withmo.data.local.entity.AppInfoEntity

@Database(entities = [AppInfoEntity::class], version = 4, exportSchema = false)
abstract class WithmoDatabase : RoomDatabase() {
    abstract fun appInfoDao(): AppInfoDao
}
