package br.com.manieri.amanitamuscaria.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import br.com.manieri.amanitamuscaria.data.local.dao.VehicleEntryDao
import br.com.manieri.amanitamuscaria.data.local.entities.VehicleEntryEntity
import br.com.manieri.amanitamuscaria.database.dao.CarroDao
import br.com.manieri.amanitamuscaria.model.Carro

@Database(
    entities = [Carro::class, VehicleEntryEntity::class],
    version = 2,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carroDao(): CarroDao
    abstract fun vehicleEntryDao(): VehicleEntryDao
}
