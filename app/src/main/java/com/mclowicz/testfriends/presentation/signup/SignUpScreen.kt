package com.mclowicz.testfriends.presentation.signup

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mclowicz.testfriends.R
import com.mclowicz.testfriends.ui.composables.InfoMessage
import com.mclowicz.testfriends.ui.composables.Loading
import com.mclowicz.testfriends.ui.composables.ScreenTitle

@Composable
fun SignUpScreen(
    viewModel: SignUpViewModel = hiltViewModel(),
    onSignUp: (userId: String) -> Unit,
) {
    val viewState by viewModel.state.collectAsStateWithLifecycle()
    val viewEvent by viewModel.event.collectAsStateWithLifecycle()

    LaunchedEffect(viewEvent) {
        if (viewEvent is SignUpEvent.SignedUp) {
            onSignUp((viewEvent as SignUpEvent.SignedUp).user.id)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .background(Color.White)
                .align(Alignment.CenterStart)
        )
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(0.5f)
                .background(Color.Black)
                .align(Alignment.CenterEnd)
        )
        Card(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.Center),
            backgroundColor = Color.White,
            shape = RoundedCornerShape(16.dp),
            border = BorderStroke(1.dp, Color.Black)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                ScreenTitle(R.string.signup_screen_title)
                Spacer(modifier = Modifier.height(16.dp))
                EmailField(
                    value = viewState.emailValue,
                    isError = viewState.isEmailError,
                    onValueChange = { value ->
                        viewModel.setEmailField(value)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                PasswordField(
                    value = viewState.passwordValue,
                    isError = viewState.isPasswordError,
                    onValueChange = { value ->
                        viewModel.setPasswordField(value)
                    }
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    modifier = Modifier.testTag(stringResource(id = R.string.signup_screen_title)),
                    onClick = {
                        viewModel.createAccount()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Black,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = stringResource(id = R.string.signup_screen_title))
                }
            }
        }
        InfoMessage(message = viewState.error?.msgResId)
        Loading(viewState.isLoading)
    }
}

@Composable
private fun EmailField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    val resource = if (!isError)
        R.string.email
    else
        R.string.emailError

    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.email)),
        value = value,
        onValueChange = onValueChange,
        isError = isError,
        label = { Text(text = stringResource(id = resource)) }
    )
}

@Composable
private fun PasswordField(
    value: String,
    isError: Boolean,
    onValueChange: (String) -> Unit
) {
    var isVisible by remember {
        mutableStateOf(false)
    }
    val visualTransformation = if (isVisible)
        VisualTransformation.None
    else
        PasswordVisualTransformation()

    val resource = if (!isError)
        R.string.password
    else
        R.string.passwordError

    OutlinedTextField(
        value = value,
        isError = isError,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.password)),
        trailingIcon = {
            VisibilityToggle(isVisible) {
                isVisible = !isVisible
            }
        },
        visualTransformation = visualTransformation,
        label = { Text(text = stringResource(id = resource)) },
    )
}

@Composable
private fun VisibilityToggle(
    isVisible: Boolean,
    onToggle: () -> Unit
) {
    val resourceId = if (isVisible)
        R.drawable.ic_visible
    else
        R.drawable.ic_invisible
    IconButton(onClick = { onToggle() }) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null
        )
    }
}