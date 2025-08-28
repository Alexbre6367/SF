package com.example.sf.screen.account

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    navController: NavHostController
) {

    val auth = FirebaseAuth.getInstance()

    Log.d("MyLog", "User email: ${auth.currentUser?.email}")

    val currentUser = Firebase.auth.currentUser
    val email = currentUser?.email ?: "Не авторизован"
    val name = currentUser?.displayName

    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    BackHandler {
        navController.navigate("home_screen")
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(24.dp))
            Text(
                text = email,
                color = colorGreen
            )

            Spacer(Modifier.height(48.dp))
            Icon(
                Icons.Default.AccountCircle,
                contentDescription = "Аккаунт",
                tint = colorGreen,
                modifier = Modifier.size(200.dp)
            )

            Spacer(Modifier.height(12.dp))
            Text(
                text = "Добрый день, ${name?.trim()}!",
                color = Color.Black,
                fontSize = 24.sp
            )

            Spacer(Modifier.height(12.dp))
            TextButton(
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
            ) {
                Text(text = "Смена пароля", color = Color.LightGray)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    start = 24.dp,
                    end = 24.dp,
                    bottom = 24.dp + WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
                .align(Alignment.BottomCenter),
            Arrangement.Center
        ) {
            Button(
                onClick = {
                    navController.navigate("delete_account")
                },
                shape = RoundedCornerShape(
                    topStart = 50.dp,
                    bottomStart = 50.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp
                ),
                border = BorderStroke(1.dp, colorGreen),
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor)
            ) {
                Icon(
                    Icons.Default.DeleteOutline,
                    contentDescription = "Удалить",
                    tint = Color.Black,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Удалить",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }

            VerticalDivider(
                color = Color.White,
                modifier = Modifier
                    .height(50.dp)
                    .size(3.dp)
            )

            Button(
                onClick = {
                    scope.launch {
                        navController.navigate("sign_in")
                        delay(500)
                        signOut(auth)
                    }
                },
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    bottomStart = 8.dp,
                    topEnd = 50.dp,
                    bottomEnd = 50.dp
                ),
                border = BorderStroke(1.dp, colorGreen),
                modifier = Modifier.weight(1f).height(52.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor)

            ) {
                Icon(
                    Icons.AutoMirrored.Default.Logout,
                    contentDescription = "Выйти",
                    tint = Color.Black,
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "Выйти",
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black,
                    fontSize = 18.sp
                )
            }
        }
    }
}

private fun signOut(auth: FirebaseAuth) {
    auth.signOut()
}