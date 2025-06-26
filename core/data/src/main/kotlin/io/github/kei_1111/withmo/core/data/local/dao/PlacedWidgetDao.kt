package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import io.github.kei_1111.withmo.core.data.local.entity.PlacedWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacedWidgetDao {

    @Query("SELECT * FROM withmo_widget_info")
    fun getAllList(): Flow<List<PlacedWidgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(placedWidgetEntityList: List<PlacedWidgetEntity>)

    @Update
    suspend fun update(placedWidgetEntityList: List<PlacedWidgetEntity>)

    @Delete
    suspend fun delete(placedWidgetEntityList: List<PlacedWidgetEntity>)
}
