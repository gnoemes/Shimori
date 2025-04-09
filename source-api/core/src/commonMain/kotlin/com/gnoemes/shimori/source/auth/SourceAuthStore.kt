package com.gnoemes.shimori.source.auth

import com.gnoemes.shimori.source.model.SourceAuthState

interface SourceAuthStore {
    fun get(sourceId : Long) : SourceAuthState?
    fun save(state : SourceAuthState)
    fun clear(sourceId : Long)
    fun clear()
}
