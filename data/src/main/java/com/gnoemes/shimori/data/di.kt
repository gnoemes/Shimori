package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.anime.AnimeWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.manga.MangaWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider

val dataModule = DI.Module("data") {
    bindProvider { new(::ShikimoriUserRepository) }
    bindProvider { new(::RateRepository) }

    bindProvider { new(::ListsStateManager) }

    bindProvider { new(::AnimeRepository) }
    bindProvider { new(::MangaRepository) }
    bindProvider { new(::RanobeRepository) }
    bindProvider { new(::AnimeWithStatusLastRequestStore) }
    bindProvider { new(::MangaWithStatusLastRequestStore) }
    bindProvider { new(::RanobeWithStatusLastRequestStore) }
}