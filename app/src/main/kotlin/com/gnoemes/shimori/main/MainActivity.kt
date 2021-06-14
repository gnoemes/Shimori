package com.gnoemes.shimori.main

import android.os.Bundle
import androidx.core.view.WindowCompat
import com.gnoemes.shimori.R
import com.gnoemes.shimori.common.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        WindowCompat.setDecorFitsSystemWindows(window, false)

    }
}

