package com.gnoemes.shimori.common.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.gnoemes.shimori.settings.AppLocale
import com.gnoemes.shimori.settings.AppLocale.Companion.toTag
import java.util.Locale

actual object LocalAppLocale {
    private var default: Locale? = null

    actual val current: String
        @Composable
        get() = Locale.getDefault().toLanguageTag()

    @Composable
    actual infix fun provides(value: AppLocale): ProvidedValue<*> {
        val configuration = LocalConfiguration.current

        if (default == null) {
            default = Locale.forLanguageTag(AppLocale.English.toTag())
        }

        val new = when (value) {
            AppLocale.Russian -> Locale.forLanguageTag(value.toTag())
            else -> default!!
        }

        Locale.setDefault(new)
        configuration.setLocale(new)
        val context = LocalContext.current
        return LocalContext.provides(context.createConfigurationContext(configuration))
    }
}