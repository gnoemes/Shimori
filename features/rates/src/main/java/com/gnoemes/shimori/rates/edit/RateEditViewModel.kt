package com.gnoemes.shimori.rates.edit

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.Success
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
import com.gnoemes.shimori.domain.interactors.DeleteRate
import com.gnoemes.shimori.domain.launchObserve
import com.gnoemes.shimori.domain.observers.ObserveRate
import com.gnoemes.shimori.model.rate.Rate
import com.gnoemes.shimori.model.rate.RateTargetType
import com.gnoemes.shimori.model.rate.copyProgress
import com.gnoemes.shimori.model.rate.copyTargetId
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

internal class RateEditViewModel @AssistedInject constructor(
    @Assisted initialState: RateEditViewState,
    private val observeRate: ObserveRate,
    private val createOrUpdateRate: CreateOrUpdateRate,
    private val deleteRate: DeleteRate
) : BaseViewModel<RateEditViewState>(initialState) {

    private val pendingActions = Channel<RateEditAction>(Channel.BUFFERED)

    init {
        viewModelScope.launchObserve(observeRate) { flow ->
            flow.distinctUntilChanged().execute {
                if (it is Success) copy(rate = it())
                else this
            }
        }

        viewModelScope.launch {
            for (action in pendingActions) when (action) {
                is RateEditAction.ChangeStatus -> onChangeStatus(action)
                is RateEditAction.RatingChanged -> onRatingChanged(action)
                is RateEditAction.ProgressChanged -> onProgressChanged(action)
                is RateEditAction.ReWatchesChanged -> onRewatchesChanged(action)
                is RateEditAction.CommentChanged -> onCommentChanged(action)
                is RateEditAction.Delete -> onDelete()
                is RateEditAction.Done -> onCreateOrUpdate()
            }
        }

        withState {
            val id = it.rateShikimoriId
            if (id != null) observeRate(ObserveRate.Params(id))
        }
    }

    fun submitAction(action: RateEditAction) {
        viewModelScope.launch { pendingActions.send(action) }
    }

    private fun onCreateOrUpdate() {
        withState { state ->
            var rate = state.rate
            if (rate == null) {
                rate = when (state.type) {
                    RateTargetType.ANIME -> Rate.DEFAULT_ANIME
                    RateTargetType.MANGA -> Rate.DEFAULT_MANGA
                    RateTargetType.RANOBE -> Rate.DEFAULT_RANOBE
                }.copyTargetId(state.shikimoriTargetId)
            }
            createOrUpdateRate(CreateOrUpdateRate.Params(rate))
        }
    }

    private fun onDelete() {
        withState { state ->
            val rate = state.rate
            rate?.let { deleteRate(DeleteRate.Params(it)) }
        }
    }

    private fun onCommentChanged(action: RateEditAction.CommentChanged) {
        setState { copy(rate = (rate ?: Rate.EMPTY).copy(comment = action.newComment)) }
    }

    private fun onRewatchesChanged(action: RateEditAction.ReWatchesChanged) {
        setState { copy(rate = (rate ?: Rate.EMPTY).copy(reCounter = action.newRewatches)) }
    }

    private fun onProgressChanged(action: RateEditAction.ProgressChanged) {
        setState {
            copy(rate = (rate ?: Rate.EMPTY).copyProgress(newProgress = action.newProgress))
        }
    }

    private fun onRatingChanged(action: RateEditAction.RatingChanged) {
        setState { copy(rate = (rate ?: Rate.EMPTY).copy(score = action.newRating)) }
    }

    private fun onChangeStatus(action: RateEditAction.ChangeStatus) {
        setState { copy(rate = (rate ?: Rate.EMPTY).copy(status = action.newStatus)) }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: RateEditViewState): RateEditViewModel
    }

    companion object : MvRxViewModelFactory<RateEditViewModel, RateEditViewState> {
        override fun create(viewModelContext: ViewModelContext, state: RateEditViewState): RateEditViewModel? {
            val f: RateEditDialogFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            return f.viewModelFactory.create(state)
        }

        override fun initialState(viewModelContext: ViewModelContext): RateEditViewState? {
            val f: RateEditDialogFragment =
                (viewModelContext as FragmentViewModelContext).fragment()
            val args = f.requireArguments()

            return RateEditViewState(
                    rateShikimoriId = args.getLong("id").let { if (it == 0L) null else it },
                    type = args.getSerializable("type") as? RateTargetType ?: RateTargetType.ANIME,
                    name = args.getString("name") ?: "",
                    shikimoriTargetId = args.getLong("targetId").let { if (it == 0L) null else it }
            )
        }
    }

}