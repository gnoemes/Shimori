package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.characters.CharacterInfo
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
class CharacterRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedCharacterStore,
    private val roleStore: CharacterRoleStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val ids: SourceIdsSyncDao,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeTitleCharactersCount(titleId: Long, type: TrackTargetType) =  store.dao.observeTitleCharactersCount(titleId, type)
    fun observeTitleCharacters(titleId: Long, type: TrackTargetType, search: String?) =
        store.dao.observeTitleCharacters(titleId, type, search)

    suspend fun sync(id: Long): SourceResponse<CharacterInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Character with id: $id not found")

        return catalogue.character { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    roleStore.trySync(it)
                    characterUpdated(id)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            roleStore.trySync(data)

            when (val info = data.data) {
                is AnimeInfo -> if (info.characters != null && info.charactersRoles != null) {
                    val localId = ids.findLocalId(
                        data.sourceId,
                        info.entity.id,
                        info.entity.type.sourceDataType
                    ) ?: return@transactionRunner
                    titleCharactersUpdated(localId, info.entity.type)
                }

                is MangaInfo -> if (info.characters != null && info.charactersRoles != null) {
                    val localId = ids.findLocalId(
                        data.sourceId,
                        info.entity.id,
                        info.entity.type.sourceDataType
                    ) ?: return@transactionRunner
                    titleCharactersUpdated(localId, info.entity.type)
                }
            }
        }
    }

    fun needUpdateCharacter(
        id: Long,
        expiry: Instant = ExpiryConstants.CHARACTER_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.CHARACTER_DETAILS,
        id,
        expiry
    )

    fun needUpdateTitleCharacters(
        id: Long,
        type: TrackTargetType,
        expiry: Instant = ExpiryConstants.TITLE_CHARACTERS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_CHARACTERS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_CHARACTERS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_CHARACTERS
        },
        id,
        expiry
    )

    fun titleCharactersUpdated(
        id: Long,
        type: TrackTargetType,
    ) =
        entityLastRequest.updateLastRequest(
            when (type) {
                TrackTargetType.ANIME -> Request.ANIME_DETAILS_CHARACTERS
                TrackTargetType.MANGA -> Request.MANGA_DETAILS_CHARACTERS
                TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_CHARACTERS
            },
            id
        )


    fun characterUpdated(id: Long) =
        entityLastRequest.updateLastRequest(Request.CHARACTER_DETAILS, id)
}