package com.gnoemes.shimori.rates.edit

import com.gnoemes.shimori.model.rate.RateStatus

internal sealed class RateEditAction {
    data class ChangeStatus(val newStatus: RateStatus) : RateEditAction()
    data class RatingChanged(val newRating: Int) : RateEditAction()
    data class ProgressChanged(val newProgress: Int) : RateEditAction()
    data class ReWatchesChanged(val newRewatches: Int) : RateEditAction()
    data class CommentChanged(val newComment: String?) : RateEditAction()
    object Delete : RateEditAction()
    object Done : RateEditAction()
}