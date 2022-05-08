package com.gnoemes.shimori.settings

import com.gnoemes.shimori.base.core.di.KodeinTag
import com.gnoemes.shimori.common.ui.utils.bindViewModel
import org.kodein.di.DI
import org.kodein.di.instance

actual val settingsModule = DI.Module("settings-module") {
    bindViewModel { SettingsViewModel(instance(tag = KodeinTag.appName), instance()) }
}