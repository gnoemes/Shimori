package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ColumnScope.ListItem(
    icon: Painter,
    text: String,
    onClick: () -> Unit,
    description: String? = null,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .clickable { onClick() }
        .padding(start = 4.dp, top = 8.dp, end = 16.dp, bottom = 8.dp)
    ,
) {
    Row(
        modifier = modifier
    ) {
        Icon(
            icon, contentDescription = null,
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
        )

        Spacer(Modifier.width(4.dp))

        Column(
            modifier = Modifier.align(Alignment.CenterVertically),
        ){
            Text(
                text,
                style = MaterialTheme.typography.bodyLarge
            )

            if (description != null) {
                Text(
                    description,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}