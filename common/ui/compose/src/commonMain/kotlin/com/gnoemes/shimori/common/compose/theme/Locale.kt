package com.gnoemes.shimori.common.compose.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidedValue
import com.gnoemes.shimori.settings.AppLocale

expect object LocalAppLocale {
    val current: String @Composable get
    @Composable
    infix fun provides(value: AppLocale): ProvidedValue<*>
}