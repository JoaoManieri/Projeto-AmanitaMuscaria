package br.com.manieri.amanitamuscaria.database.dao

import androidx.room.Dao
import br.com.manieri.amanitamuscaria.model.Carro
import br.com.manieri.amanitamuscaria.util.Constants

@Dao
interface CarroDao : BaseDao<Carro> {
    override val tableName: String
        get() = Constants.TABLE_CARROS

}