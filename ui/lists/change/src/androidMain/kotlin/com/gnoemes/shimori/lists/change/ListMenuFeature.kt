package com.gnoemes.shimori.lists.change

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.lists.change.section.ListMenuStatusSectionScreenModel
import org.kodein.di.DI
import org.kodein.di.DirectDI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance

actual object ListMenuFeature : Feature {
    private fun DirectDI.sectionScreenModel(type: Int) = ListMenuStatusSectionScreenModel(
        type,
        instance(),
        instance(),
        instance()
    )

    actual override val di: DI.Module = DI.Module("lists-change-module") {
        bindProvider { new(::ListMenuScreenModel) }
        bindFactory(tag = "section-0") { type: Int -> sectionScreenModel(type) }
        bindFactory(tag = "section-1") { type: Int -> sectionScreenModel(type) }
        bindFactory(tag = "section-2") { type: Int -> sectionScreenModel(type) }
        bindFactory(tag = "section-3") { type: Int -> sectionScreenModel(type) }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.ListMenu> {
            ListMenuScreen()
        }
    }
}