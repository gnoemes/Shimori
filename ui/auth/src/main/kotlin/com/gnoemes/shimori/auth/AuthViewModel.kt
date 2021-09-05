package com.gnoemes.shimori.auth

import androidx.lifecycle.ViewModel
import com.gnoemes.shikimori.ShikimoriAuthManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class AuthViewModel @Inject constructor(
    shikimoriAuthManager: ShikimoriAuthManager,
) : ViewModel(), ShikimoriAuthManager by shikimoriAuthManager