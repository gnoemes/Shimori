package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.compose.LocalShimoriTextCreator
import com.gnoemes.shimori.common.ui.resources.Icons
import com.gnoemes.shimori.common.ui.resources.icons.ic_star
import com.gnoemes.shimori.data.ShimoriTitleEntity
import org.jetbrains.compose.resources.painterResource

@Composable
fun TitleSubInfo(
    title: ShimoriTitleEntity,
    modifier: Modifier = Modifier
) {
    val textCreator = LocalShimoriTextCreator.current
    val ratingExists by remember(title.rating) {
        mutableStateOf(title.rating != null && title.rating != 0.0)
    }

    Row(
        modifier = modifier
            .padding(bottom = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (ratingExists) {
            Icon(
                painterResource(Icons.ic_star),
                contentDescription = null,
                modifier = Modifier
                    .size(16.dp),
            )
        }

        Text(
            buildAnnotatedString {
                textCreator.build {
                    if (ratingExists) {
                        append("%.2f".format(title.rating))
                        append(" $divider ")
                    }
                    append(title.status())
                }
            },
            style = MaterialTheme.typography.labelMedium.copy(
                lineHeightStyle = LineHeightStyle(
                    LineHeightStyle.Alignment.Proportional,
                    LineHeightStyle.Trim.Both
                )
            ),
            modifier = Modifier.fillMaxWidth(),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis

        )
    }
}