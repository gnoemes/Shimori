package com.gnoemes.shimori.home

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.with
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import cafe.adriel.voyager.core.stack.StackEvent
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.tab.LocalTabNavigator
import cafe.adriel.voyager.navigator.tab.TabNavigator
import cafe.adriel.voyager.transitions.ScreenTransition
import coil.compose.AsyncImage
import com.gnoemes.shimori.common.ui.components.ShimoriNavigationBarItem
import com.gnoemes.shimori.common.ui.empty
import com.gnoemes.shimori.common.ui.navigation.BottomControlsScreen
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
import soup.compose.material.motion.animation.materialSharedAxisZ

@OptIn(
    ExperimentalAnimationApi::class
)
internal object HomeScreen : Screen() {

    private const val TabFadeDuration = 200

    private val tabs = listOf(
        tab(FeatureScreen.Lists),
        MockTab,
        tab(FeatureScreen.Auth)
    )

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<HomeScreenModel>()

        val state by screenModel.state.collectAsState()

        var inTabNavigator by remember {
            mutableStateOf<Navigator?>(null)
        }

        TabNavigator(
            tab = tabs[0]
        ) { tabNavigator ->
            Scaffold(
                bottomBar = { BottomBar(inTabNavigator, state.profileImage) },
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
                                Navigator(screen = it) { navigator ->
                                    inTabNavigator = navigator
                                    DefaultNavigatorScreenTransition(navigator)
                                }
                            }
                        },
                        label = "Tab animation",
                    )
                }
            }
        }
    }

    @Composable
    private fun BottomBar(
        inTabNavigator: Navigator?,
        profileImage: ShimoriImage?
    ) {
        Column {
            NavigationBar(inTabNavigator, profileImage)
            Spacer(modifier = Modifier.navigationBarsPadding())
        }
    }

    @Composable
    private fun NavigationBar(
        inTabNavigator: Navigator?,
        profileImage: ShimoriImage?
    ) {
        val isBottomNavVisible =
            inTabNavigator == null || inTabNavigator.lastItem !is BottomControlsScreen
        AnimatedVisibility(
            visible = isBottomNavVisible,
            enter = slideInVertically { it },
            exit = slideOutVertically { it },
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
                        NavigationBarItem(inTabNavigator, tab, profileImage)
                    }
                }
            }
        }
    }

    @Composable
    fun RowScope.NavigationBarItem(
        inTabNavigator: Navigator?,
        tab: Tab,
        profileImage: ShimoriImage?
    ) {
        val tabNavigator = LocalTabNavigator.current
        val scope = rememberCoroutineScope()
        val selected = tabNavigator.current::class == tab::class

        ShimoriNavigationBarItem(
            selected = selected,
            icon = {
                if (profileImage != null && tab == tabs[2]) {
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
                else inTabNavigator?.let {
                    scope.launch { tab.onReselect(it) }
                }
            },
        )
    }

    @Composable
    fun DefaultNavigatorScreenTransition(navigator: Navigator) {
        ScreenTransition(
            navigator = navigator,
            transition = {
                materialSharedAxisZ(
                    forward = navigator.lastEvent != StackEvent.Pop,
                )
            },
        )
    }
}