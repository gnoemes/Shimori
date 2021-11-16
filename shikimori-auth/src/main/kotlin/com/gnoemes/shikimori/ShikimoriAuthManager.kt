package com.gnoemes.shikimori

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

interface ShikimoriAuthManager {
    fun buildLoginActivityResult(): LoginShikimori = LoginShikimori(buildLoginIntent())
    fun buildRegisterActivityResult(): LoginShikimori = LoginShikimori(buildRegisterIntent())
    fun buildLoginIntent(): Intent
    fun buildRegisterIntent(): Intent
    fun onLoginResult(result: LoginShikimori.Result)
}

class LoginShikimori internal constructor(
    private val loginIntent: Intent,
) : ActivityResultContract<Unit, LoginShikimori.Result?>() {

    override fun createIntent(context: Context, input: Unit): Intent = loginIntent

    override fun parseResult(
        resultCode: Int,
        intent: Intent?
    ): Result? = intent?.let {
        Result(
                AuthorizationResponse.fromIntent(it),
                AuthorizationException.fromIntent(it)
        )
    }

    data class Result(
        val response: AuthorizationResponse?,
        val exception: AuthorizationException?
    )
}