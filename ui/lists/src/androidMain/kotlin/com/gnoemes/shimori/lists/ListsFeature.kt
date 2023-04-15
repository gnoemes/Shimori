package com.gnoemes.shimori.lists

import cafe.adriel.voyager.core.registry.ScreenRegistry
import cafe.adriel.voyager.core.registry.screenModule
import com.gnoemes.shimori.base.core.extensions.new
import com.gnoemes.shimori.common.ui.navigation.Feature
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.lists.page.ListPageScreenModel
import com.gnoemes.shimori.lists.sort.ListSortScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider

actual object ListsFeature : Feature {
    actual override val di: DI.Module = DI.Module("lists-module") {
        bindProvider { new(::ListsScreenModel) }
        bindProvider { new(::ListPageScreenModel) }
        bindProvider { new(::ListSortScreenModel) }
    }
    actual override val screens: ScreenRegistry.() -> Unit = screenModule {
        register<FeatureScreen.Lists> { ListsTab }
    }
}