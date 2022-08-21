package com.gnoemes.shimori.lists

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.utils.ImageID
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.data.list.ListsStateManager
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
    private val stateManager: ListsStateManager,
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
        stateManager.type.observe,
        stateManager.page.observe,
        observeMyUser.flow,
        observePinsExist.flow,
        observeRatesExist.flow,
        stateManager.ratesLoading.observe,
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
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListsViewState.Empty
    )


    init {
        viewModelScope.launch {
            combine(
                stateManager.type.observe,
                stateManager.page.observe,
                observeShikimoriAuth.flow,
                stateManager.ratesLoading.observe,
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
            stateManager.uiEvents.observe
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
            when (id) {
                MESSAGE_TOGGLE_PIN -> (uiMessageManager.message.firstOrNull()?.payload as? TitleWithRateEntity)
                    ?.let {
                        togglePin(it)
                    }
                MESSAGE_INCREMENTER_UPDATE -> (uiMessageManager.message.firstOrNull()?.payload as? Rate)
                    ?.let {
                        undoIncrementerProgress(it)
                    }
            }
        }
    }

    private fun showUiEvent(event: ListsUiEvents) {
        when (event) {
            ListsUiEvents.IncrementerHint -> showIncrementerHint()
            is ListsUiEvents.PinStatusChanged -> showPinStatusChanged(event.title, event.pinned)
            is ListsUiEvents.IncrementerProgress -> showIncrementerProgress(
                event.title,
                event.oldRate,
                event.newProgress
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

    private fun showIncrementerHint() {
        viewModelScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_INCREMENTER_HINT,
                    message = textProvider[MessageID.IncrementerHint],
                    imageRes = ImageID.Tip,
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
        private const val MESSAGE_INCREMENTER_HINT = 2L
        private const val MESSAGE_INCREMENTER_UPDATE = 3L
    }
}