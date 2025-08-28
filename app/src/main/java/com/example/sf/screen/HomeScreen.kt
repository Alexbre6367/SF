package com.example.sf.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.sf.database.fridge.Fridge
import com.example.sf.database.fridge.Meal
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import com.example.sf.ui.theme.colorRed
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.himanshoe.charty.circle.CircleChart
import com.himanshoe.charty.circle.model.CircleData
import com.himanshoe.charty.common.asSolidChartColor
import kotlinx.coroutines.delay


@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: SFViewModel, navController: NavHostController
) {

    var deleteActivated by remember { mutableStateOf(false) }
    val selectedFood by viewModel.isSelectedFood.collectAsState()
    val selectedMeal by viewModel.isSelectedMeal.collectAsState()

    val userFood by viewModel.foodList.observeAsState(emptyList())
    val list = userFood.asReversed()

    val userMeal by viewModel.mealsList.observeAsState(emptyList())

    val cameraPermissionState = rememberPermissionState(
        android.Manifest.permission.CAMERA
    )

    var permissionRequested by remember { mutableStateOf(false) }

    LaunchedEffect(cameraPermissionState.status, permissionRequested) {
        if (permissionRequested && cameraPermissionState.status.isGranted) {
            navController.navigate("camera_screen")
            permissionRequested = false
        }
    }

    BackHandler {
        viewModel.clearSelected()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            )
            {
                viewModel.clearSelected()
            },
        containerColor = backgroundColor,
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start = 36.dp,
                        end = 36.dp,
                        bottom = 12.dp + WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    )
                    .clip(RoundedCornerShape(48.dp))
                    .background(Color.White)
                    .height(70.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { navController.navigate("account_screen") },
                    modifier = Modifier.padding(start = 34.dp)
                ) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = "Аккаунт",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )
                }

                IconButton(
                    onClick = {
                        if (selectedFood.isNotEmpty() || selectedMeal.isNotEmpty()) {
                            if (deleteActivated) {
                                viewModel.deleteFood()
                                deleteActivated = false
                            } else {
                                deleteActivated = true
                            }
                        } else {
                            navController.navigate("add_screen")
                        }
                    },
                ) {
                    Icon(
                        imageVector = when {
                            (selectedFood.isNotEmpty() || selectedMeal.isNotEmpty()) && deleteActivated -> Icons.Default.Check
                            selectedFood.isNotEmpty() || selectedMeal.isNotEmpty() -> Icons.Default.DeleteOutline
                            else -> Icons.Default.Add
                        },
                        contentDescription = "Добавить",
                        tint = if (selectedFood.isNotEmpty() || selectedMeal.isNotEmpty()) colorRed else Color.Gray,
                        modifier = Modifier.size(30.dp)
                    )
                }
                IconButton(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            navController.navigate("camera_screen")
                            viewModel.clearSelected()
                        } else {
                            permissionRequested = true
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier.padding(end = 34.dp)
                ) {
                    Icon(
                        Icons.Outlined.CameraAlt,
                        contentDescription = "Камера",
                        tint = Color.Gray,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }) {
                    viewModel.clearSelected()
                },
        ) {
            Text(
                text = "В холодильнике",
                fontSize = 24.sp,
                color = Color.Black,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(horizontal = 24.dp).padding(top = 12.dp, bottom = 24.dp)
            )

            if (userFood.isEmpty()) {
                Column(
                    modifier = Modifier
                        .padding(start = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .width(150.dp)
                        .height(230.dp)
                        .clickable(
                            onClick = {
                                navController.navigate("add_screen")
                            }),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить",
                        tint = colorGreen,
                        modifier = Modifier.size(55.dp)
                    )

                    Spacer(Modifier.height(8.dp))
                    Text(
                        text = """Добавьте
                                |продукт в холодильник""".trimMargin(),
                        fontSize = 16.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                LazyRow {
                    itemsIndexed(list) { index, food ->
                        FridgeScreen(
                            foodItem = food,
                            isSelected = food.id in selectedFood,
                            onToggleSelection = { viewModel.toggleFood(food.id) },
                            startingPadding = if (index == 0) 24.dp else 0.dp,
                            endPadding = if (index == userFood.lastIndex) 24.dp else 16.dp
                        )
                    }
                }
            }

            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "План питания",
                    fontSize = 24.sp,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = FontFamily.SansSerif,
                    modifier = Modifier.padding(24.dp).weight(1f)
                )

                if(viewModel.isSelectedMeal.value.size == 1) {
                    val selectedMeal = viewModel.isSelectedMeal.value.firstOrNull()

                    selectedMeal?.let { mealId ->
                        IconButton(
                            onClick = { navController.navigate("add_dish_screen/$mealId") },
                            modifier = Modifier.padding(end = 24.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Редактировать",
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            if (userMeal.isEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(150.dp)
                        .padding(horizontal = 24.dp)
                        .clip(RoundedCornerShape(24.dp))
                        .background(Color.White)
                        .clickable(
                            onClick = { navController.navigate("add_dish_screen") }),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Default.Add,
                        contentDescription = "Добавить блюдо",
                        tint = colorGreen,
                        modifier = Modifier.size(45.dp)
                    )


                    Spacer(Modifier.width(40.dp))
                    Text(
                        text = """Составляйте план
                                    |питания на день""".trimMargin(),
                        color = Color.Gray,
                        fontSize = 20.sp
                    )
                }
            } else {
                Column {
                    userMeal.forEach { meal ->
                        EatingScreen(
                            mealItem = meal,
                            isSelected = meal.id in selectedMeal,
                            onToggleSelection = { viewModel.toggleMeal(meal.id) })
                    }
                }
            }
        }

    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun FridgeScreen(
    foodItem: Fridge,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
    startingPadding: Dp,
    endPadding: Dp
) {

    if (isSelected) {
        LaunchedEffect(foodItem.id) {
            delay(10000)
            onToggleSelection()
        }
    }

    var isOverText by remember { mutableStateOf(false) }
    var isResult by remember { mutableStateOf<TextLayoutResult?>(null) }
    var isPFC by remember { mutableStateOf(false) }

    val targetWidth = if (isOverText || isPFC) 250.dp else 150.dp
    val width by animateDpAsState(targetWidth)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = startingPadding, end = endPadding),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(24.dp))
                .border(
                    2.dp, if (isSelected) colorGreen else Color.White, RoundedCornerShape(24.dp)
                )
                .background(Color.White)
                .width(width)
                .height(230.dp)
                .combinedClickable(onClick = {
                    isResult?.let { result ->
                        isOverText = result.hasVisualOverflow
                    }
                    if (foodItem.proteins != null && foodItem.fats != null && foodItem.carbohydrates != null) {
                        isPFC = !isPFC
                    }
                }, onLongClick = { onToggleSelection() }),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                AnimatedContent(
                    targetState = isPFC
                ) { expanded ->
                    if (expanded) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = """
                            |Белки - ${foodItem.proteins?.toInt()}г
                            |Жиры - ${foodItem.fats?.toInt()}г
                            |Углеводы - ${foodItem.carbohydrates?.toInt()}г
                        """.trimMargin(),
                                color = Color.Black,
                                fontSize = 15.sp,
                            )
                            Spacer(Modifier.width(20.dp))
                            CircleChart(
                                proteins = foodItem.proteins,
                                fats = foodItem.fats,
                                carbohydrates = foodItem.carbohydrates
                            )
                        }
                    } else {
                        CircleChart(
                            proteins = foodItem.proteins,
                            fats = foodItem.fats,
                            carbohydrates = foodItem.carbohydrates
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))
            Text(
                text = foodItem.foodName,
                color = Color.Black,
                maxLines = 2,
                fontSize = 20.sp,
                textAlign = TextAlign.Center,
                overflow = TextOverflow.Ellipsis,
                onTextLayout = { isResult = it })

            Spacer(Modifier.weight(1f))
            Text(
                text = foodItem.foodQuantity,
                color = Color.Gray,
                maxLines = 1,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 18.dp)
            )
        }
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun EatingScreen(
    mealItem: Meal,
    isSelected: Boolean,
    onToggleSelection: () -> Unit,
) {

    if (isSelected) {
        LaunchedEffect(mealItem.id) {
            delay(10000)
            onToggleSelection()
        }
    }

    var isPFC by remember { mutableStateOf(false) }
    var textHeight by remember { mutableStateOf(0.dp) }

    val baseHeight = 100.dp

    val targetHeigh = if (isPFC) baseHeight + textHeight else 50.dp
    val height by animateDpAsState(targetHeigh)

    val currentDensity = LocalDensity.current

    SubcomposeLayout { constraints ->
        val textMeasurable = subcompose("dishText") {
            Text(
                text = mealItem.dish,
                color = Color.Black,
                fontSize = 20.sp,
                modifier = Modifier
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp)
            )
        }.first().measure(constraints)

        val measuredTextHeight = with(currentDensity) { textMeasurable.height.toDp() }

        if (textHeight != measuredTextHeight) {
            textHeight = measuredTextHeight
        }

        layout(0, 0) {}
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp, bottom = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(Color.White)
                .height(height)
                .border(
                    if (isSelected) 1.8.dp else 0.dp,
                    if (isSelected) colorGreen else Color.White,
                    RoundedCornerShape(24.dp)
                )
                .combinedClickable(
                    onClick = {
                        isPFC = !isPFC
                        if (isSelected) {
                            onToggleSelection()
                        }
                    },
                    onLongClick = { onToggleSelection() }
                ),
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = isPFC
            ) { expanded ->
                if (expanded) {
                    Column(
                        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = mealItem.mealTime,
                            color = Color.Black,
                            maxLines = 1,
                            fontSize = 25.sp,
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 4.dp)
                                .align(Alignment.CenterHorizontally)
                        )

                        if(mealItem.proteins != null) {
                            Text(
                                text = "${mealItem.proteins?.toInt()}г белки • " +
                                        "${mealItem.fats?.toInt()}г жиры • " +
                                        "${mealItem.carbohydrates?.toInt()}г углеводы",
                                color = Color.DarkGray,
                                maxLines = 1,
                                fontSize = 12.sp,
                                modifier = Modifier
                                    .padding(bottom = 6.dp)
                                    .align(Alignment.CenterHorizontally)
                            )
                        }

                        HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 24.dp),
                            thickness = 1.dp,
                            color = Color.LightGray
                        )

                        Text(
                            text = mealItem.dish,
                            color = Color.Black,
                            fontSize = 20.sp,
                            modifier = Modifier
                                .padding(horizontal = 24.dp)
                                .padding(top = 8.dp)
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 24.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = mealItem.mealTime,
                            color = Color.Black,
                            maxLines = 1,
                            fontSize = 20.sp,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            Icons.Default.KeyboardArrowDown,
                            contentDescription = "Развернуть",
                            tint = Color.Black
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
        CircleData(
            value = carbohydrates ?: 0f, label = "Углеводы", color = Color.Green.asSolidChartColor()
        )
    )

    CircleChart(
        data = { chartItems },
        modifier = Modifier.size(100.dp),
    )
}

