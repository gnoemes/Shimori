package com.gnoemes.shimori.domain.observers

import com.gnoemes.shimori.data.repositories.rates.RateRepository
import com.gnoemes.shimori.domain.SubjectInteractor
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateTargetType
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveRate @Inject constructor(
    private val rateRepository: RateRepository,
) : SubjectInteractor<ObserveRate.Params, Rate?>() {

    override fun createObservable(params: Params): Flow<Rate?> {
        return when {
            params.id != null -> rateRepository.observeById(params.id)
            params.shikimoriId != null -> rateRepository.observeByShikimoriId(params.shikimoriId)
            params.targetId != null && params.targetType != null -> rateRepository.observeByTarget(params.targetId, params.targetType)
            else -> throw IllegalStateException("Empty params")
        }
    }

    data class Params(
        val id: Long? = null,
        val shikimoriId: Long? = null,
        val targetId: Long? = null,
        val targetType: RateTargetType? = null
    ) {
        companion object {
            fun byId(id: Long) = Params(id = id)
            fun byShikimoriId(shikimoriId: Long) = Params(shikimoriId = shikimoriId)
            fun byTarget(targetId: Long, targetType: RateTargetType) =
                Params(targetId = targetId, targetType = targetType)
        }
    }
}