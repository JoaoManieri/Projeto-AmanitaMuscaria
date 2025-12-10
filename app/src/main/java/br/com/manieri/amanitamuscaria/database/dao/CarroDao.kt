package br.com.manieri.amanitamuscaria.database.dao

import androidx.room.Dao
import androidx.room.Query
import br.com.manieri.amanitamuscaria.model.Carro

@Dao
interface CarroDao : BaseDao<Carro> {
    @Query("SELECT * FROM Carros")
    suspend fun getAll(): List<Carro>
}
