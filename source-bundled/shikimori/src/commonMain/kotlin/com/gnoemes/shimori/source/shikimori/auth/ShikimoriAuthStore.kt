package com.gnoemes.shimori.source.shikimori.auth

import com.gnoemes.shimori.source.auth.AuthExpiration
import com.gnoemes.shimori.source.auth.SourceAuthStore
import com.gnoemes.shimori.source.model.SourceAuthState
import com.gnoemes.shimori.source.shikimori.ShikimoriId
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriAuthStore(
    private val id: Lazy<ShikimoriId>,
    private val store: Lazy<SourceAuthStore>,
    private val bus: Lazy<AuthExpiration>,
) {
    fun get(): SourceAuthState? = store.value.get(id.value)
    fun save(state: SourceAuthState) = store.value.save(state)
    suspend fun clear() = store.value.clear(id.value).also {
        bus.value.onAuthExpired(id.value)
    }
}