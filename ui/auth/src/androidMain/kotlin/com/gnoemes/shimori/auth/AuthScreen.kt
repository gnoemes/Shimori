package com.gnoemes.shimori.auth

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import cafe.adriel.voyager.core.registry.rememberScreen
import cafe.adriel.voyager.kodein.rememberScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import cafe.adriel.voyager.navigator.tab.TabOptions
import com.gnoemes.shimori.common.ui.components.Background
import com.gnoemes.shimori.common.ui.components.ChevronIcon
import com.gnoemes.shimori.common.ui.components.EnlargedButton
import com.gnoemes.shimori.common.ui.components.ScaffoldExtended
import com.gnoemes.shimori.common.ui.components.ShimoriSnackbar
import com.gnoemes.shimori.common.ui.components.rememberSnackbarHostState
import com.gnoemes.shimori.common.ui.navigation.FeatureScreen
import com.gnoemes.shimori.common.ui.navigation.Tab
import com.gnoemes.shimori.common.ui.theme.dimens
import kotlinx.coroutines.delay

object AuthScreen : Tab {
    override val options: TabOptions
        @Composable
        get() {
            return TabOptions(
                index = 2u,
                title = stringResource(R.string.profile),
                icon = painterResource(id = R.drawable.ic_profile)
            )
        }

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel<AuthScreenModel>()
        val navigator = LocalNavigator.currentOrThrow
        val settingsScreen = rememberScreen(FeatureScreen.Settings)

        val signInLauncher =
            rememberLauncherForActivityResult(screenModel.buildLoginActivityResult()) { result ->
                if (result != null) {
                    screenModel.onLoginResult(result)
                }
            }

        val signUpLauncher =
            rememberLauncherForActivityResult(screenModel.buildRegisterActivityResult()) { result ->
                if (result != null) {
                    screenModel.onLoginResult(result)
                }
            }

        val snackbarHostState = rememberSnackbarHostState()

        val error by screenModel.error.collectAsStateWithLifecycle(null)

        error?.let { message ->
            LaunchedEffect(message) {
                snackbarHostState.showSnackbar(message.message)

                delay(100L)
                screenModel.onMessageShown(message.id)
            }
        }

        Background {
            Auth(
                snackbarHostState = snackbarHostState,
                signIn = { signInLauncher.launch(Unit) },
                signUp = { signUpLauncher.launch(Unit) },
                openSettings = { navigator.push(settingsScreen) }
            )
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun Auth(
        snackbarHostState: SnackbarHostState,
        signIn: () -> Unit,
        signUp: () -> Unit,
        openSettings: () -> Unit,
    ) {

        ScaffoldExtended(
            snackbarHost = {
                ShimoriSnackbar(
                    hostState = snackbarHostState,
                    modifier = Modifier.fillMaxWidth(),
                    icon = {
                        Image(
                            painter = painterResource(id = R.drawable.ic_attention),
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                )
            },
            bottomBar = {
                Spacer(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .height(MaterialTheme.dimens.bottomBarHeight)
                )
            }
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

    }
}