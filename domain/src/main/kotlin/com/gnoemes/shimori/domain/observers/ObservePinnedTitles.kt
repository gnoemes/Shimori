package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.anime.AnimeWithRate
import com.gnoemes.shimori.model.manga.MangaWithRate
import com.gnoemes.shimori.model.ranobe.RanobeWithRate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ObservePinnedTitles @Inject constructor(
    private val animeRepository: AnimeRepository,
    private val mangaRepository: MangaRepository,
    private val ranobeRepository: RanobeRepository,
) : SubjectInteractor<Unit, List<EntityWithRate<out ShimoriEntity>>>() {

    override fun createObservable(params: Unit): Flow<List<EntityWithRate<out ShimoriEntity>>> {
        return combine(
                animeRepository.observePinned(),
                mangaRepository.observePinned(),
                ranobeRepository.observePinned()
        ) { animes: List<AnimeWithRate>, mangas: List<MangaWithRate>, ranobe: List<RanobeWithRate> ->
            //TODO merge sort?
            mapOf(
                    0 to animes,
                    1 to mangas,
                    2 to ranobe
            ).map { it.value }
                .flatten()
                .sortedByDescending { it.rate?.dateUpdated }
        }
    }
}