package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.theme.dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RootScreenLayout(
    scrollBehavior: TopAppBarScrollBehavior,
    content: @Composable (PaddingValues) -> Unit,
    floatingActionButton: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = { },
    floatingActionButtonPosition: FabPosition = FabPosition.Center
) {
    val navigator = LocalNavigator.currentOrThrow
    val settingsScreen = rememberScreen(FeatureScreen.Settings)

    ScaffoldExtended(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    ShimoriSearchBar(
                        query = "",
                        active = false,
                        onQueryChange = {},
                        onSearch = { },
                        onActiveChange = { },
                        onSettingsClick = { navigator.push(settingsScreen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        content = { },
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.Transparent,
                    scrolledContainerColor = Color.Transparent,
                    navigationIconContentColor = Color.Transparent,
                    titleContentColor = Color.Transparent,
                    actionIconContentColor = Color.Transparent,
                ),
                scrollBehavior = scrollBehavior
            )

            //mock

        }, snackbarHost = snackBarHost, floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        bottomBar = {
            Spacer(
                modifier = Modifier
                    .navigationBarsPadding()
                    .height(MaterialTheme.dimens.bottomBarHeight)
            )
        },
        content = content
    )
}