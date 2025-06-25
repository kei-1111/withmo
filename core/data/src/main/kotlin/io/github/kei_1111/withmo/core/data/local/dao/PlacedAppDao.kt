package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kei_1111.withmo.core.data.local.entity.PlacedAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacedAppDao {

    @Query("SELECT * FROM placed_app")
    fun getAll(): Flow<List<PlacedAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(placedApps: List<PlacedAppEntity>)

    @Query("DELETE FROM placed_app")
    suspend fun deleteAll()
}
