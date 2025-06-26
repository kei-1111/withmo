package io.github.kei_1111.withmo.core.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import io.github.kei_1111.withmo.core.data.local.entity.FavoriteAppEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FavoriteAppDao {

    @Query("SELECT * FROM favorite_app ORDER BY favorite_order ASC")
    fun getAll(): Flow<List<FavoriteAppEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(favoriteApps: List<FavoriteAppEntity>)

    @Query("DELETE FROM favorite_app")
    suspend fun deleteAll()
}
