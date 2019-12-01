package com.gnoemes.shimori.search

import androidx.lifecycle.viewModelScope
import com.airbnb.mvrx.FragmentViewModelContext
import com.airbnb.mvrx.MvRxViewModelFactory
import com.airbnb.mvrx.ViewModelContext
import com.gnoemes.common.BaseViewModel
import com.gnoemes.shimori.domain.interactors.GetMyUser
import com.gnoemes.shimori.domain.interactors.SearchPaginated
import com.gnoemes.shimori.domain.launchObserve
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class SearchViewModel @AssistedInject constructor(
    @Assisted initialState: SearchViewState,
    private val searchItems: SearchPaginated,
    private val getMyUser: GetMyUser
) : BaseViewModel<SearchViewState>(initialState) {

    init {
        viewModelScope.launch {

            val job = async(searchItems.dispatcher) {
                searchItems(SearchPaginated.Params(emptyMap()))
            }

            viewModelScope.launchObserve(searchItems) {
                it.execute { copy(resuls = it()) }
            }
        }

        withState {  getMyUser(GetMyUser.Params) }
    }

    @AssistedInject.Factory
    interface Factory {
        fun create(initialState: SearchViewState): SearchViewModel
    }

    companion object : MvRxViewModelFactory<SearchViewModel, SearchViewState> {
        override fun create(viewModelContext: ViewModelContext, state: SearchViewState): SearchViewModel? {
            val fragment: SearchFragment = (viewModelContext as FragmentViewModelContext).fragment()
            return fragment.viewModelFactory.create(state)
        }
    }
}