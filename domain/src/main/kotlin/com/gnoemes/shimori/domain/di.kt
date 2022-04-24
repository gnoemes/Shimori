package com.gnoemes.shimori.domain

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.domain.interactors.UpdateRates
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import org.kodein.di.DI
import org.kodein.di.bindProvider

val domainModule = DI.Module("domain") {
    bindProvider { new(::ObserveMyUserShort) }
    bindProvider { new(::ObserveShikimoriAuth) }
    bindProvider { new(::UpdateUser) }
    bindProvider { new(::UpdateRates) }
}