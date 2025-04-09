package com.gnoemes.shimori.source.shikimori.actions

import com.gnoemes.shimori.logging.api.Logger
import com.gnoemes.shimori.source.SourceAction
import com.gnoemes.shimori.source.shikimori.ShikimoriId
import com.gnoemes.shimori.source.shikimori.ShikimoriOpenIdClient
import com.gnoemes.shimori.source.shikimori.ShikimoriValues
import com.gnoemes.shimori.source.shikimori.auth.ShikimoriAuthStore
import me.tatarka.inject.annotations.Inject

@Inject
class ShikimoriLogoutAction(
    private val values: ShikimoriValues,
    private val client: Lazy<ShikimoriOpenIdClient>,
    private val sourceId: Lazy<ShikimoriId>,
    private val store: Lazy<ShikimoriAuthStore>,
    private val logger: Logger
) : SourceAction<Unit, Unit> {

    override suspend fun invoke(args: Unit) {
        //TODO logout on website
        store.value.clear()
    }
}