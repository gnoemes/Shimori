package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.anime.AnimeRepository
import com.gnoemes.shimori.data.manga.MangaRepository
import com.gnoemes.shimori.data.ranobe.RanobeRepository
import com.gnoemes.shimori.data.track.TrackStatus
import com.gnoemes.shimori.data.track.TrackTargetType
import com.gnoemes.shimori.data.tracks.TrackRepository
import com.gnoemes.shimori.data.user.UserRepository
import com.gnoemes.shimori.data.user.UserShort
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.sources.SourceIds
import kotlinx.coroutines.withContext
import me.tatarka.inject.annotations.Inject

@Inject
class UpdateTitleTracks(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
    private val trackRepository: TrackRepository,
    private val userRepository: UserRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateTitleTracks.Params, Unit>() {

    override suspend fun doWork(params: Params) {
        withContext(dispatchers.io) {
            //TODO support sources
            val user = userRepository.queryMeShort(SourceIds.SHIKIMORI)
            if (user == null || user.isLocal) return@withContext

            if (params.forceUpdate) update(user, params.type, params.status)
            else if (params.optionalUpdate && needUpdate(params.type, params.status)) update(
                user,
                params.type,
                params.status
            )

        }
    }

    private suspend fun update(user: UserShort, type: TrackTargetType, status: TrackStatus?) =
        when (type) {
            TrackTargetType.ANIME -> animeRepository.syncTracked(user, status).also {
                trackRepository.trySync(it)
            }

            TrackTargetType.MANGA -> mangaRepository.syncTracked(user, status).also {
                trackRepository.trySync(it)
            }

            TrackTargetType.RANOBE -> ranobeRepository.syncTracked(user, status).also {
                trackRepository.trySync(it)
            }
        }

    private suspend fun needUpdate(type: TrackTargetType, status: TrackStatus?) = when (type) {
        TrackTargetType.ANIME -> animeRepository.needUpdateTitlesWithStatus(status)
        TrackTargetType.MANGA -> mangaRepository.needUpdateTitlesWithStatus(status)
        TrackTargetType.RANOBE -> ranobeRepository.needUpdateTitlesWithStatus(status)
    }

    data class Params(
        val type: TrackTargetType,
        val forceUpdate: Boolean,
        val optionalUpdate: Boolean,
        val status: TrackStatus?
    ) {
        companion object {
            fun fullUpdate(type: TrackTargetType) = Params(
                type = type,
                forceUpdate = true,
                optionalUpdate = false,
                status = null
            )

            fun optionalUpdate(type: TrackTargetType) = Params(
                type = type,
                forceUpdate = false,
                optionalUpdate = true,
                status = null
            )
        }
    }
}