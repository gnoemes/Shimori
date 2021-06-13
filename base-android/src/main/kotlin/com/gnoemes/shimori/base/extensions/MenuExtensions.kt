package com.gnoemes.shimori.base.extensions

import android.view.Menu
import android.view.MenuItem

inline fun Menu.add(groupId: Int, itemId: Int, order: Int, titleRes: Int, crossinline action: MenuItem.() -> Unit) {
    this.add(groupId, itemId, order, titleRes)
    action(findItem(itemId))
}