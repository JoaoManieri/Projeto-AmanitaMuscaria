package br.com.manieri.amanitamuscaria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import br.com.manieri.amanitamuscaria.data.local.entities.VehicleEntryEntity

@Dao
interface VehicleEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: VehicleEntryEntity)

    @Query("SELECT * FROM vehicle_entries ORDER BY createdAt DESC")
    suspend fun getAll(): List<VehicleEntryEntity>
}
