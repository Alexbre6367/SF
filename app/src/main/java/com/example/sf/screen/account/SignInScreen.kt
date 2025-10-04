package com.example.sf.screen.account

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
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

    val nameState = remember { mutableStateOf("") }
    val emailState = remember { mutableStateOf("") }
    val passwordState = remember { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

    var isError by remember { mutableStateOf(false) }
    if (isError) {
        LaunchedEffect(Unit) {
            delay(5000)
            isError = false
        }
    }

    val animationColorError by animateColorAsState(
        targetValue = if (isError) Color.Red else Color.Transparent,
    )

    BackHandler {
        navController.navigate("sign_up")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                keyboardController?.hide()
                focusManager.clearFocus()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    navController.navigate("sign_up")
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = colorGreen,
                    modifier = Modifier.size(30.dp)
                )
            }

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
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(120.dp))
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                tint = colorGreen,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(80.dp))
            OutlinedTextField(
                value = emailState.value,
                onValueChange = {
                    emailState.value = it
                },
                placeholder = { Text("Ваша Почта") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedBorderColor = animationColorError,
                    cursorColor = colorGreen,
                    unfocusedBorderColor = animationColorError,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
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
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedBorderColor = animationColorError,
                    cursorColor = colorGreen,
                    unfocusedBorderColor = animationColorError,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = { navController.navigate("password_screen") },
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Забыли пароль?", color = Color.LightGray)
            }
        }


        Column(
            modifier = Modifier
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
                .align(Alignment.BottomCenter),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = {
                    if (emailState.value.isNotBlank() && passwordState.value.isNotBlank()) {
                        signIn(auth, emailState.value, passwordState.value) { isSuccess ->
                            if (isSuccess) {
                                viewModel.userCredential(
                                    emailState.value,
                                    passwordState.value,
                                    nameState.value, //под вопросом
                                )
                                navController.navigate("home_screen")
                            } else {
                                isError = true
                                focusManager.clearFocus()
                            }
                        }
                    } else {
                        isError = true
                        focusManager.clearFocus()
                        Log.d("MyLog", "Пустое поле")
                    }
                },
                modifier = Modifier
                    .height(55.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
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

private fun signIn(
    auth: FirebaseAuth,
    email: String,
    password: String,
    onResult: (Boolean) -> Unit
) {
    auth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            onResult(it.isSuccessful)
            if (it.isSuccessful) {
                Log.d("MyLog", "Успешный вход")
            } else {
                Log.d("MyLog", "Ошибка входа")
            }
        }
}
