package com.gnoemes.shimori.data.core.database.daos

import com.gnoemes.shimori.data.core.entities.ShimoriEntity

abstract class EntityDao<in E : ShimoriEntity> {
    abstract suspend fun insert(entity: E)
    abstract suspend fun update(entity: E)
    abstract suspend fun delete(entity: E)
    suspend fun insertOrUpdate(entity: E) {
        if (entity.id == 0L) {
            insert(entity)
        } else {
            update(entity)
        }
    }
}