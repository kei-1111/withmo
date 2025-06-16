package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.kei_1111.withmo.core.data.local.entity.WithmoAppInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WithmoAppInfoDao {

    @Query("SELECT * FROM withmo_app_info")
    fun getAllList(): Flow<List<WithmoAppInfoEntity>>

    @Query("SELECT * FROM withmo_app_info WHERE favorite_order != 'NotFavorite'")
    fun getFavoriteList(): Flow<List<WithmoAppInfoEntity>>

    @Query("SELECT * FROM withmo_app_info WHERE position_x IS NOT NULL AND position_y IS NOT NULL")
    fun getPlacedList(): Flow<List<WithmoAppInfoEntity>>

    @Query("SELECT * FROM withmo_app_info WHERE package_name = :packageName")
    suspend fun getByPackageName(packageName: String): WithmoAppInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(withmoAppInfo: WithmoAppInfoEntity)

    @Update
    suspend fun update(withmoAppInfo: WithmoAppInfoEntity)

    @Update
    suspend fun updateList(withmoAppList: List<WithmoAppInfoEntity>)

    @Delete
    suspend fun delete(withmoAppInfo: WithmoAppInfoEntity)
}
