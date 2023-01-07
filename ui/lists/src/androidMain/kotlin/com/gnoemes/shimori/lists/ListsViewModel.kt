package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.list.ListsStateBus
import com.gnoemes.shimori.data.list.ListsUiEvents
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.interactors.UpdateTitleRates
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObservePinsExist
import com.gnoemes.shimori.domain.observers.ObserveRatesExist
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListsViewModel(
    private val stateBus: ListsStateBus,
    private val updateTitleRates: UpdateTitleRates,
    private val textProvider: ShimoriTextProvider,
    private val togglePin: ToggleTitlePin,
    private val updateRate: CreateOrUpdateRate,
    observeRatesExist: ObserveRatesExist,
    observePinsExist: ObservePinsExist,
    observeMyUser: ObserveMyUserShort,
    observeShikimoriAuth: ObserveShikimoriAuth,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    val state = combine(
        stateBus.type.observe,
        stateBus.page.observe,
        observeMyUser.flow,
        observePinsExist.flow,
        observeRatesExist.flow,
        stateBus.ratesLoading.observe,
        uiMessageManager.message,
    ) { type, status, user, hasPins, hasRates, isLoading, message ->
        ListsViewState(
            type = type,
            status = status,
            user = user,
            isEmpty = if (type == ListType.Pinned) !hasPins else !hasRates,
            hasRates = hasRates,
            isLoading = isLoading,
            message = message
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = ListsViewState.Empty
    )

    init {
        viewModelScope.launch {
            combine(
                stateBus.type.observe,
                stateBus.page.observe,
                observeShikimoriAuth.flow,
                stateBus.ratesLoading.observe,
            ) { type, page, auth, loading ->
                val rateType = type.rateType ?: return@combine null

                //prevent double sync
                if (loading) return@combine null

                Triple(rateType, page, auth.isAuthorized)
            }
                .filterNotNull()
                .distinctUntilChanged()
                .filter { it.third }
                .map { it.first to it.second }
                .collect(::updatePage)
        }

        viewModelScope.launch {
            stateBus.uiEvents.observe
                .collect(::showUiEvent)
        }

        observeShikimoriAuth(Unit)
        observeMyUser(Unit)
        observePinsExist(Unit)
        observeRatesExist(Unit)
    }

    fun onMessageShown(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun onMessageAction(id: Long) {
        viewModelScope.launch {
            val payload = uiMessageManager.message.firstOrNull()?.payload

            when (id) {
                MESSAGE_TOGGLE_PIN -> (payload as? TitleWithRateEntity)?.let(::togglePin)
                MESSAGE_INCREMENTER_UPDATE -> (payload as? Rate)?.let(::undoIncrementerProgress)
                MESSAGE_RATE_DELETED -> (payload as? Rate)?.let(::createRate)
            }
        }
    }

    private fun createRate(rate: Rate) {
        viewModelScope.launch {
            updateRate(CreateOrUpdateRate.Params(rate)).collect()
        }
    }

    private fun showUiEvent(event: ListsUiEvents) {
        when (event) {
            is ListsUiEvents.PinStatusChanged -> showPinStatusChanged(event.title, event.pinned)
            is ListsUiEvents.IncrementerProgress -> showIncrementerProgress(
                event.title,
                event.oldRate,
                event.newProgress
            )
            is ListsUiEvents.RateDeleted -> showRateDeleted(
                event.image,
                event.rate
            )
        }
    }

    private fun showRateDeleted(image: ShimoriImage?, rate: Rate) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_RATE_DELETED,
                    message = textProvider[MessageID.RateDeleted],
                    action = textProvider[MessageID.Undo],
                    image = image,
                    payload = rate
                )
            )
        }
    }

    private fun showIncrementerProgress(
        title: TitleWithRateEntity,
        oldRate: Rate,
        newProgress: Int
    ) {
        viewModelScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_INCREMENTER_UPDATE,
                    message = textProvider[MessageID.IncrementerFormat].format(
                        oldRate.progress,
                        newProgress
                    ),
                    image = title.entity.image,
                    action = textProvider[MessageID.Undo],
                    payload = oldRate
                )
            )
        }
    }

    private fun showPinStatusChanged(
        title: TitleWithRateEntity,
        pinned: Boolean
    ) {
        viewModelScope.launch {
            val message =
                if (pinned) MessageID.TitlePinned
                else MessageID.TitleUnPinned

            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_TOGGLE_PIN,
                    message = textProvider[message],
                    action = textProvider[MessageID.Undo],
                    image = title.entity.image,
                    payload = title
                )
            )
        }
    }

    private fun togglePin(entity: TitleWithRateEntity) {
        viewModelScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect()
        }
    }

    private fun undoIncrementerProgress(rate: Rate) {
        viewModelScope.launch {
            updateRate.invoke(CreateOrUpdateRate.Params(rate)).collect()
        }
    }

    private fun updatePage(pair: Pair<RateTargetType, RateStatus>) {
        val (type, status) = pair
        viewModelScope.launch {
            updateTitleRates(
                UpdateTitleRates.Params.optionalUpdate(
                    type = type,
                    status = status
                )
            ).collect()
        }
    }

    companion object {
        private const val MESSAGE_TOGGLE_PIN = 1L
        private const val MESSAGE_INCREMENTER_UPDATE = 2L
        private const val MESSAGE_RATE_DELETED = 3L
    }
}