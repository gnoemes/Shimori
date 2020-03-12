package com.gnoemes.shimori.main

import com.gnoemes.shimori.ShimoriAppNavigator
import com.gnoemes.shimori.base.AppNavigator

internal class MainAppNavigator(
    private val activity: MainActivity
) : ShimoriAppNavigator(activity) {

    override fun startSignIn() {
        activity.startAuth()
    }

    override fun startSignUp() {
        //TODO sign up
    }
}