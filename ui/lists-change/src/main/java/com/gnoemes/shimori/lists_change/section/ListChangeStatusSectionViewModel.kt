package com.gnoemes.shimori.lists_change.section

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.ViewModelInitializer
import com.gnoemes.shimori.data.repositories.rates.ListsStateManager
import com.gnoemes.shimori.domain.observers.ObserveListsPages
import com.gnoemes.shimori.model.rate.ListType
import com.gnoemes.shimori.model.rate.RateStatus
import dagger.Module
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

internal class ListChangeStatusSectionViewModel @AssistedInject constructor(
    @Assisted internal val rawType: Int,
    private val listsStateManager: ListsStateManager,
    observeStatuses: ObserveListsPages
) : ViewModel() {
    private val sectionType = ListType.findOrDefault(rawType)

    val state = combine(
        observeStatuses.flow,
        listsStateManager.type.observe,
        listsStateManager.page.observe
    ) { statuses, currentType, currentStatus ->

        ListChangeStatusSectionViewState(
            statuses = statuses,
            selectedStatus = if (currentType == sectionType) currentStatus else null
        )
    }

    init {
        observeStatuses(ObserveListsPages.Params(sectionType.rateType!!))
    }

    fun onStatusChanged(newStatus: RateStatus) {
        viewModelScope.launch {
            listsStateManager.type.update(sectionType)
            listsStateManager.page.update(newStatus)
        }
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            sectionType: Int
        ) =
            ViewModelProvider.Factory.from(
                ViewModelInitializer(ListChangeStatusSectionViewModel::class.java) {
                    assistedFactory.create(sectionType)
                }
            )
    }

    @Module
    @InstallIn(ActivityRetainedComponent::class)
    interface AssistedInjectModule

    @EntryPoint
    @InstallIn(ActivityComponent::class)
    internal interface ViewModelFactoryProvider {
        fun sectionFactory(): Factory
    }

    @AssistedFactory
    interface Factory {
        fun create(sectionType: Int): ListChangeStatusSectionViewModel
    }
}