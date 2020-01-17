package com.gnoemes.shimori.rates

import android.os.Build
import android.view.Gravity
import android.view.Menu
import android.view.View
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.core.view.iterator
import androidx.databinding.BindingAdapter
import com.gnoemes.common.extensions.colorStateList
import com.gnoemes.common.utils.RateUtils
import com.gnoemes.shimori.base.extensions.add
import com.gnoemes.shimori.model.rate.RateStatus
import com.gnoemes.shimori.model.rate.RateTargetType
import com.google.android.material.navigation.NavigationView

@BindingAdapter("categories", "targetType", "selectedCategory", requireAll = true)
fun setupCategories(view: NavigationView,
                    oldCategories: List<RateCategory>?,
                    oldType: RateTargetType?,
                    oldCategory: RateStatus?,
                    categories: List<RateCategory>,
                    type: RateTargetType,
                    selectedCategory: RateStatus?
) {
    selectedCategory?.let {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.apply {
                setItemBackgroundResource(RateUtils.getMenuBackground(selectedCategory))
                itemTextColor = context.colorStateList(RateUtils.getTextColor(selectedCategory))
            }
        }
    }

    view.menu.run {
        if (oldCategories != categories || categories.isEmpty()) {
            clear()

            if (categories.isEmpty()) {
                add(R.string.rate_empty)
                return
            }

            categories.forEach { category ->
                add(0, category.status.priority, Menu.NONE, RateUtils.getName(type, category.status)) {
                    actionView = getRateActionView(view, category.status, category.count)
                }
            }

            setGroupCheckable(0, true, true)
        }

        if (oldCategory == selectedCategory || selectedCategory == null) return

        view.menu.iterator().forEach { item ->
            if (item.itemId == selectedCategory.priority) {
                item.actionView.isSelected = true
                view.setCheckedItem(item)
            } else {
                item.actionView?.isSelected = false
            }
        }
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