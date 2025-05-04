package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.person.PersonRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTitlePersons(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val personRepository: PersonRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitlePersons.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id, params.type)
            else if (shouldUpdate(params.id, params.type)) update(params.id, params.type)
        }
    }

    private suspend fun update(id: Long, type: TrackTargetType) {
        when (type) {
            TrackTargetType.ANIME -> animeRepository.syncTitlePersons(id)
                .also { personRepository.trySync(it) }

            TrackTargetType.MANGA -> mangaRepository.syncTitlePersons(id)
                .also { personRepository.trySync(it) }

            TrackTargetType.RANOBE -> ranobeRepository.syncTitlePersons(id)
                .also { personRepository.trySync(it) }
        }
    }

    private fun shouldUpdate(id: Long, type: TrackTargetType) =
        personRepository.shouldUpdateTitlePersons(id, type)

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