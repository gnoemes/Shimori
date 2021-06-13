package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.model.ShimoriEntity

interface EntityInserter {
    suspend fun <E : ShimoriEntity> insertOrUpdate(dao: EntityDao<E>, entities: List<E>)
    suspend fun <E : ShimoriEntity> insertOrUpdate(dao: EntityDao<E>, entity: E): Long
}