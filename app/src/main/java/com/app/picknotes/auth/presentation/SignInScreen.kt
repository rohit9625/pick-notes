package com.app.picknotes.auth.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.app.picknotes.NavRoute
import com.app.picknotes.R
import com.app.picknotes.auth.presentation.event.AuthEvent
import com.app.picknotes.auth.presentation.state.SignInState
import com.app.picknotes.screens.TextFieldWithIcons

@Composable
fun SignInScreen(
    state: SignInState,
    onEvent: (AuthEvent)-> Unit,
    navController: NavController
) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.notebook),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.blur(2.dp)
        )
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .padding(horizontal = 32.dp)
                        .shadow(elevation = 4.dp, shape = RoundedCornerShape(5))
                        .background(color = Color.White, shape = RoundedCornerShape(5))
                ) {

                    Box(modifier = Modifier
                        .fillMaxWidth()
                        .height(4.dp)) {
                        // Show the LinearProgressIndicator only when isLoading is true
                        if (state.isLoading) {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = MaterialTheme.colorScheme.primary // Set the color as needed
                            )
                        }
                    }
                    Text(
                        text = stringResource(id = R.string.welcome_back),
                        modifier = Modifier.padding(top = 16.dp, bottom = 16.dp),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary,
                    )

                    TextFieldWithIcons(
                        text = state.email,
                        label = "Email",
                        onValueChange = { onEvent(AuthEvent.OnEmailChange(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        errorMessage = state.emailError
                    )

                    TextFieldWithIcons(
                        text = state.password,
                        label = "Password",
                        onValueChange = { onEvent(AuthEvent.OnPasswordChange(it)) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password,
                            imeAction = ImeAction.Done
                        ),
                        errorMessage = state.passwordError
                    )

                    Text(
                        text = state.response ?: "",
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .align(Alignment.Start),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodySmall
                    )

                    Button(
                        onClick = {
                            onEvent(AuthEvent.OnSubmit {
                                navController.navigate(NavRoute.HomeScreen) {
                                    popUpTo(route = NavRoute.SignInScreen) {
                                        inclusive = true
                                    }
                                }
                            })
                        },
                        enabled = !state.hasError,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 32.dp, start = 64.dp, end = 64.dp)
                            .heightIn(min = 40.dp)
                    ) {
                        Text(
                            text = stringResource(id = R.string.sign_in),
                            fontSize = 16.sp,
                            modifier = Modifier.padding(vertical = 6.dp)
                        )
                    }
                }

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.already_have_an_account)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = stringResource(id = R.string.sign_up),
                        color = Color.Blue,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable {
                            navController.navigate(NavRoute.SignUpScreen)
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = stringResource(id = R.string.pick_notes_by_rohit),
                fontStyle = FontStyle.Italic
            )
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignInScreenPreview() {
    SignInScreen(
        state = SignInState(),
        onEvent = {},
        navController = rememberNavController()
    )
}