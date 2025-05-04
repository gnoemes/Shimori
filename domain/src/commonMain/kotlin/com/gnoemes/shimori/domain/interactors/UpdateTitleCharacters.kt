package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.character.CharacterRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTitleCharacters(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val characterRepository: CharacterRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleCharacters.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id, params.type)
            else if (shouldUpdate(params.id, params.type)) update(params.id, params.type)
        }
    }

    private suspend fun update(id: Long, type: TrackTargetType) {
        when (type) {
            TrackTargetType.ANIME -> animeRepository.syncTitleCharacters(id)
                .also { characterRepository.trySync(it) }

            TrackTargetType.MANGA -> mangaRepository.syncTitleCharacters(id)
                .also { characterRepository.trySync(it) }

            TrackTargetType.RANOBE -> ranobeRepository.syncTitleCharacters(id)
                .also { characterRepository.trySync(it) }
        }
    }

    private fun shouldUpdate(id: Long, type: TrackTargetType) =
        characterRepository.shouldUpdateTitleCharacters(id, type)

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