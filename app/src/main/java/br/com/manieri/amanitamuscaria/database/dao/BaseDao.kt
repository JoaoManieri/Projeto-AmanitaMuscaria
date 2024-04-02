package br.com.manieri.amanitamuscaria.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface BaseDao<T> {

    val tableName: String

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: T)

    @Update
    suspend fun update(entity: T)

    @Delete
    suspend fun delete(entity: T)

    @RawQuery
    suspend fun getAll(query: SupportSQLiteQuery): List<T>

    fun getAllQuery(): SupportSQLiteQuery {
        return SimpleSQLiteQuery("SELECT * FROM $tableName")
    }

}
