package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.eventbus.StateBus
import com.gnoemes.shimori.data.manga.GenreRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTitle(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val genreRepository: GenreRepository,
    private val bus: StateBus,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitle.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id, params.type)
            else if (needUpdate(params.id, params.type)) update(params.id, params.type)
        }
    }

    private suspend fun update(id: Long, type: TrackTargetType) {
        try {
            bus.titleUpdating(true)
            when (type) {
                TrackTargetType.ANIME -> animeRepository.sync(id)
                    .also {
                        genreRepository.trySync(it)
                    }

                TrackTargetType.MANGA -> mangaRepository.sync(id)
                    .also {
                        genreRepository.trySync(it)
                    }

                TrackTargetType.RANOBE -> ranobeRepository.sync(id)
                    .also {
                        genreRepository.trySync(it)
                    }
            }
        } finally {
            bus.titleUpdating(false)
        }
    }

    private fun needUpdate(id: Long, type: TrackTargetType) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitle(id)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitle(id)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitle(id)
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