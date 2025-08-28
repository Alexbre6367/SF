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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay

@Composable
fun DeleteAccount(
    navController: NavHostController
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    val auth = FirebaseAuth.getInstance()

    val passwordState = remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    if (isError) {
        LaunchedEffect(Unit) {
            delay(5000)
            isError = false
        }
    }

    val currentUser = auth.currentUser
    val userEmail = currentUser?.email

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
                    navController.navigate("account_screen")
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
                text = "Удаление аккаунта",
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
            Spacer(modifier = Modifier.height(120.dp))
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                tint = colorGreen,
                modifier = Modifier.size(100.dp)
            )

            Spacer(modifier = Modifier.height(48.dp))
            OutlinedTextField(
                value = passwordState.value,
                onValueChange = {
                    passwordState.value = it
                },
                placeholder = { Text("Ваш Пароль") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(50.dp),
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

            Spacer(Modifier.height(24.dp))
            Text(
                text = "Это действие нельзя отменить. Вы потерете все данные",
                textAlign = TextAlign.Center,
                color = Color.LightGray
            )
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
                if (userEmail != null && passwordState.value.isNotEmpty()) {
                    deleteAccount(auth, userEmail, passwordState.value)
                    navController.navigate("sign_in")
                } else {
                    isError = true
                }
            },
            modifier = Modifier
                .height(55.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(50.dp),
            colors = ButtonDefaults.buttonColors(colorGreen)
        ) {
            Text(text = "Удалить аккаунт", color = Color.White)
        }
    }
}



private fun deleteAccount(auth: FirebaseAuth, email: String, password: String) {
    val credential = EmailAuthProvider.getCredential(email, password)
    auth.currentUser?.reauthenticate(credential)?.addOnCompleteListener { it ->
        if (it.isSuccessful) {
            auth.currentUser?.delete()?.addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d("MyLog", "Пользователь удален")
                } else {
                    Log.d("MyLog", "Ошибка удаления пользователя")
                }
            }
        } else {
            Log.d("MyLog", "Ошибка повторной аутентификации")
        }
    }
}