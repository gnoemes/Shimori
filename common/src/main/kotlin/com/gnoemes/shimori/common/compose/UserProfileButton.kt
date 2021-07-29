package com.gnoemes.shimori.common.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.model.user.UserShort

@Composable
fun UserProfileButton(
    modifier: Modifier = Modifier,
    user: UserShort? = null,
    authorized: Boolean = false,
    avatarClick: () -> Unit = {},
) {
    IconButton(onClick = avatarClick, modifier = modifier) {
        val avatar = user?.image?.preview
        when {
            authorized && avatar != null -> {
                Image(
                        painter = rememberImagePainter(avatar) {
                            crossfade(true)
                        },
                        contentDescription = stringResource(R.string.profile),
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape),
                )
            }
            else -> {
                ShimoriIconButton(
                        onClick = avatarClick,
                        selected = false,
                        painter = painterResource(R.drawable.ic_profile),
                        modifier = Modifier
                            .size(32.dp),
                )
            }
        }
    }
}