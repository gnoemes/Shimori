package com.gnoemes.shimori.data.base.database.daos

import com.gnoemes.shimori.data.base.entities.ShimoriEntity

abstract class EntityDao<in E : ShimoriEntity> {
    abstract suspend fun insert(entity: E)
    abstract suspend fun insertAll(entities: List<E>)
    abstract suspend fun update(entity: E)
    abstract suspend fun deleteEntity(entity: E)

    suspend fun insertOrUpdate(entity: E) {
        return if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
        }
    }

    open suspend fun insertOrUpdate(entities: List<E>) {
        entities.forEach { insertOrUpdate(it) }
    }
}