package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.core.BaseCatalogueRepository
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject

@Inject
class PersonRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedPersonStore,
    private val roleStore: PersonRoleStore,
    private val ids: SourceIdsSyncDao,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Person>(
    SourceDataType.Person,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeTitlePersons(titleId: Long, type: TrackTargetType) =
        store.dao.observeTitlePersons(titleId, type)

    suspend fun sync(id: Long) =
        request(id) {
            person { get(it) }
        }.also {
            transactionRunner {
                store.trySync(it)
                roleStore.trySync(it)
                personUpdated(id)
            }
        }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        store.trySync(data)
        roleStore.trySync(data)

        when (val info = data.data) {
            is AnimeInfo -> if (info.persons != null && info.personsRoles != null) {
                val localId = ids.findLocalId(
                    data.sourceId,
                    info.entity.id,
                    info.entity.type.sourceDataType
                ) ?: return
                titlePersonsUpdated(localId, info.entity.type)
            }

            is MangaInfo -> if (info.persons != null && info.personsRoles != null) {
                val localId = ids.findLocalId(
                    data.sourceId,
                    info.entity.id,
                    info.entity.type.sourceDataType
                ) ?: return
                titlePersonsUpdated(localId, info.entity.type)
            }
        }
    }

    fun shouldUpdatePerson(
        id: Long,
    ) = shouldUpdate(
        Request.PERSON_DETAILS,
        id,
    )

    fun shouldUpdateTitlePersons(
        id: Long,
        type: TrackTargetType,
    ) = shouldUpdate(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_PERSONS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_PERSONS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_PERSONS
        },
        id,
    )

    private fun titlePersonsUpdated(
        id: Long,
        type: TrackTargetType,
    ) = updated(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_PERSONS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_PERSONS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_PERSONS
        },
        id
    )

    private fun personUpdated(id: Long) = updated(Request.PERSON_DETAILS, id)
}