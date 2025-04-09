package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.data.app.SourceParams
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.characters.Character
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.data.db.api.daos.CharacterDao
import com.gnoemes.shimori.data.db.api.daos.SourceIdsSyncDao
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncer.Companion.RESULT_TAG
import com.gnoemes.shimori.data.db.api.syncer.ItemSyncerResult
import com.gnoemes.shimori.data.syncer.SyncedSourceStore
import com.gnoemes.shimori.data.syncer.syncerForEntity
import com.gnoemes.shimori.data.titles.anime.AnimeInfo
import com.gnoemes.shimori.data.titles.manga.MangaInfo
import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.model.SourceDataType
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@SingleIn(AppScope::class)
class SyncedCharacterStore(
    internal val dao: CharacterDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Character) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.params, data)
            is AnimeInfo -> data.characters?.let { trySync(response.params, it) }
            is MangaInfo -> data.characters?.let { trySync(response.params, it) }
            is Character -> sync(response.params, data)
            is CharacterInfo -> sync(response.params, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(params: SourceParams, data: List<E>) {
        when {
            data.filterIsInstance<Character>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<Character>()
            )

            data.filterIsInstance<CharacterInfo>().isNotEmpty() -> sync(
                params,
                data.filterIsInstance<CharacterInfo>().map { it.entity }
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(params: SourceParams, remote: Character) {
        val result = createSyncer(params).sync(
            syncDao.findLocalId(params.sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(params: SourceParams, remote: List<Character>) {
        val result = createSyncer(params).sync(
            currentValues = dao.queryAll(),
            networkValues = remote,
            removeNotMatched = false
        )

        log(result)
    }

    private fun createSyncer(
        params: SourceParams
    ) = syncerForEntity(
        syncDao,
        type,
        params,
        dao,
        entityToKey = { _, title -> syncDao.findRemoteId(params.sourceId, title.id, type) },
        networkEntityToKey = { _, title -> title.id },
        networkToId = { remote -> remote.id },
        mapper = { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Character>) {
        logger.i(tag = RESULT_TAG) {
            "Character sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}