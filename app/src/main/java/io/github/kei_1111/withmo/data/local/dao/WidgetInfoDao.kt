package io.github.kei_1111.withmo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.kei_1111.withmo.data.local.entity.WidgetInfoEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface WidgetInfoDao {

    @Query("SELECT * FROM widget_info")
    fun getAllWidgets(): Flow<List<WidgetInfoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(widgetInfoEntityList: List<WidgetInfoEntity>)

    @Update
    suspend fun update(widgetInfoEntityList: List<WidgetInfoEntity>)

    @Delete
    suspend fun delete(widgetInfoEntityList: List<WidgetInfoEntity>)
}
