package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.db.api.daos.EntityDao

interface SqlDelightQueryableDao<in E : ShimoriEntity> {
    val db: ShimoriDB
}

interface SqlDelightEntityDao<in E : ShimoriEntity> : EntityDao<E>, SqlDelightQueryableDao<E> {
    override fun insert(entities: List<E>) {
        db.transaction {
            for (entity in entities) {
                insert(entity)
            }
        }
    }

    override fun upsert(entities: List<E>) {
        db.transaction {
            for (entity in entities) {
                upsert(entity)
            }
        }
    }
}