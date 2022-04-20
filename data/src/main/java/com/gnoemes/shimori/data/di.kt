package com.gnoemes.shimori.data

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.data.repositories.user.ShikimoriUserRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider

val dataModule = DI.Module("data") {
    bindProvider { new(::ShikimoriUserRepository) }
    bindProvider { new(::RateRepository) }
}