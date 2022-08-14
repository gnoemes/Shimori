package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.utils.ImageID
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.data.core.entities.TitleWithRateEntity
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.domain.interactors.ToggleTitlePin
import com.gnoemes.shimori.domain.observers.ObserveListPage
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal class ListPageViewModel(
    private val stateManager: ListsStateManager,
    private val observeListPage: ObserveListPage,
    private val observeRateSort: ObserveRateSort,
    private val togglePin: ToggleTitlePin,
    private val textProvider: ShimoriTextProvider,
    observeMyUser: ObserveMyUserShort
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    val state = combine(
        stateManager.type.observe,
        stateManager.page.observe,
        observeMyUser.flow,
        uiMessageManager.message,
        ::ListPageViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListPageViewState.Empty
    )

    val items = observeListPage.flow
        .filterIsInstance<PagingData<TitleWithRateEntity>>()
        .cachedIn(viewModelScope)

    init {
        viewModelScope.launch {
            combine(
                stateManager.type.observe,
                stateManager.page.observe,
                observeRateSort.flow,
            ) { type, status, sort ->
                ObserveListPage.Params(
                    type,
                    status,
                    sort ?: RateSort.defaultForType(type),
                    PAGING_CONFIG
                )
            }
                .distinctUntilChanged()
                .collect(observeListPage::invoke)
        }

        viewModelScope.launch {
            stateManager.type.observe
                .map(ObserveRateSort::Params)
                .collect(observeRateSort::invoke)
        }

        observeMyUser(Unit)
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
                        togglePin(it, notify = false)
                    }
            }
        }
    }

    fun togglePin(entity: TitleWithRateEntity, notify: Boolean = true) {
        viewModelScope.launch {
            togglePin(ToggleTitlePin.Params(entity.type, entity.id)).collect { pinned ->
                if (notify) {
                    val message =
                        if (pinned) MessageID.TitlePinned
                        else MessageID.TitleUnPinned

                    uiMessageManager.emitMessage(
                        UiMessage(
                            id = MESSAGE_TOGGLE_PIN,
                            message = textProvider[message],
                            action = textProvider[MessageID.Undo],
                            image = entity.entity.image,
                            payload = entity
                        )
                    )
                }
            }
        }
    }

    fun onIncrementClick() {
        viewModelScope.launch {
            uiMessageManager.emitMessage(
                UiMessage(
                    id = MESSAGE_INCREMENTOR_HINT,
                    message = textProvider[MessageID.IncrementorHint],
                    imageRes = ImageID.Tip,
                )
            )
        }
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 5
        )

        private const val MESSAGE_TOGGLE_PIN = 1L
        private const val MESSAGE_INCREMENTOR_HINT = 2L
    }
}