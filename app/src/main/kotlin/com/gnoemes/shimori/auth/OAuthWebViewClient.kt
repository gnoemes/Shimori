package com.gnoemes.shimori.auth

import android.webkit.WebResourceRequest
import android.webkit.WebView
import com.google.accompanist.web.AccompanistWebViewClient
import com.google.accompanist.web.WebContent
import java.util.regex.Pattern

internal class OAuthWebViewClient(
    private val triggerUrl: String?,
    private val targetUrl: String?,
    private val codePattern: Pattern,
    private val onResult: (String?, String?) -> Unit
) : AccompanistWebViewClient() {

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString()
        interceptCode(url)
        return super.shouldOverrideUrlLoading(view, request)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        if (url == triggerUrl && targetUrl != null) {
            state.content = state.content.withUrl(targetUrl)
        }
    }

    private fun interceptCode(url: CharSequence?) {
        if (url == null) return

        val matcher = codePattern.matcher(url)
        if (matcher.find()) {
            val authCode =
                if (matcher.group(1) == "error") null
                else matcher.group(2)
            val error =
                if (matcher.group(1) == "error") matcher.group(2)
                else null
            onResult(authCode, error)
        }
    }

    private fun WebContent.withUrl(url: String) = when (this) {
        is WebContent.Url -> copy(url = url)
        else -> WebContent.Url(url)
    }
}