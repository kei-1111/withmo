package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.kei_1111.withmo.core.data.local.entity.WithmoWidgetInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WithmoWidgetInfoDao {

    @Query("SELECT * FROM withmo_widget_info")
    fun getAllList(): Flow<List<WithmoWidgetInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(withmoWidgetInfoEntityList: List<WithmoWidgetInfoEntity>)

    @Update
    suspend fun update(withmoWidgetInfoEntityList: List<WithmoWidgetInfoEntity>)

    @Delete
    suspend fun delete(withmoWidgetInfoEntityList: List<WithmoWidgetInfoEntity>)
}
