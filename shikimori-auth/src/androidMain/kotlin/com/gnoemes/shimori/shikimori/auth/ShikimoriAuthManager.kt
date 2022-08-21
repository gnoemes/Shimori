package com.gnoemes.shimori.shikimori.auth

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract

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
            intent.getStringExtra(KEY_AUTH_CODE),
            intent.getStringExtra(KEY_AUTH_ERROR),
        )
    }

    data class Result(
        val authCode: String?,
        val error: String?,
    )
}


//Flow: START -> DONE
// or START -> TRIGGER_URL -> TARGET_URL -> DONE
// we need trigger and target because shikimori doesn't support redirect after registration
const val KEY_START_URL = "start_url"
const val KEY_TRIGGER_URL = "trigger_url"
const val KEY_TARGET_URL = "target_url"

const val KEY_AUTH_CODE = "code"
const val KEY_AUTH_ERROR = "error"
const val KEY_AUTH_CODE_PATTERN = "code_pattern"
