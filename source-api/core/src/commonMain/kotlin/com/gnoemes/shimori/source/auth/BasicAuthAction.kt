package com.gnoemes.shimori.source.auth

import com.gnoemes.shimori.source.SourceAction
import com.gnoemes.shimori.source.model.SourceAuthState


interface BasicAuthAction : SourceAction<Unit, SourceAuthState?> {
    override suspend operator fun invoke(args: Unit): SourceAuthState? = invoke()
    suspend operator fun invoke(): SourceAuthState?
}