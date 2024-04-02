package br.com.manieri.amanitamuscaria.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import br.com.manieri.amanitamuscaria.util.Constants

@Entity(tableName = Constants.TABLE_CARROS)
data class Carro(
    @PrimaryKey
    val id: Int,
    val name: String
)