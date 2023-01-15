package com.gnoemes.shimori.data.repositories.character

import com.gnoemes.shimori.base.core.extensions.instantInPast
import com.gnoemes.shimori.data.core.database.daos.CharacterDao
import com.gnoemes.shimori.data.core.entities.app.ExpiryConstants
import com.gnoemes.shimori.data.core.entities.characters.Character
import com.gnoemes.shimori.data.core.entities.characters.CharacterInfo
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import kotlinx.datetime.Instant

class CharacterRepository(
    private val dao: CharacterDao,
    private val characterLastRequest: CharacterDetailsLastRequestStore,
) {
    suspend fun queryById(id: Long) = dao.queryById(id)
    fun observeByTitle(targetId: Long, targetType: TrackTargetType) =
        dao.observeByTitle(targetId, targetType)

    suspend fun sync(sourceId: Long, remote: List<Character>) = dao.sync(sourceId, remote)
    suspend fun sync(sourceId: Long, remote: Character) = dao.sync(sourceId, arrayListOf(remote))

    suspend fun sync(sourceId: Long, remote: CharacterInfo) = dao.sync(sourceId, remote)

    suspend fun characterUpdated(id: Long) = characterLastRequest.updateLastRequest(id = id)

    suspend fun sync(
        sourceId: Long,
        targetId: Long,
        targetType: TrackTargetType,
        remote: List<Character>
    ) = dao.sync(sourceId, targetId, targetType, remote)


    suspend fun needUpdateCharacter(
        id: Long,
        //update details once per day
        expiry: Instant = instantInPast(minutes = ExpiryConstants.CharacterDetails)
    ) = characterLastRequest.isRequestBefore(expiry, id = id)
}