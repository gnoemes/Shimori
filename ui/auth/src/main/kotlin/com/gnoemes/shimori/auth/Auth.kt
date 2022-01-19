package com.gnoemes.shimori.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.gnoemes.shimori.common.R
import com.gnoemes.shimori.common.compose.ChevronIcon
import com.gnoemes.shimori.common.compose.EnlargedButton

@Composable
fun Auth(
    openSettings: () -> Unit,
) {
    Auth(viewModel = hiltViewModel(), openSettings)
}

@Composable
private fun Auth(
    viewModel: AuthViewModel,
    openSettings: () -> Unit
) {
    val signInLauncher =
        rememberLauncherForActivityResult(viewModel.buildLoginActivityResult()) { result ->
            if (result != null) {
                viewModel.onLoginResult(result)
            }
        }

    val signUpLauncher =
        rememberLauncherForActivityResult(viewModel.buildRegisterActivityResult()) { result ->
            if (result != null) {
                viewModel.onLoginResult(result)
            }
        }

    Auth(
        signIn = { signInLauncher.launch(Unit) },
        signUp = { signUpLauncher.launch(Unit) },
        openSettings = openSettings
    )
}

@Composable
private fun Auth(
    signIn: () -> Unit,
    signUp: () -> Unit,
    openSettings: () -> Unit,
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
    ) {

        Spacer(modifier = Modifier.height(120.dp))

        Icon(
            painter = painterResource(id = R.drawable.ic_profile),
            contentDescription = stringResource(id = R.string.profile),
            modifier = Modifier
                .size(96.dp)
                .background(
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.12f),
                    shape = CircleShape
                )
                .padding(24.dp)
                .align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = stringResource(id = R.string.sign_in_title),
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.sign_in_message),
            color = MaterialTheme.colorScheme.secondary,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(64.dp))

        EnlargedButton(
            onClick = signIn,
            text = stringResource(id = R.string.sign_in),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_sign_in),
                    contentDescription = null
                )
            },
            rightIcon = {
                ChevronIcon()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        EnlargedButton(
            onClick = signUp,
            text = stringResource(id = R.string.sign_up),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_create_account),
                    contentDescription = null
                )
            },
            rightIcon = {
                ChevronIcon()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        EnlargedButton(
            onClick = openSettings,
            text = stringResource(id = R.string.settings),
            modifier = Modifier
                .height(48.dp)
                .fillMaxWidth(),
            leftIcon = {
                Icon(
                    painter = painterResource(id = R.drawable.ic_settings),
                    contentDescription = null
                )
            }
        )
    }
}