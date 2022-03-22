package com.gnoemes.shimori.lists.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingConfig
import com.gnoemes.shimori.common.api.UiMessage
import com.gnoemes.shimori.common.api.UiMessageManager
import com.gnoemes.shimori.common.utils.MessageID
import com.gnoemes.shimori.common.utils.ShimoriUiMessageTextProvider
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.interactors.GetRandomTitleWithStatus
import com.gnoemes.shimori.domain.interactors.ToggleListPin
import com.gnoemes.shimori.domain.observers.ObserveMyUserShort
import com.gnoemes.shimori.domain.observers.ObserveRateSort
import com.gnoemes.shimori.model.EntityWithRate
import com.gnoemes.shimori.model.ShikimoriContentEntity
import com.gnoemes.shimori.model.ShimoriEntity
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateSort
import com.gnoemes.shimori.model.rate.RateStatus
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

internal abstract class BasePageViewModel(
    private val listManager: ListsStateManager,
    private val uiMessageTextProvider: ShimoriUiMessageTextProvider,
    observeMy: ObserveMyUserShort,
    private val observeRateSort: ObserveRateSort,
    private val getRandomTitleWithStatus: GetRandomTitleWithStatus,
    private val togglePin: ToggleListPin,
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()

    protected abstract val listType: ListType
    abstract suspend fun loadPage(status: RateStatus, sort: RateSort)

    val state = combine(
        listManager.type.observe,
        listManager.page.observe,
        observeMy.flow,
        uiMessageManager.message,
        ::ListPageViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = ListPageViewState.Empty
    )

    init {
        viewModelScope.launch {
            combine(
                listManager.page.observe,
                observeRateSort.flow,
            ) { status, sort ->
                status to sort
            }
                .distinctUntilChanged()
                .collect { loadPage(it.first, it.second ?: RateSort.defaultForType(listType)) }
        }

        observeRateSort(ObserveRateSort.Params(listType))
        observeMy(Unit)
    }

    fun showMessage(id: MessageID) {
        viewModelScope.launch {
//            uiMessageManager.emitMessage()
        }
    }

    fun onMessageShown(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }

    fun onMessageAction(id: Long) {
        viewModelScope.launch {
            when (id) {
                MESSAGE_TOGGLE_PIN -> (uiMessageManager.message.firstOrNull()?.payload as? EntityWithRate<out ShimoriEntity>)
                    ?.let {
                        togglePin(it, notify = false)
                    }
            }
        }
    }

    fun togglePin(entity: EntityWithRate<out ShimoriEntity>, notify: Boolean = true) {
        viewModelScope.launch {
            togglePin(ToggleListPin.Params(entity.type, entity.id))
                .collect { pinned ->
                    if (notify) {
                        val message = uiMessageTextProvider.text(
                            if (pinned) MessageID.TitlePinned
                            else MessageID.TitleUnPinned
                        )

                        uiMessageManager.emitMessage(
                            UiMessage(
                                id = MESSAGE_TOGGLE_PIN,
                                message = message,
                                action = uiMessageTextProvider.text(MessageID.Undo),
                                shimoriImage = (entity.entity as? ShikimoriContentEntity)?.image,
                                payload = entity
                            )
                        )
                    }
                }
        }
    }

    companion object {
        val PAGING_CONFIG = PagingConfig(
            pageSize = 70,
            initialLoadSize = 70,
            prefetchDistance = 20
        )

        private const val MESSAGE_TOGGLE_PIN = 1L
    }
}