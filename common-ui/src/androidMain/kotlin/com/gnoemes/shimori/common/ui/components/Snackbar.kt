package com.gnoemes.shimori.common.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gnoemes.shimori.common.ui.theme.ShimoriSmallRoundedCornerShape

@Composable
fun ShimoriSnackbar(
    hostState: SnackbarHostState,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = { hostState.currentSnackbarData?.dismiss() },
    icon: @Composable (RowScope.() -> Unit)? = null,
    snackbar: @Composable (SnackbarData) -> Unit = { data ->
        SwipeDismissSnackbar(
            data = data,
            icon = icon,
            onDismiss = onDismiss,
        )
    }
) {
    SnackbarHost(
        hostState = hostState,
        modifier = modifier,
        snackbar = snackbar
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SwipeDismissSnackbar(
    data: SnackbarData,
    onDismiss: (() -> Unit)? = null,
    icon: @Composable (RowScope.() -> Unit)?,
    snackbar: @Composable (SnackbarData) -> Unit = {
        Snackbar(
            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
            shape = ShimoriSmallRoundedCornerShape,
            action = when (val actionLabel = data.visuals.actionLabel) {
                null, "" -> null
                else -> {
                    @Composable() {
                        TextButton(
                            //override button color to match snackbar component design
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = LocalContentColor.current
                            ),
                            onClick = { data.performAction() }
                        ) {
                            Text(text = actionLabel)
                        }
                    }
                }
            }
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (icon != null) {
                    icon()
                    Spacer(modifier = Modifier.width(12.dp))
                }

                Text(
                    text = it.visuals.message,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    },
) {
    val dismissState = rememberDismissState {
        if (it != DismissValue.Default) {
            // First dismiss the snackbar
            data.dismiss()
            // Then invoke the callback
            onDismiss?.invoke()
        }
        true
    }

    SwipeToDismiss(
        state = dismissState,
        directions = setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart),
        background = {},
        dismissContent = { snackbar(data) }
    )
}

@Composable
fun rememberSnackbarHostState() = remember {
    SnackbarHostState()
}