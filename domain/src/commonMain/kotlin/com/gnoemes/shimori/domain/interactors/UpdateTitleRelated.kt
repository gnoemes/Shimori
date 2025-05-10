package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.app.newData
import com.gnoemes.shimori.data.core.RelatedStore
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.domain.Interactor
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTitleRelated(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val relatedStore: RelatedStore,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleRelated.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            if (params.force) update(params.id, params.type)
            else if (shouldUpdate(params.id, params.type)) update(params.id, params.type)
        }
    }

    private suspend fun update(id: Long, type: TrackTargetType) {
        when (type) {
            TrackTargetType.ANIME -> animeRepository.syncTitleRelated(id).also { info ->
                info.newData { it.related }.let {
                    animeRepository.trySync(it)
                    animeRepository.titleRelatedUpdated(id)
                    mangaRepository.trySync(it)
                    ranobeRepository.trySync(it)
                    relatedStore.trySync(it)
                }
            }

            TrackTargetType.MANGA -> mangaRepository.syncTitleRelated(id).also { info ->
                info.newData { it.related }.let {
                    animeRepository.trySync(it)
                    mangaRepository.trySync(it)
                    mangaRepository.titleRelatedUpdated(id)
                    ranobeRepository.trySync(it)
                    relatedStore.trySync(it)
                }
            }

            TrackTargetType.RANOBE -> ranobeRepository.syncTitleRelated(id).also { info ->
                info.newData { it.related }.let {
                    animeRepository.trySync(it)
                    mangaRepository.trySync(it)
                    ranobeRepository.trySync(it)
                    ranobeRepository.titleRelatedUpdated(id)
                    relatedStore.trySync(it)
                }
            }
        }
    }

    private fun shouldUpdate(id: Long, type: TrackTargetType) =
        when (type) {
            TrackTargetType.ANIME -> animeRepository.shouldUpdateTitleRelated(id)
            TrackTargetType.MANGA -> mangaRepository.shouldUpdateTitleRelated(id)
            TrackTargetType.RANOBE -> ranobeRepository.shouldUpdateTitleRelated(id)
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