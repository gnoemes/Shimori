package com.gnoemes.shimori.auth

import androidx.lifecycle.ViewModel
import com.gnoemes.shimori.shikimori.auth.ShikimoriAuthManager

internal class AuthViewModel(
    authManager: ShikimoriAuthManager
) : ViewModel(), ShikimoriAuthManager by authManager