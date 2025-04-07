package com.gnoemes.shimori.common.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.runtime.staticCompositionLocalOf
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppLocale.Companion.toTag
import java.util.Locale

actual object LocalAppLocale {
    private val LocalAppLocale = staticCompositionLocalOf { Locale.getDefault().toLanguageTag() }

    private var default: Locale? = null

    actual val current: String
        @Composable
        get() = Locale.getDefault().toLanguageTag()

    @Composable
    actual infix fun provides(value: AppLocale): ProvidedValue<*> {
        if (default == null) {
            default = Locale.forLanguageTag(AppLocale.English.toTag())
        }

        val new = when (value) {
            AppLocale.Russian -> Locale.forLanguageTag(value.toTag())
            else -> default!!
        }

        Locale.setDefault(new)
        return LocalAppLocale.provides(new.toLanguageTag())
    }
}