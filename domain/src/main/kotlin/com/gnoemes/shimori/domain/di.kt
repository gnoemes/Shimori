package com.gnoemes.shimori.domain

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.domain.interactors.UpdateRateSort
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.interactors.UpdateTitleRates
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.*
import org.kodein.di.DI
import org.kodein.di.bindProvider

val domainModule = DI.Module("domain") {
    bindProvider { new(::ObserveMyUserShort) }
    bindProvider { new(::ObserveShikimoriAuth) }
    bindProvider { new(::ObservePinsExist) }
    bindProvider { new(::ObserveRatesExist) }
    bindProvider { new(::ObserveRateSort) }
    bindProvider { new(::ObserveListPage) }

    bindProvider { new(::UpdateUser) }
    bindProvider { new(::UpdateRates) }
    bindProvider { new(::UpdateTitleRates) }
    bindProvider { new(::UpdateRateSort) }
}