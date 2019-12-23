package com.gnoemes.shimori.data.database

import android.database.sqlite.SQLiteException
import android.util.Log
import com.gnoemes.shimori.data.daos.EntityDao
import com.gnoemes.shimori.data.daos.EntityInserter
import com.gnoemes.shimori.data.util.DatabaseTransactionRunner
import com.gnoemes.shimori.model.ShimoriEntity
import javax.inject.Inject


class ShimoriEntityInserter @Inject constructor(
    private val transactionRunner: DatabaseTransactionRunner
) : EntityInserter {

    override suspend fun <E : ShimoriEntity> insertOrUpdate(dao: EntityDao<E>, entities: List<E>) =
        transactionRunner {
            entities.forEach { insertOrUpdate(dao, it) }
        }

    override suspend fun <E : ShimoriEntity> insertOrUpdate(dao: EntityDao<E>, entity: E): Long {
        Log.i("INSERTER", "insertOrUpdate $entity")

        return if (entity.id == 0L) {
            try {
                dao.insert(entity)
            } catch (e: SQLiteException) {
                throw SQLiteException("Error while inserting entity $entity").apply {
                    e.printStackTrace()
                    initCause(e)
                }
            }
        } else {
            try {
                dao.update(entity)
                entity.id
            } catch (e: SQLiteException) {
                throw SQLiteException("Error while updating entity: $entity").apply {
                    e.printStackTrace()
                    initCause(e)
                }
            }
        }
    }
}