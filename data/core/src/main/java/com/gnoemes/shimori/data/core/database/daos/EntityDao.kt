package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.ShimoriEntity

abstract class EntityDao<in E : ShimoriEntity> {
    abstract suspend fun insert(entity: E)
    open suspend fun update(entity: E) = insert(entity)
    abstract suspend fun deleteEntity(entity: E)

    open suspend fun insertAll(entities: List<E>) = entities.forEach { insert(it) }

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