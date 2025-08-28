@file:Suppress("EXTENSION_SHADOWED_BY_MEMBER")

package com.example.sf.screen.camera

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.PreviewView
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.navigation.NavHostController
import com.example.sf.database.fridge.Fridge
import com.example.sf.database.viewModel.CameraViewModel
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.common.asSolidChartColor

@Composable
fun CameraPreviewContent(
    viewModel: CameraViewModel,
    modifier: Modifier = Modifier,
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
) {
    val context = LocalContext.current

    val previewView = remember {
        PreviewView(context).apply {
            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
            scaleType = PreviewView.ScaleType.FILL_CENTER
        }
    }

    LaunchedEffect(lifecycleOwner, previewView.surfaceProvider) {
        viewModel.bindToCamera(
            lifecycleOwner = lifecycleOwner,
            surfaceProvider = previewView.surfaceProvider
        )
    }

    AndroidView(
        factory = { previewView },
        modifier = modifier
    )
}

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun CameraScreen(
    viewModelFridge: SFViewModel,
    viewModel: CameraViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val aiResult by viewModel.aiAnalysisResult.collectAsState()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { nonNullUri ->
            viewModel.processImageUriDirectly(nonNullUri, context.contentResolver)
        }
    }

    aiResult?.let {
        BottomSheet(
            showSheet = true,
            onDismiss = { viewModel.clearAi() },
            analysisResult = it,
            viewModelFridge = viewModelFridge,
            navController = navController
        )
    }

    BackHandler {
        navController.navigate("home_screen")
    }

    Box(modifier = Modifier.fillMaxSize()) {
        CameraPreviewContent(
            viewModel = viewModel,
            modifier = Modifier.matchParentSize()
        )

        IconButton(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(
                    bottom = 25.dp + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding(), start = 25.dp
                )
                .size(90.dp)
        ) {
            Icon(
                Icons.Outlined.Image,
                contentDescription = "Галерея",
                Modifier.size(40.dp)
            )
        }


        IconButton(
            onClick = {
                viewModel.imageCapture?.takePicture(
                    ContextCompat.getMainExecutor(context),
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(imageProxy: ImageProxy) {
                            val bitmap = imageProxy.toBitmap()
                            viewModel.processCapturedImage(bitmap)
                            imageProxy.close()
                        }
                    }
                )
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(
                    bottom = 25.dp + WindowInsets.navigationBars.asPaddingValues()
                        .calculateBottomPadding()
                )
                .size(90.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(77.dp)
                    .border(width = 3.5.dp, color = Color.White, shape = CircleShape)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun BottomSheet(
    showSheet: Boolean,
    onDismiss: () -> Unit,
    analysisResult: String,
    viewModelFridge: SFViewModel,
    navController: NavHostController
) {
    val sheetState = rememberModalBottomSheetState()

    val parsed = analysisResult.lines()

    val name = parsed.getOrNull(0)
    val calories = parsed.getOrNull(1)
    val proteins = parsed.getOrNull(2)
    val fats = parsed.getOrNull(3)
    val carbohydrates = parsed.getOrNull(4)



    if(showSheet) {
        ModalBottomSheet(
            onDismissRequest = { onDismiss() },
            sheetState = sheetState,
            containerColor = backgroundColor
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Результат от ИИ",
                        color = colorGreen,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Min)
                    ) {
                        Column(
                            modifier = Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 36.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Text(
                                text = name ?: "",
                                color = Color.Black,
                                fontSize = 26.sp
                            )

                            Spacer(Modifier.height(20.dp))
                            CircleChart(
                                proteins = proteins?.toFloat(),
                                fats = fats?.toFloat(),
                                carbohydrates = carbohydrates?.toFloat()
                            )
                        }

                        VerticalDivider(
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 30.dp),
                            color = Color.LightGray,
                        )

                        Column(
                            modifier = Modifier.weight(1f).padding(horizontal = 24.dp, vertical = 55.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Калории - ${calories ?: "Не удалось определить"}",
                                color = Color.Black,
                                fontSize = 18.sp,
                            )

                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = "Белки - ${proteins ?: "Не удалось определить"}",
                                color = Color.Black,
                                fontSize = 18.sp,
                            )

                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = "Жиры - ${fats ?: "Не удалось определить"}",
                                color = Color.Black,
                                fontSize = 18.sp,
                            )

                            Spacer(Modifier.height(20.dp))
                            Text(
                                text = "Углеводы - ${carbohydrates ?: "Не удалось определить"}",
                                color = Color.Black,
                                fontSize = 18.sp,
                            )
                        }
                    }

                    var isLoading by remember { mutableStateOf(false) }
                    Button(
                        onClick = {
                            if(!isLoading) {
                                isLoading = true
                                viewModelFridge.addFood(
                                    Fridge(
                                        foodName = name ?: "Ошибка",
                                        foodQuantity = calories ?: "Ошибка",
                                        foodDate = "",
                                        proteins = proteins?.toFloat(),
                                        fats = fats?.toFloat(),
                                        carbohydrates = carbohydrates?.toFloat()
                                    )
                                )
                                navController.navigate("home_screen")
                                onDismiss()
                                isLoading = false
                            }
                        },
                        enabled = !isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                            .size(56.dp),
                        shape = RoundedCornerShape(size = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorGreen,
                            contentColor = Color.White,
                            disabledContainerColor = colorGreen,
                            disabledContentColor = Color.White
                        )
                    ) {
                        Text(
                            text = "В холодильник",
                            color = Color.White
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CircleChart(
    proteins: Float?, fats: Float?, carbohydrates: Float?
) {
    val chartItems = listOf(
        CircleData(value = proteins ?: 0f, label = "Белки", color = Color.Red.asSolidChartColor()),
        CircleData(value = fats ?: 0f, label = "Жиры", color = Color.Blue.asSolidChartColor()),
        CircleData(value = carbohydrates ?: 0f, label = "Углеводы", color = Color.Green.asSolidChartColor()
        )
    )

    CircleChart(
        data = { chartItems },
        modifier = Modifier.size(150.dp),
    )
}

@Suppress("unused")
private fun ImageProxy.toBitmap(): Bitmap? {
    val buffer = planes[0].buffer
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}


