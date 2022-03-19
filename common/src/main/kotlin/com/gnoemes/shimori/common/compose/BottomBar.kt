package com.gnoemes.shimori.common.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.*
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
fun RowScope.ShimoriBottomBarItem(
    onClick: () -> Unit,
    selected: Boolean,
    icon: @Composable () -> Unit,
    label: (@Composable () -> Unit)?,
    colors: BottomBarItemColors = BottomBarItemDefaults.colors(),
    enabled: Boolean = true,
    alwaysShowLabel: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    modifier: Modifier = Modifier
) {
    val styledIcon = @Composable {
        val iconColor by colors.iconColor(selected = selected)
        CompositionLocalProvider(LocalContentColor provides iconColor, content = icon)
    }

    val styledLabel: @Composable (() -> Unit)? = label?.let {
        @Composable {
            val style = MaterialTheme.typography.labelMedium
            val textColor by colors.textColor(selected = selected)
            CompositionLocalProvider(LocalContentColor provides textColor) {
                ProvideTextStyle(style, content = label)
            }
        }
    }

    Box(
        modifier
            .selectable(
                selected = selected,
                onClick = onClick,
                enabled = enabled,
                role = Role.Tab,
                interactionSource = interactionSource,
                indication = rememberRipple(),
            )
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        val animationProgress: Float by animateFloatAsState(
            targetValue = if (selected) 1f else 0f,
            animationSpec = tween(ItemAnimationDurationMillis)
        )

        BottomBarItemBaselineLayout(
            icon = styledIcon,
            label = styledLabel,
            alwaysShowLabel = alwaysShowLabel,
            animationProgress = animationProgress
        )
    }
}

/**
 * Base layout for a [BottomBarItem].
 *
 * @param icon icon for this item
 * @param label text label for this item
 * @param alwaysShowLabel whether to always show the label for this item. If false, the label will
 * only be shown when this item is selected.
 * @param animationProgress progress of the animation, where 0 represents the unselected state of
 * this item and 1 represents the selected state. This value controls other values such as indicator
 * size, icon and label positions, etc.
 */
@Composable
private fun BottomBarItemBaselineLayout(
    icon: @Composable () -> Unit,
    label: @Composable (() -> Unit)?,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
) {
    Layout({
        Box(Modifier.layoutId(IconLayoutIdTag)) { icon() }

        if (label != null) {
            Box(
                Modifier
                    .layoutId(LabelLayoutIdTag)
                    .alpha(if (alwaysShowLabel) 1f else animationProgress)
                    .padding(horizontal = BottomBarItemHorizontalPadding)
            ) { label() }
        }
    }) { measurables, constraints ->
        val iconPlaceable =
            measurables.first { it.layoutId == IconLayoutIdTag }.measure(constraints)

        val labelPlaceable =
            label?.let {
                measurables
                    .first { it.layoutId == LabelLayoutIdTag }
                    .measure(
                        // Measure with loose constraints for height as we don't want the label to take up more
                        // space than it needs
                        constraints.copy(minHeight = 0)
                    )
            }

        if (label == null) {
            placeIcon(iconPlaceable, constraints)
        } else {
            placeLabelAndIcon(
                labelPlaceable!!,
                iconPlaceable,
                constraints,
                alwaysShowLabel,
                animationProgress
            )
        }
    }
}

/**
 * Places the provided [iconPlaceable], and possibly [indicatorPlaceable] if it exists, in the
 * center of the provided [constraints].
 */
private fun MeasureScope.placeIcon(
    iconPlaceable: Placeable,
    constraints: Constraints
): MeasureResult {
    val width = constraints.maxWidth
    val height = constraints.maxHeight

    val iconX = (width - iconPlaceable.width) / 2
    val iconY = (height - iconPlaceable.height) / 2

    return layout(width, height) {
        iconPlaceable.placeRelative(iconX, iconY)
    }
}

/**
 * Places the provided [labelPlaceable], [iconPlaceable], and [indicatorPlaceable] in the correct
 * position, depending on [alwaysShowLabel] and [animationProgress].
 *
 * When [alwaysShowLabel] is true, the positions do not move. The [iconPlaceable] will be placed
 * near the top of the item and the [labelPlaceable] will be placed near the bottom, according to
 * the spec.
 *
 * When [animationProgress] is 1 (representing the selected state), the positions will be the same
 * as above.
 *
 * Otherwise, when [animationProgress] is 0, [iconPlaceable] will be placed in the center, like in
 * [placeIcon], and [labelPlaceable] will not be shown.
 *
 * When [animationProgress] is animating between these values, [iconPlaceable] and [labelPlaceable]
 * will be placed at a corresponding interpolated position.
 *
 * [indicatorPlaceable] will always be placed in such a way that it shares the same center as
 * [iconPlaceable].
 *
 * @param labelPlaceable text label placeable inside this item
 * @param iconPlaceable icon placeable inside this item
 * @param indicatorPlaceable indicator placeable inside this item, if it exists
 * @param constraints constraints of the item
 * @param alwaysShowLabel whether to always show the label for this item. If true, icon and label
 * positions will not change. If false, positions transition between 'centered icon with no label'
 * and 'top aligned icon with label'.
 * @param animationProgress progress of the animation, where 0 represents the unselected state of
 * this item and 1 represents the selected state. Values between 0 and 1 interpolate positions of
 * the icon and label.
 */
private fun MeasureScope.placeLabelAndIcon(
    labelPlaceable: Placeable,
    iconPlaceable: Placeable,
    constraints: Constraints,
    alwaysShowLabel: Boolean,
    animationProgress: Float,
): MeasureResult {
    val height = constraints.maxHeight

    val baseline = labelPlaceable[LastBaseline]
    // Label should be `ItemVerticalPadding` from the bottom
    val labelY = height - baseline - BottomBarItemVerticalPadding.roundToPx()

    // Icon (when selected) should be `ItemVerticalPadding` from the top
    val selectedIconY = BottomBarItemVerticalPadding.roundToPx()
    val unselectedIconY =
        if (alwaysShowLabel) selectedIconY else (height - iconPlaceable.height) / 2

    // How far the icon needs to move between unselected and selected states.
    val iconDistance = unselectedIconY - selectedIconY

    // The interpolated fraction of iconDistance that all placeables need to move based on
    // animationProgress.
    val offset = (iconDistance * (1 - animationProgress)).roundToInt()

    val containerWidth = constraints.maxWidth

    val labelX = (containerWidth - labelPlaceable.width) / 2
    val iconX = (containerWidth - iconPlaceable.width) / 2

    return layout(containerWidth, height) {
        if (alwaysShowLabel || animationProgress != 0f) {
            labelPlaceable.placeRelative(labelX, labelY + offset)
        }
        iconPlaceable.placeRelative(iconX, selectedIconY + offset)
    }
}

private const val IconLayoutIdTag: String = "icon"

private const val LabelLayoutIdTag: String = "label"

private val BottomBarItemHorizontalPadding: Dp = 4.dp
/*@VisibleForTesting*/
internal val BottomBarItemVerticalPadding: Dp = 16.dp

private const val ItemAnimationDurationMillis = 100


/** Represents the colors of the various elements of a navigation item. */
interface BottomBarItemColors {
    /**
     * Represents the icon color for this item, depending on whether it is [selected].
     *
     * @param selected whether the item is selected
     */
    @Composable
    fun iconColor(selected: Boolean): State<Color>

    /**
     * Represents the text color for this item, depending on whether it is [selected].
     *
     * @param selected whether the item is selected
     */
    @Composable
    fun textColor(selected: Boolean): State<Color>
}

object BottomBarItemDefaults {
    /**
     * Creates a [BottomBarItemColors] with the provided colors according to the Material
     * specification.
     *
     * @param selectedIconColor the color to use for the icon when the item is selected.
     * @param unselectedIconColor the color to use for the icon when the item is unselected.
     * @param selectedTextColor the color to use for the text label when the item is selected.
     * @param unselectedTextColor the color to use for the text label when the item is unselected.
     * @return the resulting [BottomBarItemColors] used for [ShimoriBottomBarItem]
     */
    @Composable
    fun colors(
        selectedIconColor: Color = MaterialTheme.colorScheme.primary,
        unselectedIconColor: Color = MaterialTheme.colorScheme.onSurface,
        selectedTextColor: Color = MaterialTheme.colorScheme.primary,
        unselectedTextColor: Color = MaterialTheme.colorScheme.onSurface,
    ): BottomBarItemColors = remember(
        selectedIconColor,
        unselectedIconColor,
        selectedTextColor,
        unselectedTextColor,
    ) {
        DefaultBottomBarItemColors(
            selectedIconColor = selectedIconColor,
            unselectedIconColor = unselectedIconColor,
            selectedTextColor = selectedTextColor,
            unselectedTextColor = unselectedTextColor,
        )
    }
}

private class DefaultBottomBarItemColors(
    private val selectedIconColor: Color,
    private val unselectedIconColor: Color,
    private val selectedTextColor: Color,
    private val unselectedTextColor: Color,
) : BottomBarItemColors {

    @Composable
    override fun iconColor(selected: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (selected) selectedIconColor else unselectedIconColor,
            animationSpec = tween(ItemAnimationDurationMillis)
        )
    }

    @Composable
    override fun textColor(selected: Boolean): State<Color> {
        return animateColorAsState(
            targetValue = if (selected) selectedTextColor else unselectedTextColor,
            animationSpec = tween(ItemAnimationDurationMillis)
        )
    }
}