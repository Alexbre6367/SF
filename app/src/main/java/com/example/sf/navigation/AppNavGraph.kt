package com.example.sf.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition.Companion.None
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.sf.database.viewModel.CameraViewModel
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.screen.AddDishScreen
import com.example.sf.screen.AddScreen
import com.example.sf.screen.HomeScreen
import com.example.sf.screen.account.AccountScreen
import com.example.sf.screen.account.DeleteAccount
import com.example.sf.screen.account.PasswordScreen
import com.example.sf.screen.account.SignInScreen
import com.example.sf.screen.account.SignUpScreen
import com.example.sf.screen.camera.CameraScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun AppNavGraph(
    viewModel: SFViewModel,
    cameraViewModel: CameraViewModel,
    navController: NavHostController
) {

    val user = FirebaseAuth.getInstance().currentUser

    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        NavHost(
            navController = navController,
            startDestination = if(user != null) "home_screen" else "sign_in"
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
                    navController = navController
                )
            }

            composable(
                route = "delete_account",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                DeleteAccount(
                    navController = navController
                )
            }

            composable(
                route = "home_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                HomeScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = "camera_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                CameraScreen(
                    viewModel = cameraViewModel,
                    navController = navController,
                    viewModelFridge = viewModel
                )
            }

            composable(
                route = "add_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                AddScreen(
                    viewModel = viewModel,
                    navController = navController
                )
            }

            composable(
                route = "add_dish_screen/{mealId}",
                arguments = listOf(navArgument("mealId") {
                    type = NavType.IntType
                    defaultValue = -1
                }),
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) { backStackEntry ->
                val mealId = backStackEntry.arguments?.getInt("mealId") ?: -1
                val selectedMeal = viewModel.mealsList.value?.find { it.id == mealId }

                AddDishScreen(
                    viewModel = viewModel,
                    navController = navController,
                    addToMeal = selectedMeal
                )
            }

            composable(
                route = "add_dish_screen",
                enterTransition = { EnterTransition.None },
                exitTransition = { None },
                popExitTransition = { None },
            ) {
                AddDishScreen(
                    viewModel = viewModel,
                    navController = navController,
                    addToMeal = null
                )
            }
        }
    }
}
