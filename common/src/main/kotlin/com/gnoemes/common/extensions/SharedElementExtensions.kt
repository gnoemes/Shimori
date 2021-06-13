package com.gnoemes.common.extensions

import android.app.Activity
import android.os.Bundle
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.fragment.app.FragmentTransaction
import androidx.navigation.ActivityNavigatorExtras
import androidx.navigation.Navigator
import androidx.navigation.fragment.FragmentNavigator
import com.gnoemes.common.utils.SharedElementHelper

fun SharedElementHelper.toBundle(activity: Activity): Bundle? {
    return toActivityOptions(activity)?.toBundle()
}

fun SharedElementHelper.toActivityOptions(activity: Activity): ActivityOptionsCompat? {
    if (!isEmpty()) {
        return ActivityOptionsCompat.makeSceneTransitionAnimation(
            activity,
            *sharedElements.map { Pair(it.key, it.value) }.toTypedArray()
        )
    }
    return null
}

fun SharedElementHelper.applyToTransaction(tx: FragmentTransaction) {
    for ((view, customTransitionName) in sharedElements) {
        tx.addSharedElement(view, customTransitionName!!)
    }
}

fun SharedElementHelper.toFragmentNavigatorExtras(): Navigator.Extras {
    return FragmentNavigator.Extras.Builder()
        .addSharedElements(sharedElements)
        .build()
}

fun SharedElementHelper.toActivityNavigatorExtras(activity: Activity): Navigator.Extras {
    return ActivityNavigatorExtras(toActivityOptions(activity))
}
