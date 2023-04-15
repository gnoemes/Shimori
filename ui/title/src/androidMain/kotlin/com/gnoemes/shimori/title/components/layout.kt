package com.gnoemes.shimori.title.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.components.ChevronIcon
import com.gnoemes.shimori.common.ui.ignoreHorizontalParentPadding
import com.gnoemes.shimori.common.ui.noRippleClickable

@Composable
internal fun RowContentSection(
    title: String,
    isMoreVisible: Boolean,
    sectionLoaded: Boolean,
    onClickMore: () -> Unit,
    nonRowContent: @Composable ColumnScope.() -> Unit = { },
    contentHorizontalArrangement: Dp = 12.dp,
    content: @Composable RowScope.() -> Unit
) {
    Column {
        CompositionLocalProvider(
            LocalContentColor provides MaterialTheme.colorScheme.onBackground
        ) {
            if (sectionLoaded) {
                Row(
                    modifier = Modifier.noRippleClickable { onClickMore() }
                ) {
                    CompositionLocalProvider(
                        LocalTextStyle provides MaterialTheme.typography.titleMedium
                    ) {
                        Text(
                            text = title,
                            modifier = Modifier
                                .weight(1f)
                        )
                    }

                    if (isMoreVisible) {
                        Spacer(modifier = Modifier.width(12.dp))
                        IconButton(
                            onClick = onClickMore, modifier = Modifier.size(24.dp)
                        ) {
                            ChevronIcon()
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            val screenPadding = 16.dp

            Row(
                modifier = Modifier
                    .ignoreHorizontalParentPadding(screenPadding)
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(contentHorizontalArrangement)
            ) {
                Spacer(modifier = Modifier.width(screenPadding - contentHorizontalArrangement))
                content()
                Spacer(modifier = Modifier.width(screenPadding - contentHorizontalArrangement))
            }

            nonRowContent()
        }
    }
}