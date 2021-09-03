package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.theme.*

@Composable
fun ShimoriButton(
    modifier: Modifier,
    selected: Boolean,
    onClick: () -> Unit,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.subInfoStyle,
    painter: Painter? = null,
    iconSize: Dp = 24.dp
) {
    ShimoriButton(
            onClick = onClick,
            modifier = modifier,
            painter = painter,
            text = text,
            textStyle = textStyle,
            iconSize = iconSize,
            contentColor = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary,
            buttonColors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selected) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.button
            )
    )
}

@Composable
fun ShimoriButton(
    onClick: () -> Unit,
    modifier: Modifier,
    text: String,
    contentColor: Color,
    buttonColors: ButtonColors,
    textStyle: TextStyle = MaterialTheme.typography.subInfoStyle,
    painter: Painter? = null,
    iconSize: Dp = 24.dp
) {

    val contentPadding =
        if (painter == null) PaddingValues(horizontal = 12.dp, vertical = 8.dp)
        else PaddingValues(start = 12.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)

    Button(
            onClick = onClick,
            elevation = null,
            shape = RoundedCornerShape(32.dp),
            border = BorderStroke(1.dp, MaterialTheme.colors.alpha),
            colors = buttonColors,
            modifier = modifier,
            contentPadding = contentPadding
    ) {

        if (painter != null) {
            Icon(
                    modifier = Modifier.size(iconSize),
                    painter = painter,
                    contentDescription = text,
                    tint = contentColor,
            )
            Spacer(modifier = Modifier.width(8.dp))
        }

        Text(
                text = text,
                style = textStyle,
                color = contentColor
        )
    }
}

@Composable
fun EnlargedButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    painter: Painter,
    text: String,
    textStyle: TextStyle = MaterialTheme.typography.keyInfoStyle,
    content: @Composable RowScope.() -> Unit = {}
) {
    EnlargedButton(
            onClick = onClick,
            modifier = modifier,
            painter = painter,
            text = text,
            textStyle = textStyle,
            contentColor = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary,
            buttonColors = ButtonDefaults.textButtonColors(
                    backgroundColor = if (selected) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.button,
                    disabledContentColor = MaterialTheme.colors.disabled
            ),
            content
    )
}

@Composable
fun EnlargedButton(
    onClick: () -> Unit,
    modifier: Modifier,
    painter: Painter,
    text: String,
    textStyle: TextStyle,
    contentColor: Color,
    buttonColors: ButtonColors,
    content: @Composable RowScope.() -> Unit
) {

    TextButton(
            colors = buttonColors,
            shape = RoundedCornerShape(12.dp),
            onClick = onClick,
            border = BorderStroke(1.dp, MaterialTheme.colors.alpha),
            modifier = modifier,
            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
    ) {
        Icon(
                painter = painter,
                contentDescription = text,
                tint = contentColor,
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
                text = text,
                style = textStyle,
                color = contentColor,
                modifier = Modifier.weight(1f),
                overflow = TextOverflow.Ellipsis,
                maxLines = 1
        )

        content()
    }
}

@Composable
fun ShimoriIconButton(
    onClick: () -> Unit,
    selected: Boolean,
    painter: Painter,
    modifier: Modifier,
    contentDescription: String? = null,
    enabled: Boolean = true,
) {
    Button(onClick = onClick,
            modifier = modifier.border(1.dp, MaterialTheme.colors.alpha, shape = CircleShape),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selected) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.button
            ),
            elevation = null,
            contentPadding = PaddingValues(8.dp),
            enabled = enabled
    ) {
        Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun ShimoriConfirmationButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    type: ConfirmationButtonType = ConfirmationButtonType.Primary,
    enabled: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.keyInfoStyle,
) {

    val truePrimary = type == ConfirmationButtonType.Primary && enabled

    val buttonModifier = modifier then
            if (!truePrimary) Modifier.border(1.dp, MaterialTheme.colors.alpha, shape = CircleShape)
            else Modifier

    Button(
            onClick = onClick,
            modifier = buttonModifier,
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (truePrimary) MaterialTheme.colors.secondary else MaterialTheme.colors.button
            ),
            elevation = null,
            contentPadding = PaddingValues(16.dp),
            enabled = enabled
    ) {
        Text(
                text = text,
                style = textStyle,
                color = when {
                    truePrimary -> MaterialTheme.colors.onSecondary
                    enabled -> MaterialTheme.colors.onPrimary
                    else -> MaterialTheme.colors.disabled
                }
        )
    }
}


@JvmInline
value class ConfirmationButtonType private constructor(val type: Int) {
    companion object {
        val Primary = ConfirmationButtonType(0)
        val Secondary = ConfirmationButtonType(1)
    }
}

@Preview
@Composable
fun PreviewShimoriButtonDark() {
    ShimoriTheme(useDarkColors = true) {
        ShimoriButton(
                selected = false,
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(32.dp)
                    .wrapContentWidth(),
                text = "Name"
        )
    }
}

@Preview
@Composable
fun PreviewShimoriButtonDarkSelected() {
    ShimoriTheme(useDarkColors = true) {
        ShimoriButton(
                selected = true,
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(32.dp)
                    .wrapContentWidth(),
                text = "Name"
        )
    }
}

@Preview
@Composable
fun PreviewShimoriButtonDarkWithIconSelected() {
    ShimoriTheme(useDarkColors = true) {
        ShimoriButton(
                selected = true,
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(32.dp)
                    .wrapContentWidth(),
                text = "Name",
                painter = painterResource(id = R.drawable.ic_arrow_down)
        )
    }
}

@Preview
@Composable
fun PreviewEnlargedDark() {
    ShimoriTheme(useDarkColors = true) {
        EnlargedButton(
                selected = false,
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(48.dp)
                    .width(156.dp),
                painter = painterResource(R.drawable.ic_anime),
                text = "Anime",
                textStyle = MaterialTheme.typography.subInfoStyle
        )
    }
}

@Preview
@Composable
fun PreviewEnlargedDarkSelected() {
    ShimoriTheme(useDarkColors = true) {
        EnlargedButton(
                selected = true,
                onClick = {},
                modifier = Modifier
                    .padding(start = 16.dp, top = 12.dp, end = 12.dp, bottom = 12.dp)
                    .height(48.dp)
                    .width(156.dp),
                painter = painterResource(R.drawable.ic_anime),
                text = "Anime",
                textStyle = MaterialTheme.typography.subInfoStyle
        )
    }
}

@Preview
@Composable
fun PreviewShimoriIconButtonDark() {
    ShimoriTheme(useDarkColors = true) {
        ShimoriIconButton(onClick = { },
                selected = false,
                painter = painterResource(id = R.drawable.ic_explore),
                modifier = Modifier
                    .padding(12.dp)
                    .size(32.dp),
                contentDescription = null
        )
    }
}

@Preview
@Composable
fun PreviewShimoriConfirmationButton() {
    ShimoriTheme(useDarkColors = true) {
        ShimoriConfirmationButton(
                onClick = { },
                text = "Accept",
                modifier = Modifier
                    .width(158.dp)
                    .height(IntrinsicSize.Min)
        )
    }
}