package com.gnoemes.shimori.data.daos

import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.ShimoriEntity
import com.gnoemes.shimori.data.db.api.daos.EntityDao

interface SqlDelightEntityDao<in E : ShimoriEntity> : EntityDao<E> {
    val db: ShimoriDB

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