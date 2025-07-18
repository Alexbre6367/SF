package com.example.sf

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.navigation.AppNavGraph
import com.example.sf.screen.account.AccountScreen
import com.example.sf.screen.account.SignInScreen
import com.example.sf.ui.theme.SFTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    private val viewModel: SFViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            SFTheme {
                AppNavGraph(
                    viewModel = viewModel,
                    navController = navController
                )
            }
        }

    }


}

