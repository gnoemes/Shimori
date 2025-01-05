package com.gnoemes.shimori.data.source.auth.store

import com.gnoemes.shimori.data.source.auth.SimpleAuthState
import com.gnoemes.shimori.preferences.AppAuthObservablePreferences
import com.gnoemes.shimori.source.SourceAuthState
import kotlinx.serialization.json.Json
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@Inject
@ContributesBinding(AppScope::class)
@SingleIn(AppScope::class)
class DefaultAuthStore(
    private val prefs: AppAuthObservablePreferences
) : SourceAuthStore {

    private val json by lazy { Json.Default }

    override fun get(sourceId: Long): SourceAuthState? = prefs.getStringOrNull(
        KEY + sourceId,
    )?.let<String, SimpleAuthState>(json::decodeFromString)

    override fun save(state: SourceAuthState) = prefs.putString(
        KEY + state.sourceId,
        state.serializeToJson()
    )

    override fun clear(sourceId: Long) = prefs.remove(KEY + sourceId)
    override fun clear() = prefs.clear()

    private companion object {
        private const val KEY = "source_"
    }
}