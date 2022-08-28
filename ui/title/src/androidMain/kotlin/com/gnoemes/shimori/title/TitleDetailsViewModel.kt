package com.gnoemes.shimori.title

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.core.extensions.combine
import com.gnoemes.shimori.data.core.entities.rate.RateTargetType
import com.gnoemes.shimori.domain.interactors.CreateOrUpdateRate
import com.gnoemes.shimori.domain.interactors.UpdateTitle
import com.gnoemes.shimori.domain.observers.ObserveTitleWithRateEntity
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class TitleDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val updateTitle: UpdateTitle,
    private val updateRate: CreateOrUpdateRate,
    observeTitle: ObserveTitleWithRateEntity,
) : ViewModel() {
    private val id: Long = savedStateHandle["id"]!!
    private val type: RateTargetType = savedStateHandle["type"]!!

    val state = combine(
        observeTitle.flow,
        ::TitleDetailsViewState
    ).stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
        initialValue = TitleDetailsViewState()
    )

    init {
        viewModelScope.launch {
            updateTitle(UpdateTitle.Params.optionalUpdate(id, type)).collect {
                Log.i("DEVE", "$it")
            }
        }

        observeTitle(ObserveTitleWithRateEntity.Params(id, type))
    }
}