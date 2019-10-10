package com.gnoemes.common

import android.content.Intent
import android.os.Bundle
import androidx.core.view.doOnPreDraw
import com.airbnb.mvrx.BaseMvRxActivity
import dagger.android.AndroidInjection
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasAndroidInjector
import javax.inject.Inject

abstract class BaseActivity : BaseMvRxActivity(), HasAndroidInjector {

    @Inject
    lateinit var fragmentInjector: DispatchingAndroidInjector<Any>

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
    }

    private var postponedTransition = false

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        handleIntent(intent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun postponeEnterTransition() {
        super.postponeEnterTransition()
        postponedTransition = true
    }

    override fun startPostponedEnterTransition() {
        postponedTransition = false
        super.startPostponedEnterTransition()
    }

    fun scheduleStartPostponedTransitions() {
        if (postponedTransition) {
            window.decorView.doOnPreDraw {
                startPostponedEnterTransition()
            }
        }
    }

    open fun handleIntent(intent: Intent) {}

    override fun androidInjector(): AndroidInjector<Any> = fragmentInjector
}