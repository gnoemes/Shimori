package com.gnoemes.shimori.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEach
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.bottomSheet.BottomSheetNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import coil.compose.AsyncImage
import com.gnoemes.shimori.common.ui.components.ShimoriNavigationBarItem
import com.gnoemes.shimori.common.ui.empty
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.MockTab
import com.gnoemes.shimori.common.ui.navigation.Screen
import com.gnoemes.shimori.common.ui.navigation.Tab
import com.gnoemes.shimori.common.ui.theme.ShimoriBiggestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.dimens
import com.gnoemes.shimori.data.core.entities.common.ShimoriImage
import kotlinx.coroutines.launch
import soup.compose.material.motion.animation.materialFadeThroughIn
import soup.compose.material.motion.animation.materialFadeThroughOut

@OptIn(
    ExperimentalMaterialApi::class, ExperimentalLayoutApi::class,
    ExperimentalAnimationApi::class
)
internal object HomeScreen : Screen() {

    private const val TabFadeDuration = 200

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val screenModel = rememberScreenModel<HomeScreenModel>()

        val state by screenModel.state.collectAsState()

        val listsTab = rememberScreen(FeatureScreen.Lists) as Tab
        val mockTab1 = MockTab
        val mockTab2 = MockTab

        val tabs = listOf(listsTab, mockTab1, mockTab2)

        BottomSheetNavigator(
            scrimColor = MaterialTheme.colorScheme.scrim.copy(alpha = .32f),
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetContentColor = contentColorFor(backgroundColor = MaterialTheme.colorScheme.surface),
            sheetBackgroundColor = MaterialTheme.colorScheme.surface,
        ) { bottomSheetNavigator ->
            TabNavigator(
                tab = listsTab
            ) { tabNavigator ->
                CompositionLocalProvider(LocalNavigator provides navigator) {
                    Scaffold(
                        bottomBar = { BottomBar(tabs, state.profileImage) },
                        contentWindowInsets = WindowInsets.empty
                    ) { contentPadding ->
                        Box(
                            modifier = Modifier
                        ) {
                            AnimatedContent(
                                targetState = tabNavigator.current,
                                transitionSpec = {
                                    materialFadeThroughIn(
                                        initialScale = 1f,
                                        durationMillis = TabFadeDuration
                                    ) with materialFadeThroughOut(durationMillis = TabFadeDuration)
                                },
                                content = {
                                    tabNavigator.saveableState(key = "currentTab", it) {
                                        it.Content()
                                    }
                                },
                                label = "Tab animation",
                            )
                        }
                    }
                }
            }
        }
    }

    @Composable
    private fun BottomBar(
        tabs: List<Tab>,
        profileImage: ShimoriImage?
    ) {
        Column {
            NavigationBar(tabs, profileImage)
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    @Composable
    private fun NavigationBar(
        tabs: List<Tab>,
        profileImage: ShimoriImage?
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(MaterialTheme.dimens.bottomBarContainerHeight)
                .background(
                    brush = Brush.verticalGradient(
                        colorStops = arrayOf(
                            0f to MaterialTheme.colorScheme.background.copy(alpha = 0f),
                            0.52f to MaterialTheme.colorScheme.background.copy(alpha = 0.60f),
                            1f to MaterialTheme.colorScheme.background,
                        ),
                        tileMode = TileMode.Clamp
                    )
                )
        ) {
            NavigationBar(
                tonalElevation = 0.dp,
                containerColor = Color.Transparent,
                modifier = Modifier
                    .height(MaterialTheme.dimens.bottomBarHeight)
                    .align(Alignment.BottomStart),
                windowInsets = WindowInsets.empty,
            ) {
                tabs.fastForEach { tab ->
                    val tabNavigator = LocalTabNavigator.current
                    val navigator = LocalNavigator.currentOrThrow
                    val scope = rememberCoroutineScope()

                    val selected = tabNavigator.current::class == tab::class
                    ShimoriNavigationBarItem(
                        selected = selected,
                        icon = {
                            if (profileImage != null
                                //TODO change to profile tab
                                   && tab == MockTab
                            ) {
                                AsyncImage(
                                    model = profileImage,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(ShimoriBiggestRoundedCornerShape),
                                    contentDescription = tab.options.title
                                )
                            } else if (tab.options.icon != null) {
                                Icon(
                                    painter = tab.options.icon!!,
                                    contentDescription = tab.options.title
                                )
                            }
                        },
                        label = {
                            Text(
                                text = tab.options.title,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        },
                        onClick = {
                            if (!selected) tabNavigator.current = tab
                            else scope.launch { tab.onReselect(navigator) }
                        },
                    )
                }
            }
        }
    }
}