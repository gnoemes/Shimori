package com.gnoemes.shimori.domain.interactors

import com.gnoemes.shimori.base.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.data.repositories.manga.MangaRepository
import com.gnoemes.shimori.domain.Interactor
import com.gnoemes.shimori.model.rate.RateStatus
import javax.inject.Inject

class UpdateMangaRates @Inject constructor(
    private val repository: MangaRepository,
    private val dispatchers: AppCoroutineDispatchers
) : Interactor<UpdateMangaRates.Params>() {

    override suspend fun doWork(params: Params) {
        with(dispatchers.io) {
            if (params.force) {
                repository.updateMyMangaWithStatus(params.status)
            } else if (params.optionalUpdate && repository.needUpdateMangaWithStatus()) {
                repository.updateMyMangaWithStatus(params.status)
            }
        }
    }

    data class Params(
        val force: Boolean,
        val optionalUpdate: Boolean,
        val status: RateStatus?
    ) {
        companion object {
            val OptionalUpdate = Params(force = false, optionalUpdate = true, status = null)
        }
    }
}