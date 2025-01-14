package com.gnoemes.shimori.data.daos


import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.ShimoriDB
import com.gnoemes.shimori.data.db.api.daos.TrackDao
import com.gnoemes.shimori.data.track.Track
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.track
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn


@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = TrackDao::class)
class TrackDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers,
) : TrackDao(), SqlDelightEntityDao<Track> {
    override fun insert(entity: Track): Long {
        entity.let {
            db.trackQueries.insert(
                it.targetId,
                it.targetType,
                it.status,
                it.score,
                it.comment,
                it.progress,
                it.reCounter,
                it.dateCreated,
                it.dateUpdated
            )
        }

        return db.trackQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Track) {
        entity.let {
            db.trackQueries.update(
                comgnoemesshimoridatadb.data.Track(
                    it.id,
                    it.targetId,
                    it.targetType,
                    it.status,
                    it.score,
                    it.comment,
                    it.progress,
                    it.reCounter,
                    it.dateCreated,
                    it.dateUpdated
                )
            )
        }
    }

    override fun delete(entity: Track) {
        db.trackQueries.deleteById(entity.id)
    }

    override fun queryAll(): List<Track> {
        return db.trackQueries.queryAll(::track).executeAsList()
    }

    override fun queryById(id: Long): Track? {
        return db.trackQueries
            .queryById(id, ::track)
            .executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Track?> {
        return db.trackQueries
            .queryById(id, ::track)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeByTarget(targetId: Long, targetType: TrackTargetType): Flow<Track?> {
        return db.trackQueries
            .queryByTarget(targetId, targetType, ::track)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeHasTracks(): Flow<Boolean> {
        return db.trackQueries
            .queryCount()
            .asFlow()
            .mapToOne(dispatchers.io)
            .map { it > 0 }
            .flowOn(dispatchers.io)
    }

    override fun observeExistedStatuses(type: TrackTargetType): Flow<List<TrackStatus>> {
        return combine(
            *TrackStatus.listPagesOrder.map { status ->
                db.trackQueries.statusForTypeExist(type, status)
                    .asFlow()
                    .mapToOne(dispatchers.io)
                    .map { count -> status to (count > 0) }
            }
                .toTypedArray()
        ) { statuses ->
            statuses
                .filter { it.second }
                .map { it.first }
        }
            .flowOn(dispatchers.io)
    }
}