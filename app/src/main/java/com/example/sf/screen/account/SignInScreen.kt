package com.example.sf.screen.account

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Album
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.outlined.Circle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.colorGreen
import com.example.sf.ui.theme.colorRed
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun SignInScreen(
    viewModel: SFViewModel,
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val auth = FirebaseAuth.getInstance()

    val nameState = remember { mutableStateOf("")}
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }
    if(isError) {
        LaunchedEffect(Unit) {
            delay(5000)
            isError = false
        }
    }
    val animationColorLightGray by animateColorAsState(
        targetValue = if(isError) colorRed else Color.LightGray,
        animationSpec = tween(durationMillis = 500)
    )
    val animationColorGreen by animateColorAsState(
        targetValue = if(isError) colorRed else colorGreen,
        animationSpec = tween(durationMillis = 500)
    )

    val customTextSelection = TextSelectionColors(
        handleColor = colorGreen,
        backgroundColor = colorGreen.copy(alpha = 0.4f)
    )

    BackHandler {
        navController.popBackStack()
    }

    Box(
        modifier = Modifier
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
    ) {
        CompositionLocalProvider(LocalTextSelectionColors provides customTextSelection) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                Row(
                    modifier = Modifier
                        .windowInsetsPadding(WindowInsets.statusBars)
                        .padding(start = 24.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            navController.popBackStack()
                        },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.AutoMirrored.Default.ArrowBackIos,
                        contentDescription = "Назад",
                        tint = colorGreen
                    )

                    Text(
                        text = "Войти",
                        style = TextStyle(
                            fontSize = 24.sp,
                            color = Color.Black
                        ),
                        modifier = Modifier.padding(start = 12.dp)
                    )
                }

                Column(
                    modifier = Modifier.padding(
                        start = 20.dp,
                        end = 20.dp,
                        top = 100.dp
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Icon(
                        Icons.Default.AccountCircle,
                        contentDescription = "Аккаунт",
                        tint = colorGreen,
                        modifier = Modifier.size(100.dp)
                    )

                    Spacer(modifier = Modifier.height(48.dp))
                    OutlinedTextField(
                        value = emailState.value,
                        onValueChange = {
                            emailState.value = it
                        },
                        placeholder = { Text("Ваша Почта") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.LightGray,
                            focusedBorderColor = animationColorGreen,
                            cursorColor = Color.Black,
                            unfocusedBorderColor = animationColorLightGray,
                            disabledBorderColor = animationColorLightGray,
                        )
                    )

                    Spacer(modifier = Modifier.height(24.dp))
                    OutlinedTextField(
                        value = passwordState.value,
                        onValueChange = {
                            passwordState.value = it
                        },
                        placeholder = { Text("Пароль") },
                        singleLine = true,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                Icon(
                                    if (passwordVisibility) Icons.Default.SearchOff else Icons.Default.Search,
                                    contentDescription = "Просмотр"
                                )
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.Black,
                            unfocusedTextColor = Color.LightGray,
                            focusedBorderColor = animationColorGreen,
                            cursorColor = Color.Black,
                            unfocusedBorderColor = animationColorLightGray,
                            disabledBorderColor = animationColorLightGray
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    TextButton(
                        onClick = { navController.navigate("password_screen") },
                        modifier = Modifier
                            .height(55.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = "Забыли пароль?", color = Color.LightGray)
                    }
                }
            }

            Column(
                modifier = Modifier
                    .padding(start = 24.dp, end = 24.dp, bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding())
                    .align(Alignment.BottomCenter),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        if(emailState.value.isNotBlank() && passwordState.value.isNotBlank()) {
                            signIn(auth, emailState.value, passwordState.value) { isSuccess ->
                                if(isSuccess) {
                                    viewModel.userCredential(
                                        emailState.value,
                                        passwordState.value,
                                        nameState.value
                                    )
                                    navController.navigate("account_screen")
                                } else {
                                    isError = true
                                    focusManager.clearFocus()
                                }
                            }
                        }
                    },
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(50.dp),
                    colors = ButtonDefaults.buttonColors(colorGreen)
                ) {
                    Text(text = "Продолжить", color = Color.White)
                }

                TextButton(
                    onClick = { navController.navigate("sign_up") },
                    modifier = Modifier
                        .height(55.dp)
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                ) {
                    Text(text = "Регистрация", color = Color.LightGray)
                }
            }
        }
    }
}

private fun signIn(auth: FirebaseAuth, email: String, password: String, onResult: (Boolean) -> Unit) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            onResult(it.isSuccessful)
            if(it.isSuccessful) {
                Log.d("MyLog", "Успешный вход")
            } else {
                Log.d("MyLog", "Ошибка входа")
            }
        }
}
