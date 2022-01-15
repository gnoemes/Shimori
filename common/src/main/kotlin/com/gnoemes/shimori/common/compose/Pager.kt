//package com.gnoemes.shimori.common.compose
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.snapshotFlow
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.composed
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.unit.Dp
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.lerp
//import com.google.accompanist.pager.ExperimentalPagerApi
//import com.google.accompanist.pager.PagerState
//import kotlinx.coroutines.flow.Flow
//import kotlinx.coroutines.flow.distinctUntilChanged
//import kotlinx.coroutines.flow.filterNot
//import kotlinx.coroutines.flow.map
//import kotlin.math.absoluteValue
//import kotlin.math.max
//
//@Composable
//fun Indicator(
//    modifier: Modifier = Modifier,
//    height: Dp = 4.dp,
//    color: Color = MaterialTheme.colorScheme.secondary
//) {
//
//    Box(
//            modifier = modifier
//                .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
//                .width(16.dp)
//                .height(height)
//                .background(color = color)
//    )
//}
//
//@ExperimentalPagerApi
//fun Modifier.pagerTabIndicatorOffsetFixedSize(
//    pagerState: PagerState,
//    tabPositions: List<TabPosition>,
//    width: Dp = 16.dp,
//): Modifier = composed {
//    // If there are no pages, nothing to show
//    if (pagerState.pageCount == 0) return@composed this
//
//    val targetIndicatorOffset: Dp
//    val indicatorWidth: Dp
//
//    val currentTab = tabPositions[pagerState.currentPage]
//    val targetPage = pagerState.targetPage
//    val targetTab = targetPage?.let { tabPositions.getOrNull(it) }
//
//    val currentTabCenter = currentTab.left + currentTab.width / 2 - width / 2
//
//    if (targetTab != null) {
//        // The distance between the target and current page. If the pager is animating over many
//        // items this could be > 1
//        val targetDistance = (targetPage - pagerState.currentPage).absoluteValue
//        // Our normalized fraction over the target distance
//        val fraction = (pagerState.currentPageOffset / max(targetDistance, 1)).absoluteValue
//
//        val targetTabCenter = targetTab.left + targetTab.width / 2 - width / 2
//
//        targetIndicatorOffset = lerp(currentTabCenter, targetTabCenter, fraction)
//        indicatorWidth = width
//    } else {
//        // Otherwise we just use the current tab/page
//        targetIndicatorOffset = currentTabCenter
//        indicatorWidth = width
//    }
//
//    fillMaxWidth()
//        .wrapContentSize(Alignment.BottomStart)
//        .offset(x = targetIndicatorOffset)
//        .width(indicatorWidth)
//}
//
//@ExperimentalPagerApi
//inline val PagerState.pageChanges: Flow<Int>
//    get() = snapshotFlow { isScrollInProgress }
//        // Only emit when the scroll has finished
//        .filterNot { it }
//        .map { currentPage }
//        .distinctUntilChanged()