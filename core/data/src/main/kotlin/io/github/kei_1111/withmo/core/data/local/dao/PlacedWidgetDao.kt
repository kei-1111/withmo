package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kei_1111.withmo.core.data.local.entity.PlacedWidgetEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacedWidgetDao {

    @Query("SELECT * FROM withmo_widget_info")
    fun getAll(): Flow<List<PlacedWidgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(placedWidgetEntityList: List<PlacedWidgetEntity>)

    @Query("DELETE FROM withmo_widget_info")
    suspend fun deleteAll()
}
