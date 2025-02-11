package io.github.kei_1111.withmo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.kei_1111.withmo.data.local.entity.AppInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppInfoDao {

    @Query("SELECT * FROM app_info")
    fun getAllAppInfoList(): Flow<List<AppInfoEntity>>

    @Query("SELECT * FROM app_info WHERE favorite_order != 'NotFavorite'")
    fun getFavoriteAppInfoList(): Flow<List<AppInfoEntity>>

    @Query("SELECT * FROM app_info WHERE package_name = :packageName")
    suspend fun getAppInfoByPackageName(packageName: String): AppInfoEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAppInfo(appInfo: AppInfoEntity)

    @Update
    suspend fun updateAppInfo(appInfo: AppInfoEntity)

    @Update
    suspend fun updateAppInfoList(appInfoList: List<AppInfoEntity>)

    @Delete
    suspend fun deleteAppInfo(appInfo: AppInfoEntity)
}
