package com.gnoemes.shimori.auth

import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.utils.bindViewModel
import org.kodein.di.DI

actual val authModule = DI.Module("auth-module") {
    bindViewModel { new(::AuthViewModel) }
}