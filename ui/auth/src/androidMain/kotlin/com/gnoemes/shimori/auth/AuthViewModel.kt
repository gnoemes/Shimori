package com.gnoemes.shimori.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.shikimori.auth.ShikimoriAuthManager
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants.ERROR_REASON_ACCESS_DENIED
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class AuthViewModel(
    authManager: ShikimoriAuthManager,
    private val shikimori: Shikimori,
    private val textProvider: ShimoriTextProvider,
) : ViewModel(), ShikimoriAuthManager by authManager {

    private val uiMessageManager = UiMessageManager()

    val error: Flow<UiMessage?> = uiMessageManager.message

    init {
        viewModelScope.launch {
            shikimori.authError.collect { reason ->
                val message = when (reason) {
                    ERROR_REASON_ACCESS_DENIED -> textProvider[MessageID.OAuthAccessDenied]
                    else -> null
                }

                message?.let {
                    uiMessageManager.emitMessage(
                        UiMessage(
                            message = message,
                        )
                    )
                }
            }
        }
    }

    fun onMessageShown(id: Long) {
        viewModelScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}