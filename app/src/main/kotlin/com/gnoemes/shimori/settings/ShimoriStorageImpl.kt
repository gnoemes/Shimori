package com.gnoemes.shimori.settings

import android.content.Context
import androidx.core.content.edit
import com.gnoemes.shimori.base.core.settings.ShimoriStorage
import com.gnoemes.shimori.base.core.settings.ShimoriStorage.Companion.CURRENT_CATALOGUE_SOURCE
import com.gnoemes.shimori.base.core.settings.ShimoriStorage.Companion.SHIKIMORI_ACCESS_TOKEN
import com.gnoemes.shimori.base.core.settings.ShimoriStorage.Companion.SHIKIMORI_REFRESH_TOKEN

internal class ShimoriStorageImpl(context: Context) : ShimoriStorage {

    private val prefs = context.getSharedPreferences("local_storage", Context.MODE_PRIVATE)

    override var currentCatalogueSource: String?
        get() = prefs.getString(CURRENT_CATALOGUE_SOURCE, null)
        set(value) = prefs.edit { putString(CURRENT_CATALOGUE_SOURCE, value) }

    override var shikimoriAccessToken: String?
        get() = prefs.getString(SHIKIMORI_ACCESS_TOKEN, null)
        set(value) = prefs.edit { putString(SHIKIMORI_ACCESS_TOKEN, value) }

    override var shikimoriRefreshToken: String?
        get() = prefs.getString(SHIKIMORI_REFRESH_TOKEN, null)
        set(value) = prefs.edit { putString(SHIKIMORI_REFRESH_TOKEN, value) }
}