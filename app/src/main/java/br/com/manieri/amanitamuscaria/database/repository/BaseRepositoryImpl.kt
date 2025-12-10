package br.com.manieri.amanitamuscaria.database.repository

import br.com.manieri.amanitamuscaria.database.dao.BaseDao

abstract class BaseRepositoryImpl<T, D : BaseDao<T>>(protected val dao: D) : BaseRepository<T> {
    override suspend fun insert(entity: T) {
        dao.insert(entity)
    }

    override suspend fun update(entity: T) {
        dao.update(entity)
    }

    override suspend fun delete(entity: T) {
        dao.delete(entity)
    }
}
