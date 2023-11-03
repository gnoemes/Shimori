package com.gnoemes.shimori.data.db.api.daos

import com.gnoemes.shimori.data.ShimoriEntity

interface EntityDao<in E : ShimoriEntity> {
    fun insert(entity: E): Long
    fun insert(entities: List<E>)
    fun update(entity: E)
    fun upsert(entity: E): Long = upsert(entity, ::insert, ::update)
    fun upsert(entities: List<E>)
    fun delete(entity: E)
}

fun <E : ShimoriEntity> EntityDao<E>.insert(vararg entities: E) = insert(entities.toList())
fun <E : ShimoriEntity> EntityDao<E>.upsert(vararg entities: E) = upsert(entities.toList())

fun <ET : ShimoriEntity> upsert(
    entity: ET,
    insert: (ET) -> Long,
    update: (ET) -> Unit,
    onConflict: ((ET, Throwable) -> Long)? = null,
): Long {
    return try {
        if (entity.id != 0L) {
            update(entity)
            entity.id
        } else {
            insert(entity)
        }
    } catch (t: Throwable) {
        when {
            onConflict != null -> onConflict(entity, t)
            else -> throw t
        }
    }
}