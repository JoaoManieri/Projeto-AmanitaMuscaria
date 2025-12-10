package br.com.manieri.amanitamuscaria.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import br.com.manieri.amanitamuscaria.data.local.entities.VehicleEntryEntity

@Dao
interface VehicleEntryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entry: VehicleEntryEntity)

    @Query("SELECT * FROM vehicle_entries ORDER BY createdAt DESC")
    suspend fun getAll(): List<VehicleEntryEntity>

    @Query("SELECT * FROM vehicle_entries WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): VehicleEntryEntity?

    @Update
    suspend fun update(entry: VehicleEntryEntity)

    @Query("DELETE FROM vehicle_entries WHERE id = :id")
    suspend fun deleteById(id: String)
}
