package com.gnoemes.shimori.source


interface BasicAuthFlow : SourceAction<Unit, SourceAuthState?> {
    override suspend operator fun invoke(args: Unit): SourceAuthState? = invoke()
    suspend operator fun invoke(): SourceAuthState?
}