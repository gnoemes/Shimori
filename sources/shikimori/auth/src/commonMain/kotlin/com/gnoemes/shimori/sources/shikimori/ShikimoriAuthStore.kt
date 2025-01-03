package com.gnoemes.shimori.sources.shikimori

import com.gnoemes.shimori.data.source.auth.AuthExpirationEvents
import com.gnoemes.shimori.data.source.auth.store.SourceAuthStore
import com.gnoemes.shimori.source.SourceAuthState
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriAuthStore(
    private val id: Lazy<ShikimoriId>,
    private val store: Lazy<SourceAuthStore>,
    private val bus: Lazy<AuthExpirationEvents>,
) {
    fun get(): SourceAuthState? = store.value.get(id.value)
    fun save(state: SourceAuthState) = store.value.save(state)
    suspend fun clear() = store.value.clear(id.value).also {
        bus.value.onAuthExpired(id.value)
    }
}