package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.characters.Character
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
class CharacterRepository(
    logger: Logger,
    catalogue: CatalogueManager,
    entityLastRequest: EntityLastRequestStore,
    private val store: SyncedCharacterStore,
    private val roleStore: CharacterRoleStore,
    private val ids: SourceIdsSyncDao,
    private val transactionRunner: DatabaseTransactionRunner
) : BaseCatalogueRepository<Character>(
    SourceDataType.Character,
    logger,
    catalogue,
    entityLastRequest,
    transactionRunner
) {
    override fun queryById(id: Long) = store.dao.queryById(id)
    fun observeById(id: Long) = store.dao.observeById(id)
    fun observeTitleCharactersCount(titleId: Long, type: TrackTargetType) =
        store.dao.observeTitleCharactersCount(titleId, type)

    fun observeTitleCharacters(titleId: Long, type: TrackTargetType, search: String?) =
        store.dao.observeTitleCharacters(titleId, type, search)

    suspend fun sync(id: Long) =
        request(id) {
            character { get(it) }
        }.also {
            transactionRunner {
                store.trySync(it)
                roleStore.trySync(it)
                characterUpdated(id)
            }
        }

    override fun <T> trySyncTransaction(data: SourceResponse<T>) {
        store.trySync(data)
        roleStore.trySync(data)

        when (val info = data.data) {
            is AnimeInfo -> if (info.characters != null && info.charactersRoles != null) {
                val localId = ids.findLocalId(
                    data.sourceId,
                    info.entity.id,
                    info.entity.type.sourceDataType
                ) ?: return
                titleCharactersUpdated(localId, info.entity.type)
            }

            is MangaInfo -> if (info.characters != null && info.charactersRoles != null) {
                val localId = ids.findLocalId(
                    data.sourceId,
                    info.entity.id,
                    info.entity.type.sourceDataType
                ) ?: return
                titleCharactersUpdated(localId, info.entity.type)
            }
        }

    }

    fun shouldUpdateCharacter(
        id: Long,
    ) = shouldUpdate(
        Request.CHARACTER_DETAILS,
        id,
    )

    fun shouldUpdateTitleCharacters(
        id: Long,
        type: TrackTargetType,
    ) = shouldUpdate(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_CHARACTERS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_CHARACTERS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_CHARACTERS
        },
        id,
    )

    private fun titleCharactersUpdated(
        id: Long,
        type: TrackTargetType,
    ) = updated(
        when (type) {
            TrackTargetType.ANIME -> Request.ANIME_DETAILS_CHARACTERS
            TrackTargetType.MANGA -> Request.MANGA_DETAILS_CHARACTERS
            TrackTargetType.RANOBE -> Request.RANOBE_DETAILS_CHARACTERS
        },
        id
    )

    private fun characterUpdated(id: Long) = updated(Request.CHARACTER_DETAILS, id)
}