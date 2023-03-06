package com.gnoemes.shimori.domain

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.domain.interactors.*
import com.gnoemes.shimori.domain.observers.*
import org.kodein.di.DI
import org.kodein.di.bindProvider

val domainModule = DI.Module("domain") {
    bindProvider { new(::ObserveMyUserShort) }
    bindProvider { new(::ObserveShikimoriAuth) }
    bindProvider { new(::ObservePinsExist) }
    bindProvider { new(::ObserveTracksExist) }
    bindProvider { new(::ObserveListSort) }
    bindProvider { new(::ObserveListPage) }
    bindProvider { new(::ObserveExistedStatuses) }
    bindProvider { new(::ObservePinExist) }
    bindProvider { new(::ObserveTitleWithTrackEntity) }
    bindProvider { new(::ObserveCharacters) }
    bindProvider { new(::ObserveAnimeVideos) }
    bindProvider { new(::ObserveAnimeScreenshots) }

    bindProvider { new(::UpdateUser) }
    bindProvider { new(::UpdateTracks) }
    bindProvider { new(::UpdateTitleTracks) }
    bindProvider { new(::UpdateListSort) }
    bindProvider { new(::ToggleTitlePin) }
    bindProvider { new(::DeleteTrack) }
    bindProvider { new(::CreateOrUpdateTrack) }
    bindProvider { new(::UpdateUserAndTracks) }
    bindProvider { new(::SyncPendingTracks) }
    bindProvider { new(::UpdateTitle) }
    bindProvider { new(::UpdateCharacter) }
}