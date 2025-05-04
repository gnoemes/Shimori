package com.gnoemes.shimori.source.shikimori.sources

import com.apollographql.apollo.api.Optional
import com.gnoemes.shimori.base.utils.forLists
import com.gnoemes.shimori.source.catalogue.RanobeDataSource
import com.gnoemes.shimori.source.model.MalIdArgument
import com.gnoemes.shimori.source.model.SManga
import com.gnoemes.shimori.source.model.STrackStatus
import com.gnoemes.shimori.source.model.SourceIdArgument
import com.gnoemes.shimori.source.shikimori.MangaCharactersQuery
import com.gnoemes.shimori.source.shikimori.MangaDetailsQuery
import com.gnoemes.shimori.source.shikimori.MangaPeopleQuery
import com.gnoemes.shimori.source.shikimori.MangaTracksQuery
import com.gnoemes.shimori.source.shikimori.Shikimori
import com.gnoemes.shimori.source.shikimori.ShikimoriApi
import com.gnoemes.shimori.source.shikimori.mappers.from
import com.gnoemes.shimori.source.shikimori.mappers.manga.MangaDetailsMapper
import com.gnoemes.shimori.source.shikimori.mappers.manga.MangaOrRanobeTracksQueryToMangaWithTrack
import com.gnoemes.shimori.source.shikimori.mappers.ranobe.RanobeCharactersMapper
import com.gnoemes.shimori.source.shikimori.mappers.ranobe.RanobePersonsMapper
import com.gnoemes.shimori.source.shikimori.type.UserRateStatusEnum
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriRanobeDataSource(
    private val api: ShikimoriApi,
    private val mangaTracksQueryToMangaWithTrack: MangaOrRanobeTracksQueryToMangaWithTrack,
    private val detailsMapper: MangaDetailsMapper,
    private val charactersMapper: RanobeCharactersMapper,
    private val personsMapper: RanobePersonsMapper,
) : RanobeDataSource {

    override suspend fun get(id: MalIdArgument): SManga = get(SourceIdArgument(id))
    override suspend fun get(id: SourceIdArgument): SManga {
        return api.apollo.query(
            MangaDetailsQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                detailsMapper.map(it.mangas.first())
            }
    }

    override suspend fun getWithStatus(
        userId: SourceIdArgument,
        status: STrackStatus?
    ): List<SManga> {
        return api.apollo.query(
            MangaTracksQuery(
                limit = Optional.present(Shikimori.MAX_PAGE_SIZE),
                userId = Optional.present(userId.id.toString()),
                status = Optional.presentIfNotNull(UserRateStatusEnum.from(status))
            )
        ).dataAssertNoErrors
            .let {
                mangaTracksQueryToMangaWithTrack
                    .forLists()
                    .invoke(it.userRates)
                    //filter ranobe
                    .filterNotNull()
            }
    }

    override suspend fun getCharacters(id: MalIdArgument) = getCharacters(SourceIdArgument(id))
    override suspend fun getCharacters(id: SourceIdArgument): SManga {
        return api.apollo.query(
            MangaCharactersQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                charactersMapper.map(it.mangas.first())
            }
    }

    override suspend fun getPersons(id: MalIdArgument) = getPersons(SourceIdArgument(id))

    override suspend fun getPersons(id: SourceIdArgument): SManga {
        return api.apollo.query(
            MangaPeopleQuery(
                ids = Optional.present(id.id.toString())
            )
        ).dataAssertNoErrors
            .let {
                personsMapper.map(it.mangas.first())
            }
    }

}