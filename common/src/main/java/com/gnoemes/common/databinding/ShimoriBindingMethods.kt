package com.gnoemes.common.databinding

import android.view.View
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout


@BindingMethods(
    BindingMethod(type = View::class, attribute = "outlineProviderInstance", method = "setOutlineProvider"),
    BindingMethod(type = SwipeRefreshLayout::class, attribute = "isRefreshing", method = "setRefreshing"),
    BindingMethod(type = View::class, attribute = "clipToOutline", method = "setClipToOutline"),
    BindingMethod(type = View::class, attribute = "activated", method = "setActivated"),
    BindingMethod(type = View::class, attribute = "selected", method = "setSelected"),
    BindingMethod(type = View::class, attribute = "onLongClick", method = "setOnLongClickListener")
)
class ShimoriBindingMethods