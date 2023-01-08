package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.repositories.anime.AnimeDetailsLastRequestStore
import com.gnoemes.shimori.data.repositories.anime.AnimeRepository
import com.gnoemes.shimori.data.repositories.anime.AnimeRolesLastRequestStore
import com.gnoemes.shimori.data.repositories.anime.AnimeWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.character.CharacterDetailsLastRequestStore
import com.gnoemes.shimori.data.repositories.character.CharacterRepository
import com.gnoemes.shimori.data.repositories.manga.MangaDetailsLastRequestStore
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.data.repositories.manga.MangaRolesLastRequestStore
import com.gnoemes.shimori.data.repositories.manga.MangaWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.pin.ListPinRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeDetailsLastRequestStore
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRepository
import com.gnoemes.shimori.data.repositories.ranobe.RanobeRolesLastRequestStore
import com.gnoemes.shimori.data.repositories.ranobe.RanobeWithStatusLastRequestStore
import com.gnoemes.shimori.data.repositories.track.SyncPendingTracksLastRequestStore
import com.gnoemes.shimori.data.repositories.track.TrackRepository
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton

val dataModule = DI.Module("data") {
    bindProvider { new(::ShikimoriUserRepository) }
    bindProvider { new(::TrackRepository) }

    bindSingleton { new(::ListsStateBus) }

    bindProvider { new(::AnimeRepository) }
    bindProvider { new(::MangaRepository) }
    bindProvider { new(::RanobeRepository) }
    bindProvider { new(::ListPinRepository) }
    bindProvider { new(::CharacterRepository) }

    bindProvider { new(::AnimeWithStatusLastRequestStore) }
    bindProvider { new(::MangaWithStatusLastRequestStore) }
    bindProvider { new(::RanobeWithStatusLastRequestStore) }
    bindProvider { new(::SyncPendingTracksLastRequestStore) }
    bindProvider { new(::AnimeDetailsLastRequestStore) }
    bindProvider { new(::MangaDetailsLastRequestStore) }
    bindProvider { new(::RanobeDetailsLastRequestStore) }
    bindProvider { new(::CharacterDetailsLastRequestStore) }
    bindProvider { new(::AnimeRolesLastRequestStore) }
    bindProvider { new(::MangaRolesLastRequestStore) }
    bindProvider { new(::RanobeRolesLastRequestStore) }
}