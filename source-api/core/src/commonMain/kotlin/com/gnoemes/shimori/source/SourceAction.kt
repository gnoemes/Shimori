package com.gnoemes.shimori.source


interface SourceAction<Arguments, Result> {
    suspend operator fun invoke(args: Arguments): Result
}