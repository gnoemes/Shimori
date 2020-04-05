package com.gnoemes.shimori.rates.edit

import com.airbnb.epoxy.EpoxyController
import com.gnoemes.shimori.base.extensions.observable
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.rates.editRateStatus
import javax.inject.Inject

internal class RateEditStatusController @Inject constructor(
) : EpoxyController() {

    var callbacks: Callbacks? by observable(null, ::requestModelBuild)
    var state by observable(RateEditViewState(), ::requestModelBuild)

    interface Callbacks {
        fun onStatusClicked(status: RateStatus)
    }

    override fun buildModels() {
        val items = state.statuses
        val selectedStatus = state.rate?.status

        items.forEach { item ->
            editRateStatus {
                id(item.priority)
                type(state.type)
                status(item)
                selected(item == selectedStatus)
                callback(callbacks)
            }
        }
    }

    fun clear() {
        callbacks = null
    }

}