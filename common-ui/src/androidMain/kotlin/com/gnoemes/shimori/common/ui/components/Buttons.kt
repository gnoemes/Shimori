@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.theme.ShimoriBigRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriBiggestRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriDefaultRoundedCornerShape
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape

@Composable
fun EnlargedButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String? = null,
    enabled: Boolean = true,
    selected: Boolean = false,
    buttonColors: ButtonColors = ShimoriButtonDefaults.buttonColors(selected),
    leftIcon: (@Composable RowScope.() -> Unit)? = null,
    rightIcon: (@Composable RowScope.() -> Unit)? = null,
) {
    TextButton(
        colors = buttonColors,
        shape = ShimoriDefaultRoundedCornerShape,
        onClick = onClick,
        enabled = enabled,
        modifier = modifier,
        contentPadding = if (text.isNullOrEmpty()) PaddingValues(12.dp)
        else PaddingValues(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp),
    ) {
        if (leftIcon != null) {
            leftIcon()
            Spacer(modifier = Modifier.width(12.dp))
        }

        if (!text.isNullOrEmpty()) {
            Text(
                text = text,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
            )

        }

        rightIcon?.invoke(this)
    }
}

@Composable
fun ShimoriChip(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    selected: Boolean = false,
    buttonColors: ButtonColors = ShimoriButtonDefaults.buttonColors(
        containerColor = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurface,
        disabledContainerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f)
    ),
    icon: (@Composable RowScope.() -> Unit)? = null
) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ShimoriSmallRoundedCornerShape,
        colors = buttonColors,
        border =
        if (selected) null
        else
            BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.12f)
            ),
        contentPadding =
        if (icon != null) PaddingValues(start = 12.dp, top = 6.dp, end = 16.dp, bottom = 6.dp)
        else PaddingValues(vertical = 6.dp, horizontal = 12.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            maxLines = 1
        )
    }
}

@Composable
fun ShimoriButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    enabled: Boolean = true,
    selected: Boolean = false,
    buttonColors: ButtonColors = ShimoriButtonDefaults.buttonColors(selected),
    icon: (@Composable RowScope.() -> Unit)? = null
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors = buttonColors,
        shape = ShimoriBigRoundedCornerShape,
        contentPadding =
        if (icon != null) PaddingValues(start = 12.dp, top = 6.dp, end = 16.dp, bottom = 6.dp)
        else PaddingValues(vertical = 6.dp, horizontal = 12.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
            text = text,
            maxLines = 1
        )
    }
}

@Composable
fun ShimoriConformationButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    type: ConfirmationButtonType,
    enabled: Boolean = true
) {
    val containerColor =
        if (type == ConfirmationButtonType.Primary) MaterialTheme.colorScheme.primary
        else MaterialTheme.colorScheme.surfaceVariant
    val contentColor =
        if (type == ConfirmationButtonType.Primary) MaterialTheme.colorScheme.onPrimary
        else MaterialTheme.colorScheme.onSurfaceVariant
    val buttonColors = ButtonDefaults.buttonColors(
        containerColor = containerColor,
        contentColor = contentColor,
        disabledContainerColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f),
        disabledContentColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f),
    )

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ShimoriBigRoundedCornerShape,
        colors = buttonColors,
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(
            text = text,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ShimoriCircleButton(
    onClick: () -> Unit,
    modifier: Modifier,
    icon: @Composable () -> Unit,
    enabled: Boolean = true,
    selected: Boolean = false,
    buttonColors: ButtonColors = ShimoriButtonDefaults.buttonColors(selected = selected),
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = ShimoriBigRoundedCornerShape,
        colors = buttonColors,
        contentPadding = PaddingValues(8.dp),
        content = { icon() }
    )
}

@Composable
fun ShimoriFAB(
    onClick: () -> Unit,
    expanded: Boolean,
    icon: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    text: String? = null
) {
    ExtendedFloatingActionButton(
        onClick = onClick,
        expanded = expanded,
        shape = ShimoriBiggestRoundedCornerShape,
        icon = icon,
        modifier = modifier,
        text = {
            if (!text.isNullOrBlank()) {
                CompositionLocalProvider(
                    LocalTextStyle provides MaterialTheme.typography.labelLarge,
                ) {
                    Text(text = text)
                }
            }
        },
    )
}

@JvmInline
value class ConfirmationButtonType private constructor(val type: Int) {
    companion object {
        val Primary = ConfirmationButtonType(0)
        val Secondary = ConfirmationButtonType(1)
    }
}

@Composable
private fun Button(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    shape: Shape = ButtonDefaults.shape,
    border: BorderStroke? = null,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    content: @Composable RowScope.() -> Unit
) {
    androidx.compose.material3.Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content
    )
}

object ShimoriButtonDefaults {
    @Composable
    fun buttonColors(
        selected: Boolean = false,
        containerColor: Color = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.surfaceVariant,
        contentColor: Color = if (selected) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onSurfaceVariant,
        disabledContainerColor: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.12f),
        disabledContentColor: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.16f),
    ): ButtonColors =
        ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor,
            disabledContainerColor = disabledContainerColor,
            disabledContentColor = disabledContentColor
        )
}

//
//@Preview
//@Composable
//fun PreviewEnlargedDark() {
//    ShimoriThemePreview(useDarkColors = true) {
//        EnlargedButton(
//            onClick = {},
//            modifier = Modifier
//                .height(48.dp)
//                .width(156.dp),
//            text = "Anime",
//            leftIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_anime),
//                    contentDescription = stringResource(id = R.string.anime)
//                )
//            },
//            rightIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_chevron_right),
//                    contentDescription = null
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewEnlargedDarkSelected() {
//    ShimoriThemePreview(useDarkColors = true) {
//        EnlargedButton(
//            onClick = {},
//            modifier = Modifier
//                .height(48.dp)
//                .width(156.dp),
//            text = "Anime",
//            selected = true,
//            leftIcon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_anime),
//                    contentDescription = stringResource(id = R.string.anime)
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriChipDark() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriChip(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(95.dp)
//                .height(32.dp),
//            text = "Name",
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_down),
//                    contentDescription = null
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriChipSelected() {
//    ShimoriThemePreview(useDarkColors = false) {
//        ShimoriChip(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(95.dp)
//                .height(32.dp),
//            text = "Name",
//            selected = true,
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_down),
//                    contentDescription = null
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriButtonDark() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(95.dp)
//                .height(32.dp),
//            text = "Name",
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_down),
//                    contentDescription = null
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriButtonSelected() {
//    ShimoriThemePreview(useDarkColors = false) {
//        ShimoriButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(95.dp)
//                .height(32.dp),
//            text = "Name",
//            selected = true,
//            icon = {
//                Icon(
//                    painter = painterResource(id = R.drawable.ic_arrow_down),
//                    contentDescription = null
//                )
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriConfirmationButtonDarkPrimary() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriConformationButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(158.dp)
//                .height(52.dp),
//            text = "Accept",
//            type = ConfirmationButtonType.Primary
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriConfirmationButtonDarkSecondary() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriConformationButton(
//            onClick = { /*TODO*/ },
//            modifier = Modifier
//                .width(158.dp)
//                .height(52.dp),
//            text = "Cancel",
//            type = ConfirmationButtonType.Secondary
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriShimoriFABExpandedDark() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriFAB(
//            onClick = { /*TODO*/ },
//            expanded = true,
//            text = "Lists",
//            modifier = Modifier.height(40.dp),
//            icon = {
//                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = null)
//            }
//        )
//    }
//}
//
//@Preview
//@Composable
//fun PreviewShimoriShimoriFABDark() {
//    ShimoriThemePreview(useDarkColors = true) {
//        ShimoriFAB(
//            onClick = { /*TODO*/ },
//            expanded = false,
//            text = "Lists",
//            icon = {
//                Icon(painter = painterResource(id = R.drawable.ic_menu), contentDescription = null)
//            }
//        )
//    }