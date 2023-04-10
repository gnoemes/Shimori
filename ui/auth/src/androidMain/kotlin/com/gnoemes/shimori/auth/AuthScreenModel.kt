package com.gnoemes.shimori.auth

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.coroutineScope
import com.gnoemes.shikimori.Shikimori
import com.gnoemes.shimori.base.core.utils.AppCoroutineDispatchers
import com.gnoemes.shimori.common.ui.api.UiMessage
import com.gnoemes.shimori.common.ui.api.UiMessageManager
import com.gnoemes.shimori.common.ui.utils.MessageID
import com.gnoemes.shimori.common.ui.utils.ShimoriTextProvider
import com.gnoemes.shimori.common.ui.utils.get
import com.gnoemes.shimori.shikimori.auth.ShikimoriAuthManager
import com.gnoemes.shimori.shikimori.auth.ShikimoriConstants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

internal class AuthScreenModel(
    authManager: ShikimoriAuthManager,
    private val shikimori: Shikimori,
    private val textProvider: ShimoriTextProvider,
    dispatchers: AppCoroutineDispatchers,
) : ScreenModel, ShikimoriAuthManager by authManager {

    private val uiMessageManager = UiMessageManager()

    val error: Flow<UiMessage?> = uiMessageManager.message

    init {
        coroutineScope.launch(dispatchers.io) {
            shikimori.authError.collect { reason ->
                val message = when (reason) {
                    ShikimoriConstants.ERROR_REASON_ACCESS_DENIED -> textProvider[MessageID.OAuthAccessDenied]
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
        coroutineScope.launch {
            uiMessageManager.clearMessage(id)
        }
    }
}