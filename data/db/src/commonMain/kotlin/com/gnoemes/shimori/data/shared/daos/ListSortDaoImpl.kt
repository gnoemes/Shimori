package com.gnoemes.shimori.data.shared.daos

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.database.daos.ListSortDao
import com.gnoemes.shimori.data.core.entities.track.ListSort
import com.gnoemes.shimori.data.core.entities.track.ListType
import com.gnoemes.shimori.data.db.ShimoriDB
import com.gnoemes.shimori.data.shared.ListSortDAO
import com.gnoemes.shimori.data.shared.ListSortMapper
import com.squareup.sqldelight.runtime.coroutines.asFlow
import com.squareup.sqldelight.runtime.coroutines.mapToOneOrNull
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

internal class ListSortDaoImpl(
    private val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : ListSortDao() {

    override suspend fun insert(entity: ListSort) {
        entity.let {
            db.listSortQueries.insert(
                it.type.type,
                it.sortOption,
                it.isDescending
            )
        }
    }

    override suspend fun update(entity: ListSort) {
        entity.let {
            db.listSortQueries.update(
                ListSortDAO(
                    it.id,
                    it.type.type,
                    it.sortOption,
                    it.isDescending
                )
            )
        }
    }

    override suspend fun delete(entity: ListSort) {
        db.listSortQueries.deleteById(entity.id)
    }

    override suspend fun query(type: ListType): ListSort? {
        return db.listSortQueries.queryByType(type.type).executeAsOneOrNull()?.let {
            ListSortMapper.map(it)
        }
    }

    override fun observe(type: ListType): Flow<ListSort?> {
        return db.listSortQueries.queryByType(type.type)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .map(ListSortMapper::map)
            .flowOn(dispatchers.io)
    }
}