package br.com.manieri.amanitamuscaria.database

import androidx.room.Database
import androidx.room.RoomDatabase
import br.com.manieri.amanitamuscaria.database.dao.CarroDao
import br.com.manieri.amanitamuscaria.model.Carro

@Database(entities = [Carro::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carroDao(): CarroDao
}
