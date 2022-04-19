package com.gnoemes.shikimori

import com.gnoemes.shikimori.mappers.GenreMapper
import com.gnoemes.shikimori.mappers.ImageResponseMapper
import com.gnoemes.shikimori.mappers.TitleStatusMapper
import com.gnoemes.shikimori.mappers.anime.AnimeResponseMapper
import com.gnoemes.shikimori.mappers.anime.AnimeTypeMapper
import com.gnoemes.shikimori.mappers.anime.RateResponseToAnimeMapper
import com.gnoemes.shikimori.mappers.manga.MangaResponseMapper
import com.gnoemes.shikimori.mappers.manga.RateResponseToMangaMapper
import com.gnoemes.shikimori.mappers.ranobe.RanobeResponseMapper
import com.gnoemes.shikimori.mappers.ranobe.RateResponseToRanobeMapper
import com.gnoemes.shikimori.mappers.rate.RateMapper
import com.gnoemes.shikimori.mappers.rate.RateStatusMapper
import com.gnoemes.shikimori.mappers.rate.RateTargetTypeMapper
import com.gnoemes.shikimori.mappers.user.UserBriefMapper
import com.gnoemes.shikimori.mappers.user.UserResponseMapper
import com.gnoemes.shikimori.sources.*
import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.base.sources.*
import org.kodein.di.*

val shikimoriModule = DI.Module("shikimori") {
    importOnce(mappers)

    bindEagerSingleton {
        Shikimori(
            instance(KodeinTag.shikimori),
            instance(),
            instance(),
            instance()
        )
    }



    bindSingleton<RateDataSource> { new(::ShikimoriRateDataSource) }
    bindSingleton<UserDataSource> { new(::ShikimoriUserDataSource) }
    bindSingleton<AnimeDataSource> { new(::ShikimoriAnimeDataSource) }
    bindSingleton<MangaDataSource> { new(::ShikimoriMangaDataSource) }
    bindSingleton<RanobeDataSource> { new(::ShikimoriRanobeDataSource) }
}

private val mappers = DI.Module("shikimori-mappers") {
    bindProvider { RateTargetTypeMapper() }
    bindProvider { RateStatusMapper() }
    bindProvider { UserBriefMapper() }
    bindProvider { ImageResponseMapper() }
    bindProvider { UserResponseMapper() }
    bindProvider { AnimeTypeMapper() }
    bindProvider { TitleStatusMapper() }
    bindProvider { GenreMapper() }

    bindProvider { new(::RateMapper) }
    bindProvider { new(::AnimeResponseMapper) }
    bindProvider { new(::RateResponseToAnimeMapper) }
    bindProvider { new(::MangaResponseMapper) }
    bindProvider { new(::RateResponseToMangaMapper) }
    bindProvider { new(::RanobeResponseMapper) }
    bindProvider { new(::RateResponseToRanobeMapper) }
}