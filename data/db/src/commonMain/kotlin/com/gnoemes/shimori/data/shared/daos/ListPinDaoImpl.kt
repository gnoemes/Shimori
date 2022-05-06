package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.ListPinDao
import com.gnoemes.shimori.data.core.entities.app.ListPin
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.squareup.sqldelight.runtime.coroutines.asFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class ListPinDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger
) : ListPinDao() {

    override suspend fun insert(entity: ListPin) {
        entity.let {
            db.listPinQueries.insert(
                it.targetId,
                it.targetType
            )
        }
    }

    override suspend fun deleteEntity(entity: ListPin) {
        db.listPinQueries.deleteById(entity.id)
    }

    override suspend fun pin(targetId: Long, targetType: RateTargetType, pin: Boolean): Boolean {
        val local = db.listPinQueries.queryByTarget(targetId, targetType).executeAsOneOrNull()
        if (pin) {
            if (local != null) return true
            db.listPinQueries.insert(targetId, targetType)
        } else {
            if (local == null) return false
            db.listPinQueries.deleteById(local.id)
        }

        return pin
    }

    override suspend fun togglePin(targetId: Long, targetType: RateTargetType): Boolean {
        val local = db.listPinQueries.queryByTarget(targetId, targetType).executeAsOneOrNull()
        return if (local != null) {
            db.listPinQueries.deleteById(local.id)
            false
        } else {
            db.listPinQueries.insert(targetId, targetType)
            true
        }
    }

    override fun observePinExist(targetId: Long, targetType: RateTargetType): Flow<Boolean> {
        return db.listPinQueries.queryByTarget(targetId, targetType)
            .asFlow()
            .map { it.executeAsOneOrNull() != null }
    }

    override fun observePinsExist(): Flow<Boolean> {
        return db.listPinQueries.queryCount()
            .asFlow()
            .map { it.executeAsOne() > 0 }
    }
}