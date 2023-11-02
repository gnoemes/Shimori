package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject

@OptIn(ExperimentalSettingsApi::class)
@Inject
class ShimoriPreferencesImpl(
    private val storage: AppObservablePreferences,
    dispatchers: AppCoroutineDispatchers,
) : ShimoriPreferences {

    private val flowSettings by lazy { storage.toFlowSettings(dispatchers.io) }

    private companion object {
        const val SHIKIMORI_ACCESS_TOKEN = "SHIKIMORI_ACCESS_TOKEN"
        const val SHIKIMORI_REFRESH_TOKEN = "SHIKIMORI_REFRESH_TOKEN"
        const val CURRENT_CATALOGUE_SOURCE = "CURRENT_CATALOGUE_SOURCE"
        const val PREFERRED_LIST = "PREFERRED_LIST"
        const val PREFERRED_STATUS = "PREFERRED_STATUS"
    }

    override var currentCatalogueSource: String?
        get() = storage.getStringOrNull(CURRENT_CATALOGUE_SOURCE)
        set(value) = storage.putString(CURRENT_CATALOGUE_SOURCE, value ?: "")
    override var shikimoriAccessToken: String?
        get() = storage.getStringOrNull(SHIKIMORI_ACCESS_TOKEN)
        set(value) = storage.putString(SHIKIMORI_ACCESS_TOKEN, value ?: "")
    override var shikimoriRefreshToken: String?
        get() = storage.getStringOrNull(SHIKIMORI_REFRESH_TOKEN)
        set(value) = storage.putString(SHIKIMORI_REFRESH_TOKEN, value ?: "")
    override var preferredListType: Int
        get() = storage.getInt(PREFERRED_LIST, 1)
        set(value) = storage.putInt(PREFERRED_LIST, value)

    override fun observePreferredListType(): Flow<Int> = flowSettings.getIntFlow(PREFERRED_LIST, 1)

    override var preferredListStatus: String
        get() = storage.getString(PREFERRED_STATUS, "WATCHING")
        set(value) = storage.putString(PREFERRED_STATUS, value)

    override fun observePreferredListStatus(): Flow<String> =
        flowSettings.getStringFlow(PREFERRED_LIST, "WATCHING")
}