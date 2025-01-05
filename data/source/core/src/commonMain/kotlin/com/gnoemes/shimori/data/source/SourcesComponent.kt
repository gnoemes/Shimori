package com.gnoemes.shimori.data.source

import com.gnoemes.shimori.data.source.auth.SourceAuthComponent
import com.gnoemes.shimori.data.source.catalogue.SourceCatalogueComponent
import com.gnoemes.shimori.data.source.shikimori.ShikimoriSourceComponent
import com.gnoemes.shimori.data.source.track.SourceTrackComponent
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesTo


@ContributesTo(AppScope::class)
interface SourcesComponent :
    ShikimoriSourceComponent,
    SourceCatalogueComponent,
    SourceTrackComponent,
    SourceAuthComponent {


}