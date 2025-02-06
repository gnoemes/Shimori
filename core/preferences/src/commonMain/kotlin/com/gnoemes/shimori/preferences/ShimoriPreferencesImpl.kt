package com.gnoemes.shimori.preferences

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.russhwolf.settings.ExperimentalSettingsApi
import com.russhwolf.settings.coroutines.toFlowSettings
import kotlinx.coroutines.flow.Flow
import me.tatarka.inject.annotations.Inject
import software.amazon.lastmile.kotlin.inject.anvil.AppScope
import software.amazon.lastmile.kotlin.inject.anvil.ContributesBinding
import software.amazon.lastmile.kotlin.inject.anvil.SingleIn

@OptIn(ExperimentalSettingsApi::class)
@Inject
@SingleIn(AppScope::class)
@ContributesBinding(AppScope::class)
class ShimoriPreferencesImpl(
    private val storage: AppObservablePreferences,
    dispatchers: AppCoroutineDispatchers,
) : ShimoriPreferences {

    private val flowSettings by lazy { storage.toFlowSettings(dispatchers.io) }

    private companion object {
        const val CURRENT_CATALOGUE_SOURCE = "CURRENT_CATALOGUE_SOURCE"
        const val PREFERRED_LIST = "PREFERRED_LIST"
        const val PREFERRED_STATUS = "PREFERRED_STATUS"
    }

    override var currentCatalogueSource: String?
        get() = storage.getStringOrNull(CURRENT_CATALOGUE_SOURCE)
        set(value) = storage.putString(CURRENT_CATALOGUE_SOURCE, value ?: "")

    override var preferredListType: String
        get() = storage.getString(PREFERRED_LIST, "ANIME")
        set(value) = storage.putString(PREFERRED_LIST, value)

    override fun observePreferredListType(): Flow<String> =
        flowSettings.getStringFlow(PREFERRED_LIST, "ANIME")

    override var preferredListStatus: String
        get() = storage.getString(PREFERRED_STATUS, "WATCHING")
        set(value) = storage.putString(PREFERRED_STATUS, value)

    override fun observePreferredListStatus(): Flow<String> =
        flowSettings.getStringFlow(PREFERRED_STATUS, "WATCHING")

    override fun setInt(key: String, value: Int) {
        storage.putInt(key, value)
    }

    override fun getInt(key: String): Int? = storage.getIntOrNull(key)

    override fun setString(key: String, value: String) {
        storage.putString(key, value)
    }

    override fun getString(key: String): String? = storage.getStringOrNull(key)
}