package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.base.core.utils.Logger
import com.gnoemes.shimori.data.core.entities.app.SyncAction
import com.gnoemes.shimori.data.core.entities.app.SyncApi
import com.gnoemes.shimori.data.repositories.rate.RateRepository
import com.gnoemes.shimori.data.repositories.rate.SyncPendingRatesLastRequestStore
import com.gnoemes.shimori.domain.Interactor
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class SyncPendingRates(
    private val rateRepository: RateRepository,
    private val logger: Logger,
    private val syncPendingRatesLastRequest: SyncPendingRatesLastRequestStore,
    private val dispatchers: AppCoroutineDispatchers,
) : Interactor<Unit>() {

    private companion object {
        const val SYNC_TAG = "SyncPendingRates"
    }

    override suspend fun doWork(params: Unit) {
        withContext(dispatchers.io + SupervisorJob()) {
            val ratesToSync = rateRepository.querySyncPendingRates()

            logger.d(
                message = "Pending rates sync started. Rates to sync: ${ratesToSync.size}",
                tag = SYNC_TAG
            )

            ratesToSync.forEachIndexed { index, toSync ->
                //only 1 target now
                val target = toSync.targets.firstOrNull() ?: return@forEachIndexed

                val rate = rateRepository.queryById(toSync.id)

                logger.d(
                    message = "#$index: target: ${target.api} action: ${toSync.action}, attempts: ${toSync.attempts}, last attempt: ${toSync.lastAttempt}",
                    tag = SYNC_TAG
                )

                if (rate == null && toSync.action != SyncAction.DELETE) {
                    logger.d(
                        message = "#$index: Rate is missing. Deleting ...",
                        tag = SYNC_TAG
                    )
                    rateRepository.delete(toSync)
                    return@forEachIndexed
                }

                try {
                    launch {
                        when (toSync.action) {
                            SyncAction.DELETE -> rateRepository.deleteFromTarget(target)
                            else -> rateRepository.createOrUpdateOnTarget(rate!!, target)
                        }
                    }.join()

                    rateRepository.delete(toSync)
                } catch (e: Exception) {
                    logger.d(
                        message = "#$index: sync error $e \ncause: ${e.cause}",
                        tag = SYNC_TAG
                    )

                    val httpException =
                        if (e is ClientRequestException) e
                        else if (e.cause is ClientRequestException) e.cause as ClientRequestException
                        else null
                    if (httpException != null && httpException.response.status == HttpStatusCode.NotFound) {
                        rateRepository.delete(toSync)
                    } else {
                        rateRepository.createOrUpdate(
                            toSync.copy(
                                attempts = toSync.attempts + 1,
                                lastAttempt = Clock.System.now()
                            )
                        )
                    }

                    logger.d(
                        message = "#$index: Fail!",
                        tag = SYNC_TAG
                    )

                    return@forEachIndexed
                }

                logger.d(
                    message = "#$index: Success!",
                    tag = SYNC_TAG
                )

                //make a window for ui requests (more priority)
                if (target.api == SyncApi.Shikimori) {
                    delay(200)
                }
            }

            syncPendingRatesLastRequest.updateLastRequest()
        }
    }
}