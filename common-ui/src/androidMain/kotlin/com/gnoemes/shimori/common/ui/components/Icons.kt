package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.LocalShimoriRateUtil
import com.gnoemes.shimori.data.core.entities.rate.ListType
import com.gnoemes.shimori.data.core.entities.rate.RateStatus
import com.gnoemes.shimori.ui.R

@Composable
fun ChevronIcon(
    painter: Painter = painterResource(R.drawable.ic_chevron_right),
) {
    Icon(
        painter = painter,
        contentDescription = null,
    )
}

@Composable
fun RateIcon(
    rateStatus: RateStatus
) {
    Icon(
        painter = painterResource(id = LocalShimoriRateUtil.current.rateStatusIcon(rateStatus)),
        contentDescription = null
    )
}

@Composable
fun BackIcon(
    navigateUp: () -> Unit,
    painter: Painter = painterResource(R.drawable.ic_back)
) {

    IconButton(onClick = navigateUp) {
        Icon(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(24.dp)
        )
    }
}

@Composable
fun ListTypeIcon(
    type: ListType
) {
    Icon(
        painter = painterResource(id = LocalShimoriRateUtil.current.listTypeIcon(type)),
        contentDescription = null
    )
}