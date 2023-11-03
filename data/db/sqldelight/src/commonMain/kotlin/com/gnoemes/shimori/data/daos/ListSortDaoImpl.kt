package com.gnoemes.shimori.data.daos

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.ListSortDao
import com.gnoemes.shimori.data.track.ListSort
import com.gnoemes.shimori.data.track.ListType
import com.gnoemes.shimori.data.util.ListSortDAO
import com.gnoemes.shimori.data.util.ListSortMapper
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject

@Inject
class ListSortDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : ListSortDao, SqlDelightEntityDao<ListSort> {

    override fun insert(entity: ListSort): Long {
        entity.let {
            db.listSortQueries.insert(
                it.type.type,
                it.sortOption,
                it.isDescending
            )
        }

        return db.listSortQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: ListSort) {
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

    override fun delete(entity: ListSort) {
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