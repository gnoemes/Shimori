package com.gnoemes.shimori.data.character

import com.gnoemes.shimori.base.extensions.inPast
import com.gnoemes.shimori.data.app.ExpiryConstants
import com.gnoemes.shimori.data.app.Request
import com.gnoemes.shimori.data.app.SourceResponse
import com.gnoemes.shimori.data.characters.CharacterInfo
import com.gnoemes.shimori.data.db.api.db.DatabaseTransactionRunner
import com.gnoemes.shimori.data.lastrequest.EntityLastRequestStore
import com.gnoemes.shimori.data.source.catalogue.CatalogueManager
import com.gnoemes.shimori.logging.api.Logger
import kotlinx.datetime.Instant
import me.tatarka.inject.annotations.Inject
import kotlin.time.Duration.Companion.minutes

@Inject
class CharacterRepository(
    private val catalogue: CatalogueManager,
    private val store: SyncedCharacterStore,
    private val roleStore: CharacterRoleStore,
    private val entityLastRequest: EntityLastRequestStore,
    private val logger: Logger,
    private val transactionRunner: DatabaseTransactionRunner
) {
    fun observeById(id: Long) = store.dao.observeById(id)

    suspend fun sync(id: Long): SourceResponse<CharacterInfo> {
        val local = store.dao.queryById(id)
            ?: throw IllegalStateException("Character with id: $id not found")

        return catalogue.character { get(local) }
            .also {
                transactionRunner {
                    store.trySync(it)
                    roleStore.trySync(it)
                    characterUpdated(id)
                }
            }
    }

    suspend fun <T> trySync(data: SourceResponse<T>) {
        transactionRunner {
            store.trySync(data)
            roleStore.trySync(data)
        }
    }

    fun needUpdateCharacter(
        id: Long,
        expiry: Instant = ExpiryConstants.CHARACTER_DETAILS.minutes.inPast
    ) = entityLastRequest.isRequestBefore(
        Request.CHARACTER_DETAILS,
        id,
        expiry
    )

    fun characterUpdated(id: Long) =
        entityLastRequest.updateLastRequest(Request.CHARACTER_DETAILS, id)
}