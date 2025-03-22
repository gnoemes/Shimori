package com.gnoemes.shimori.common.compose.ui

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.gnoemes.shimori.data.common.ShimoriImage


@Composable
fun ShimoriSnackbar(
    data: SnackbarData,
    message: UiMessage?,
    dismissAction: (UiMessage?) -> Unit,
    modifier: Modifier = Modifier,
) {
    ShimoriSnackbar(
        data = data,
        payload = message,
        modifier = modifier,
        dismissAction = dismissAction,
        image = message?.image,
    )
}

@Composable
fun <T> ShimoriSnackbar(
    data: SnackbarData,
    payload: T,
    dismissAction: (T) -> Unit,
    modifier: Modifier = Modifier,
    image: ShimoriImage? = null,
) {
    Snackbar(
        modifier = modifier,
        action = {
            if (data.visuals.actionLabel != null) {
                androidx.compose.material3.TextButton(
                    onClick = {
                        dismissAction(payload)
                        data.performAction()
                    },
                ) {
                    Text(text = data.visuals.actionLabel!!)
                }
            }
        }
    ) {
        Row(
            Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (image != null) {
                AsyncImage(
                    model = image,
                    modifier = Modifier.size(24.dp),
                    contentDescription = null
                )

                Spacer(Modifier.width(12.dp))
            }


            Text(text = data.visuals.message)
        }
    }
}