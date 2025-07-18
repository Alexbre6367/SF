package com.example.sf.screen.account

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.colorGreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

@Composable
fun AccountScreen(
    viewModel: SFViewModel,
    navController: NavHostController
) {

    val auth = FirebaseAuth.getInstance().currentUser

    Log.d("MyLog", "User email: ${auth?.email}")

    val currentUser = Firebase.auth.currentUser
    val email = currentUser?.email ?: "Не авторизован"
    val name = currentUser?.displayName

    val context = LocalContext.current


    BackHandler {
        navController.navigate("home_scren")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = email,
                color = colorGreen
            )

            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                tint = colorGreen,
                modifier = Modifier.size(200.dp)
            )

            Text(
                text = "Добрый день, ${name?.trim()}!",
                color = Color.Black,
                fontSize = 24.sp
            )

            Button(
                onClick = {
                    FirebaseAuth.getInstance().sendPasswordResetEmail(email)
                        .addOnCompleteListener { task ->
                            if(task.isSuccessful) {
                                Toast.makeText(
                                    context,
                                    "Письмо отправлено на почту",
                                    Toast.LENGTH_LONG
                                ).show()
                            } else {
                                Toast.makeText(
                                    context,
                                    "Ошибка отправки письма",
                                    Toast.LENGTH_LONG
                                ).show()
                                Log.d("MyLog", "Ошибка отправки письма")
                            }
                        }
                },
                colors = ButtonDefaults.buttonColors(Color.Transparent),
                border = BorderStroke(1.dp, colorGreen)
            ) {
                Text(text = "Смена пароля", color = Color.Black)
            }
        }
    }
}