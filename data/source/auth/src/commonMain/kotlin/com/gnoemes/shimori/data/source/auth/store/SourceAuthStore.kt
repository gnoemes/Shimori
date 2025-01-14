package com.gnoemes.shimori.data.source.auth.store

import com.gnoemes.shimori.source.SourceAuthState

interface SourceAuthStore {
    fun get(sourceId : Long) : SourceAuthState?
    fun save(state : SourceAuthState)
    fun clear(sourceId : Long)
    fun clear()
}
