package com.gnoemes.shimori.data.daos

import androidx.paging.PagingSource
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToOne
import app.cash.sqldelight.coroutines.mapToOneOrNull
import app.cash.sqldelight.paging3.QueryPagingSource
import com.gnoemes.shimori.ShimoriDB
import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.db.api.daos.PersonDao
import com.gnoemes.shimori.data.person.Person
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.util.person
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class, boundType = PersonDao::class)
class PersonDaoImpl(
    override val db: ShimoriDB,
    private val logger: Logger,
    private val dispatchers: AppCoroutineDispatchers
) : PersonDao(), SqlDelightEntityDao<Person> {
    override fun insert(entity: Person): Long {
        entity.let {
            db.personQueries.insert(
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.isMangaka,
                it.isProducer,
                it.isSeyu,
                it.birthDate,
                it.deceasedDate
            )
        }

        return db.personQueries.selectLastInsertedRowId().executeAsOne()
    }

    override fun update(entity: Person) {
        entity.let {
            db.personQueries.update(
                it.id,
                it.name,
                it.nameRu,
                it.nameEn,
                it.image?.original,
                it.image?.preview,
                it.image?.x96,
                it.image?.x48,
                it.url,
                it.isMangaka,
                it.isProducer,
                it.isSeyu,
                it.birthDate,
                it.deceasedDate
            )
        }
    }

    override fun delete(entity: Person) {
        return db.personQueries.deleteById(entity.id)
    }

    override fun queryAll(): List<Person> {
        return db.personQueries.queryAll(::person).executeAsList()
    }

    override fun queryById(id: Long): Person? {
        return db.personQueries.queryById(id, ::person)
            .executeAsOneOrNull()
    }

    override fun observeById(id: Long): Flow<Person?> {
        return db.personQueries.queryById(id, ::person)
            .asFlow()
            .mapToOneOrNull(dispatchers.io)
            .flowOn(dispatchers.io)
    }

    override fun observeTitlePersonsCount(
        targetId: Long,
        targetType: TrackTargetType
    ): Flow<Int> {
        return db.personQueries.countPersonsByTitle(targetId, targetType)
            .asFlow()
            .mapToOne(dispatchers.io)
            .map { it.toInt() }
            .flowOn(dispatchers.io)
    }

    override fun observeTitlePersons(
        targetId: Long,
        targetType: TrackTargetType,
    ): PagingSource<Int, Person> {
        return QueryPagingSource(
            countQuery = db.personQueries.countPersonsByTitle(targetId, targetType),
            transacter = db.personQueries,
            context = dispatchers.io,
            queryProvider = { limit, offset ->
                db.personQueries
                    .queryByTitle(
                        targetId,
                        targetType,
                        limit,
                        offset,
                        ::person
                    )
            }
        )
    }
}