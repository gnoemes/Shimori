package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.core.entities.track.TrackTargetType
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.character.CharacterRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.source.SourceRepository
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext

class UpdateTitle(
    private val sourceRepository: SourceRepository,
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val characterRepository: CharacterRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitle.Params>() {

    override suspend fun doWork(params: Params) {
        withContext<Unit>(dispatchers.io) {
            if (params.force) {
                update(params.id, params.type)
                updateRoles(params.id, params.type)
            } else {
                if (needUpdate(params.id, params.type)) update(params.id, params.type)
                if (needUpdateRoles(params.id, params.type)) updateRoles(params.id, params.type)
            }
        }
        //TODO other related content
    }

    private suspend fun update(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.queryById(id)?.let {
            val (sourceId, title) = sourceRepository.get(it)
            animeRepository.sync(sourceId, title.entity)
            animeRepository.syncVideos(
                it.id,
                title.videos ?: emptyList()
            )
            animeRepository.titleUpdated(id)
        }
        TrackTargetType.MANGA -> mangaRepository.queryById(id)?.let {
            val (sourceId, title) = sourceRepository.get(it)
            mangaRepository.sync(sourceId, title)
            mangaRepository.titleUpdated(id)
        }
        TrackTargetType.RANOBE -> ranobeRepository.queryById(id)?.let {
            val (sourceId, title) = sourceRepository.get(it)
            ranobeRepository.sync(sourceId, title)
            ranobeRepository.titleUpdated(id)
        }
    }

    private suspend fun needUpdate(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitle(id)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitle(id)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitle(id)
    }

    private suspend fun updateRoles(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.queryById(id)?.let {
            val (sourceId, roles) = sourceRepository.getRoles(it)
            characterRepository.sync(sourceId, it.id, it.type, roles.characters)
            animeRepository.rolesUpdated(id)
        }
        TrackTargetType.MANGA -> mangaRepository.queryById(id)?.let {
            val (sourceId, roles) = sourceRepository.getRoles(it)
            characterRepository.sync(sourceId, it.id, it.type, roles.characters)
            mangaRepository.rolesUpdated(id)
        }
        TrackTargetType.RANOBE -> ranobeRepository.queryById(id)?.let {
            val (sourceId, roles) = sourceRepository.getRoles(it)
            characterRepository.sync(sourceId, it.id, it.type, roles.characters)
            ranobeRepository.rolesUpdated(id)
        }
    }

    private suspend fun needUpdateRoles(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitleRoles(id)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitleRoles(id)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitleRoles(id)
    }

    data class Params(
        val id: Long,
        val type: TrackTargetType,
        val force: Boolean,
    ) {
        companion object {
            fun forceUpdate(id: Long, type: TrackTargetType) = Params(
                id = id,
                type = type,
                force = true,
            )

            fun optionalUpdate(id: Long, type: TrackTargetType) = Params(
                id = id,
                type = type,
                force = false,
            )
        }
    }
}