package br.com.manieri.amanitamuscaria.database.repository

import br.com.manieri.amanitamuscaria.database.AppDatabase
import br.com.manieri.amanitamuscaria.model.Carro

class CarroRepository(private val database: AppDatabase) :
    BaseRepositoryImpl<Carro>(database.carroDao()) {}