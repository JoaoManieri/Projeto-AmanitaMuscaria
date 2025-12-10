package br.com.manieri.amanitamuscaria.database.repository

import br.com.manieri.amanitamuscaria.database.AppDatabase
import br.com.manieri.amanitamuscaria.database.dao.CarroDao
import br.com.manieri.amanitamuscaria.model.Carro

class CarroRepository(private val database: AppDatabase) :
    BaseRepositoryImpl<Carro, CarroDao>(database.carroDao()) {

    suspend fun getAll(): List<Carro> = dao.getAll()
}
