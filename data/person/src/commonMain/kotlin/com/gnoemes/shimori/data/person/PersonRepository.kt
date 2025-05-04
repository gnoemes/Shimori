package com.gnoemes.shimori.data.person

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class PersonRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedPersonStore,
    private val roleStore: PersonRoleStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val ids: SourceIdsSyncDao,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeTitlePersons(titleId: Long, type: TrackTargetType) =
        store.dao.observeTitlePersons(titleId, type)

    suspend fun sync(id: Long): SourceResponse<PersonInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Person with id: $id not found")

        return catalogue.person { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    roleStore.trySync(it)
                    personUpdated(id)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            roleStore.trySync(data)

            when (val info = data.data) {
                is AnimeInfo -> if (info.persons != null && info.personsRoles != null) {
                    val localId = ids.findLocalId(
                        data.sourceId,
                        info.entity.id,
                        info.entity.type.sourceDataType
                    ) ?: return@transactionRunner
                    titlePersonsUpdated(localId, info.entity.type)
                }

                is MangaInfo -> if (info.persons != null && info.personsRoles != null) {
                    val localId = ids.findLocalId(
                        data.sourceId,
                        info.entity.id,
                        info.entity.type.sourceDataType
                    ) ?: return@transactionRunner
                    titlePersonsUpdated(localId, info.entity.type)
                }
            }
        }
    }

    fun needUpdatePerson(
        id: Long,
        expiry: Instant = ExpiryConstants.PERSON_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.CHARACTER_DETAILS,
        id,
        expiry
    )

    fun needUpdateTitlePersons(
        id: Long,
        type: TrackTargetType,
        expiry: Instant = ExpiryConstants.TITLE_PERSONS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_PERSONS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_PERSONS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_PERSONS
        },
        id,
        expiry
    )

    fun titlePersonsUpdated(
        id: Long,
        type: TrackTargetType,
    ) =
        entityLastRequest.updateLastRequest(
            when (type) {
                TrackTargetType.ANIME -> Request.ANIME_DETAILS_PERSONS
                TrackTargetType.MANGA -> Request.MANGA_DETAILS_PERSONS
                TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_PERSONS
            },
            id
        )


    fun personUpdated(id: Long) =
        entityLastRequest.updateLastRequest(Request.PERSON_DETAILS, id)
}