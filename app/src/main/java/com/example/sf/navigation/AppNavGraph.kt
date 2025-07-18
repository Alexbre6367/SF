package com.example.sf.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExitTransition.Companion.None
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.navigation.NavHostController
import com.example.sf.screen.account.SignUpScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.screen.account.AccountScreen
import com.example.sf.screen.account.PasswordScreen
import com.example.sf.screen.account.SignInScreen

@Composable
fun AppNavGraph(
    viewModel: SFViewModel,
    navController: NavHostController
) {
    Surface (
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = "sign_in"
        ) {
            composable(
                route = "sign_up",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                SignUpScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = "sign_in",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                SignInScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = "password_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                PasswordScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = "account_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                AccountScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }
    }
}