package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.data.app.SourceDataType
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
import com.gnoemes.shimori.logging.api.Logger
import me.tatarka.inject.annotations.Inject

@Inject
class SyncedCharacterStore(
    internal val dao: CharacterDao,
    syncDao: SourceIdsSyncDao,
    logger: Logger
) : SyncedSourceStore(syncDao, logger, SourceDataType.Character) {

    override fun <T> trySync(response: SourceResponse<T>) {
        when (val data = response.data) {
            is List<*> -> trySync(response.sourceId, data)
            is AnimeInfo -> trySync(response.sourceId, data.characters)
            is Character -> sync(response.sourceId, data)
            is CharacterInfo -> sync(response.sourceId, data.entity)
            else -> logger.d(tag = tag) { "Unsupported data type for sync: ${data!!::class}" }
        }
    }

    override fun <E> trySync(sourceId: Long, data: List<E>) {
        when {
            data.filterIsInstance<Character>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<Character>()
            )

            data.filterIsInstance<CharacterInfo>().isNotEmpty() -> sync(
                sourceId,
                data.filterIsInstance<CharacterInfo>().map { it.entity }
            )

            else -> logger.d(tag = tag) {
                "Unsupported data type for sync: ${
                    data.firstOrNull()?.let { it::class }
                }"
            }
        }
    }

    private fun sync(sourceId: Long, remote: Character) {
        val result = createSyncer(sourceId).sync(
            syncDao.findLocalId(sourceId, remote.id, type)?.let { dao.queryById(it) },
            remote
        )

        log(result)
    }

    private fun sync(sourceId: Long, remote: List<Character>) {
        val result = createSyncer(sourceId).sync(
            currentValues = dao.queryAll(),
            networkValues = remote,
            removeNotMatched = false
        )

        log(result)
    }

    private fun createSyncer(
        sourceId: Long
    ) = syncerForEntity(
        syncDao,
        type,
        sourceId,
        dao,
        { _, title -> title.id },
        { _, title -> syncDao.findLocalId(sourceId, title.id, type) },
        { _, remote, local -> remote.copy(id = local?.id ?: 0) },
        logger
    )

    private fun log(result: ItemSyncerResult<Character>) {
        logger.i(tag = RESULT_TAG) {
            "Character sync results --> Added: ${result.added.size} Updated: ${result.updated.size}"
        }
    }
}