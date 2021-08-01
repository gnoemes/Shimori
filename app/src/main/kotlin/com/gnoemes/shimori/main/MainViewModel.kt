package com.gnoemes.shimori.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shimori.base.settings.ShimoriPreferences
import com.gnoemes.shimori.domain.interactors.DeleteMyUser
import com.gnoemes.shimori.domain.interactors.UpdateUser
import com.gnoemes.shimori.domain.observers.ObserveShikimoriAuth
import com.gnoemes.shimori.model.rate.RateTargetType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val prefs: ShimoriPreferences,
    observeShikimoriAuth: ObserveShikimoriAuth,
    updateUser: UpdateUser,
    deleteMyUser: DeleteMyUser,
) : ViewModel() {

    private val rateTargetType =
        MutableStateFlow(RateTargetType.findOrDefault(prefs.preferredRateType))

    private val pendingActions = MutableSharedFlow<MainAction>()

    val state = combine(
            rateTargetType,
            observeShikimoriAuth.observe().distinctUntilChanged()
    ) { rateTargetType, authState ->
        MainViewState(
                rateTargetType = rateTargetType,
                authState = authState
        )
    }

    init {
        viewModelScope.launch {
            observeShikimoriAuth.observe()
                .distinctUntilChanged()
                .collect {
                    if (it.isAuthorized) {
                        updateUser.executeSync(UpdateUser.Params(null, isMe = true))
                    } else {
                        deleteMyUser.executeSync(Unit)
                    }
                }
        }

        observeShikimoriAuth(Unit)

        viewModelScope.launch {
            pendingActions.collect { action ->
                when (action) {
                    MainAction.Random -> openRandomTitle()
                    is MainAction.ChangeRateType -> changeRateType(action.rateTargetType)
                }
            }
        }
    }

    internal fun submitAction(action: MainAction) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun openRandomTitle() {
        //TODO callback to lists
    }

    private fun changeRateType(newType: RateTargetType) {
        //TODO callback to lists
        viewModelScope.launch {
            rateTargetType.emit(newType)
        }
        prefs.preferredRateType = newType.type
    }

}