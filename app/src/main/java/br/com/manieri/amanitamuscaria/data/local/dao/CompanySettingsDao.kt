package br.com.manieri.amanitamuscaria.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import br.com.manieri.amanitamuscaria.data.local.entities.CompanySettingsEntity

@Dao
interface CompanySettingsDao {
    @Query("SELECT * FROM company_settings WHERE id = 0 LIMIT 1")
    suspend fun get(): CompanySettingsEntity?

    @Upsert
    suspend fun upsert(settings: CompanySettingsEntity)
}
