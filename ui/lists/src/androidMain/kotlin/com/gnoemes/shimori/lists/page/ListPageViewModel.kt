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
import com.gnoemes.shimori.data.core.entities.common.TitleStatus
import com.gnoemes.shimori.data.core.entities.rate.Rate
import com.gnoemes.shimori.data.core.entities.rate.RateSort
import com.gnoemes.shimori.data.list.ListsStateManager
import com.gnoemes.shimori.data.paging.PagingConfig
import com.gnoemes.shimori.data.paging.PagingData
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
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
    private val updateRate: CreateOrUpdateRate,
    observeMyUser: ObserveMyUserShort
) : ViewModel() {
    private val uiMessageManager = UiMessageManager()
    private val _uiEvents = MutableSharedFlow<UiEvents>()

    private val incrementerEvents = MutableStateFlow<TitleWithRateEntity?>(null)

    val uiEvents: SharedFlow<UiEvents> get() = _uiEvents

    val state = combine(
        stateManager.type.observe,
        stateManager.page.observe,
        observeMyUser.flow,
        uiMessageManager.message,
        incrementerEvents,
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
                MESSAGE_INCREMENTER_UPDATE -> (uiMessageManager.message.firstOrNull()?.payload as? Rate)
                    ?.let {
                        undoIncrementerProgress(it)
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

    fun showIncrementerHint() {
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

    fun showIncrementer(title: TitleWithRateEntity) {
        viewModelScope.launch {
            incrementerEvents.value = title
        }
    }

    fun updateProgressFromIncrementer(newProgress: Int) {
        viewModelScope.launch {
            //get title from incrementer
            val title = incrementerEvents.value ?: return@launch
            //hide incrementer
            incrementerEvents.value = null

            val oldRate = title.rate ?: return@launch
            val newRate = oldRate.copy(progress = newProgress)

            if (oldRate.progress == newProgress) return@launch

            updateRate.invoke(
                CreateOrUpdateRate.Params(newRate)
            ).collect {
                if (it.isSuccess) {
                    //if title fully released and progress is max, show edit rate sheet
                    if (title.entity.status == TitleStatus.RELEASED &&
                        title.entity.size == newProgress
                    ) {
                        _uiEvents.emit(UiEvents.EditRate(title))
                    } else {
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
            }
        }
    }

    private fun undoIncrementerProgress(rate: Rate) {
        viewModelScope.launch {
            updateRate.invoke(CreateOrUpdateRate.Params(rate)).collect()
        }
    }

    companion object {
        private val PAGING_CONFIG = PagingConfig(
            pageSize = 10,
            initialLoadSize = 15,
            prefetchDistance = 5
        )

        private const val MESSAGE_TOGGLE_PIN = 1L
        private const val MESSAGE_INCREMENTER_HINT = 2L
        private const val MESSAGE_INCREMENTER_UPDATE = 3L
    }
}