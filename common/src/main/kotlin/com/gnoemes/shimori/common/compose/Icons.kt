package com.gnoemes.shimori.common.compose

import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.rate.RateStatus

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