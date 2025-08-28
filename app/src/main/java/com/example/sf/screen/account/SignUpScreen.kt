package com.example.sf.screen.account

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Circle
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
import com.google.firebase.auth.UserProfileChangeRequest
import kotlinx.coroutines.delay


@Composable
fun SignUpScreen(
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
    var checked by remember { mutableStateOf(false) }
    var checkedOne by remember { mutableStateOf(false) }

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
        navController.popBackStack()
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
                text = "Регистрация",
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
        ) {
            Spacer(Modifier.height(100.dp))
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                tint = colorGreen,
                modifier = Modifier.size(100.dp).align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(30.dp))
            OutlinedTextField(
                value = nameState.value,
                onValueChange = {
                    nameState.value = it
                },
                placeholder = { Text("Ваше имя") },
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
                placeholder = { Text("Пароль(от 6 символов)") },
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

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                horizontalArrangement = Arrangement.Start
            ) {
                Row(
                    modifier = Modifier
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            checked = !checked
                            checkedOne = false
                        }
                ) {
                    Icon(
                        if (checked) Icons.Default.Circle else Icons.Outlined.Circle,
                        contentDescription = "Men",
                        tint = if (checked) colorGreen else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Мужчина",
                        color = if (checked) colorGreen else Color.Black
                    )
                }
                Spacer(modifier = Modifier.width(20.dp))
                Row(
                    modifier = Modifier
                        .padding(start = 12.dp)
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            checkedOne = !checkedOne
                            checked = false
                        }
                ) {
                    Icon(
                        if (checkedOne) Icons.Default.Circle else Icons.Outlined.Circle,
                        contentDescription = "Women",
                        tint = if (checkedOne) colorGreen else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Девушка",
                        color = if (checkedOne) colorGreen else Color.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(42.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }
                    ) {

                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "Настройка уведомлений", color = Color.Black)
                Icon(
                    Icons.AutoMirrored.Default.ArrowForwardIos,
                    contentDescription = "Уведомления",
                    modifier = Modifier.size(15.dp),
                    tint = Color.Black
                )
            }
        }
    }

    Column(
        modifier = Modifier
            .padding(
                start = 24.dp,
                end = 24.dp,
                bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues()
                    .calculateBottomPadding()
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Bottom
    ) {
        Button(
            onClick = {
                if (emailState.value.isNotBlank() && passwordState.value.isNotBlank() && nameState.value.isNotBlank()) {
                    signUp(
                        auth,
                        emailState.value,
                        passwordState.value,
                        nameState.value
                    ) { isSuccess ->
                        if (isSuccess) {
                            viewModel.userCredential(
                                emailState.value,
                                passwordState.value,
                                nameState.value,
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
            onClick = {
                navController.navigate("sign_in")
            },
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text(text = "Войти", color = Color.LightGray)
        }
    }
}



private fun signUp(
    auth: FirebaseAuth,
    email: String,
    password: String,
    name: String,
    onResult: (Boolean) -> Unit
) {
    auth.createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            onResult(it.isSuccessful)
            if (it.isSuccessful) {
                val firebaseUser = auth.currentUser
                val profileUpdate = UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build()
                firebaseUser?.updateProfile(profileUpdate)
                Log.d("MyLog", "Успешная регистрация пользователя ($name)")
            } else {
                Log.d("MyLog", "Ошибка регистрации")
            }
        }

}
