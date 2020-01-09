package com.gnoemes.shimori.rates

import android.os.Build
import android.view.Gravity
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import com.gnoemes.common.extensions.colorStateList
import com.gnoemes.common.utils.RateUtils
import com.gnoemes.shimori.model.rate.RateStatus
import com.google.android.material.navigation.NavigationView

@BindingAdapter("selectedStatus")
fun selectedStatus(view: NavigationView,
                     oldCategory: RateStatus?,
                     category: RateStatus?) {
    if (oldCategory == category || category == null) return

    view.menu.apply {
        findItem(category.priority)?.let { item ->
            item.actionView.isSelected = true
            view.setCheckedItem(item)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.apply {
                    setItemBackgroundResource(RateUtils.getMenuBackground(category))
                    itemTextColor = context.colorStateList(RateUtils.getTextColor(category))
                }
            }
        }
    }
}

@BindingAdapter("categories")
fun categories(view: NavigationView,
               oldCategories: List<RateCategory>?,
               categories: List<RateCategory>) {
    if (oldCategories == categories) return

    view.menu.apply {
        clear()

        if (categories.isEmpty()) {
            add(R.string.rate_empty)
            return
        }

        categories.forEach {
            add(0, it.status.priority, it.status.priority, RateUtils.getName(it.targetType, it.status))
            findItem(it.status.priority)?.actionView = getRateActionView(view, it.status, it.count)
        }

        setGroupCheckable(0, true, true)
    }

}

@BindingAdapter("categoriesRefreshing")
fun categoriesRefreshing(view: NavigationView, refreshing: Boolean) {
    view.findViewById<View>(R.id.design_navigation_view)?.isVisible = !refreshing
}

private fun getRateActionView(parent: View, status: RateStatus, count: Int): TextView {
    val view = TextView(parent.context)
    view.text = "$count"
    view.gravity = Gravity.CENTER_VERTICAL

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        view.setTextColor(parent.context.colorStateList(RateUtils.getTextColor(status)))
    }

    return view
}