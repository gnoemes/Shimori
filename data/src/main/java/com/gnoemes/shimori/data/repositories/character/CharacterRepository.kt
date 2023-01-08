package com.gnoemes.shimori.data.repositories.character

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.AnimeDao
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.database.daos.MangaDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.core.entities.app.SyncTarget
import com.gnoemes.shimori.data.core.entities.characters.CharacterRole
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.core.sources.CharacterDataSource
import com.gnoemes.shimori.data.core.utils.Shikimori
import kotlinx.datetime.Instant

class CharacterRepository(
    private val dao: CharacterDao,
    private val animeDao: AnimeDao,
    private val mangaDao: MangaDao,
    @Shikimori private val source: CharacterDataSource,
    private val characterLastRequest: CharacterDetailsLastRequestStore,
) {

    fun observeByTitle(targetId: Long, targetType: TrackTargetType) =
        dao.observeByTitle(targetId, targetType)

    suspend fun update(id: Long) {
        val local = dao.queryById(id)

        if (local != null) {
            val result = source.get(local)

            //update character locally
            dao.insertOrUpdate(
                result.character.copy(
                    id = local.id
                )
            )

            //update animes and get local ids
            animeDao.insertOrUpdate(result.animes)
            val animeIds = animeDao.queryIdsBySyncTargets(
                result.animes.map { SyncTarget(SyncApi.Shikimori, it.shikimoriId) }
            ).map { it.second }

            //update mangas and get local ids
            mangaDao.insertOrUpdate(result.mangas)
            val mangaIds = mangaDao.queryIdsBySyncTargets(
                result.mangas.map { SyncTarget(SyncApi.Shikimori, it.shikimoriId) }
            ).map { it.second }


            //create roles for animes & mangas based on local ids
            val roles = animeIds.map {
                CharacterRole(
                    characterId = local.id,
                    targetId = it,
                    targetType = TrackTargetType.ANIME,
                )
            } + mangaIds.map {
                CharacterRole(
                    characterId = local.id,
                    targetId = it,
                    targetType = TrackTargetType.MANGA,
                )
            }

            //store roles in db
            dao.syncRoles(roles)

            characterLastRequest.updateLastRequest(id = id)
        }
    }

    suspend fun needUpdateCharacter(
        id: Long,
        //update details once per day
        expiry: Instant = instantInPast(minutes = ExpiryConstants.CharacterDetails)
    ) = characterLastRequest.isRequestBefore(expiry, id = id)
}