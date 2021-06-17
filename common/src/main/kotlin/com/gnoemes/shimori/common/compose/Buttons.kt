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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.theme.alpha
import com.gnoemes.shimori.common.compose.theme.button
import com.gnoemes.shimori.common.compose.theme.keyInfoStyle

@Composable
fun EnlargedButton(
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier,
    painter: Painter,
    text: String,
    textStyle : TextStyle = MaterialTheme.typography.keyInfoStyle,
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
                    backgroundColor = if (selected) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.button
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
    textStyle : TextStyle,
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
                modifier = Modifier.weight(1f)
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
    contentDescription : String? = null
) {
    Button(onClick = onClick,
            modifier = modifier.border(1.dp, MaterialTheme.colors.alpha, shape = CircleShape),
            shape = RoundedCornerShape(32.dp),
            colors = ButtonDefaults.buttonColors(
                    backgroundColor = if (selected) MaterialTheme.colors.secondaryVariant else MaterialTheme.colors.button
            ),
            elevation = null,
            contentPadding = PaddingValues(8.dp)
    ) {
        Icon(
                painter = painter,
                contentDescription = contentDescription,
                tint = if (selected) MaterialTheme.colors.secondary else MaterialTheme.colors.onPrimary,
                modifier = Modifier.size(16.dp)
        )
    }
}