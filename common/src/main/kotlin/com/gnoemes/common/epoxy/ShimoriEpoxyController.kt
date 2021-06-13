package com.gnoemes.common.epoxy

import com.airbnb.epoxy.EpoxyController
import com.airbnb.mvrx.MvRxState

abstract class ShimoriEpoxyController<T : MvRxState> : EpoxyController() {
    private var _state: T? = null
    private var allowModelBuildRequests: Boolean = false

    var state: T?
        get() = _state
        set(value) = kotlin.run {
            this._state = value
            allowModelBuildRequests = true
            requestModelBuild()
            allowModelBuildRequests = false
        }

    override fun requestModelBuild() {
        check(allowModelBuildRequests) {
            "You cannot call `requestModelBuild` directly. Call " +
                    "`setState` instead to trigger a model refresh with new data."
        }
        super.requestModelBuild()
    }

    override fun moveModel(fromPosition: Int, toPosition: Int) {
        allowModelBuildRequests = true
        super.moveModel(fromPosition, toPosition)
        allowModelBuildRequests = false
    }

    override fun requestDelayedModelBuild(delayMs: Int) {
        check(allowModelBuildRequests) {
            "You cannot call `requestModelBuild` directly." +
                    " Call `setState` instead to trigger a " + "model refresh with new data."
        }
        super.requestDelayedModelBuild(delayMs)
    }

    final override fun buildModels() {
        check(isBuildingModels) {
            "You cannot call `buildModels` directly. " +
                    "Call `setState` instead to trigger a model " + "refresh with new data."
        }

        _state?.let { buildModels(it) }
    }

    protected abstract fun buildModels(state: T)


}