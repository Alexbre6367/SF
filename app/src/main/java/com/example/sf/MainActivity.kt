package com.example.sf

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.sf.database.viewModel.CameraViewModel
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.database.viewModel.SFViewModelFactory
import com.example.sf.navigation.AppNavGraph
import com.example.sf.ui.theme.SFTheme
import com.example.sf.ui.theme.colorGreen

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    private val cameraViewModel: CameraViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            val navController = rememberNavController()
            val customTextSelection = TextSelectionColors(
                handleColor = colorGreen,
                backgroundColor = colorGreen.copy(alpha = 0.4f)
            )

            SFTheme {
                val context = LocalContext.current
                val viewModel: SFViewModel = viewModel(
                    factory = SFViewModelFactory(context.applicationContext as Application)
                )

                CompositionLocalProvider(LocalTextSelectionColors provides customTextSelection) {
                    AppNavGraph(
                        viewModel = viewModel,
                        cameraViewModel = cameraViewModel,
                        navController = navController
                    )
                }
            }
        }

    }
}