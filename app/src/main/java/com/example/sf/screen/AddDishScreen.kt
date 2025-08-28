package com.example.sf.screen

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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fastfood
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sf.R
import com.example.sf.database.fridge.Meal
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun AddDishScreen(
    viewModel: SFViewModel,
    navController: NavHostController,
    addToMeal: Meal? = null
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val timeState = remember { mutableStateOf(addToMeal?.mealTime ?: "") }
    val dishState = remember { mutableStateOf(addToMeal?.dish ?: "") }

    var isError by remember { mutableStateOf(false) }
    if (isError) {
        LaunchedEffect(Unit) {
            delay(5000)
            isError = false
        }
    }

    val coroutineScope = rememberCoroutineScope()

    val animationColorError by animateColorAsState(
        targetValue = if (isError) Color.Red else Color.Transparent
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                keyboardController?.hide()
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = 8.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
                )
                .padding(horizontal = 16.dp)
                .background(backgroundColor),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(
                onClick = {
                    keyboardController?.hide()
                    navController.navigate("home_screen")
                }
            ) {
                Icon(
                    Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Назад",
                    tint = colorGreen,
                    modifier = Modifier.size(30.dp)
                )
            }

            if (addToMeal == null) {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        navController.navigate("add_screen")
                    }
                ) {
                    Icon(
                        Icons.Default.Fastfood,
                        contentDescription = "Блюдо",
                        tint = colorGreen,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(Modifier.height(70.dp))
            Icon(
                painter = painterResource(id = R.drawable.grocery_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                contentDescription = null,
                tint = colorGreen,
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(85.dp))
            OutlinedTextField(
                value = timeState.value,
                onValueChange = {
                    timeState.value = it
                },
                singleLine = true,
                placeholder = { Text("Прием пищи") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = if(addToMeal != null) Color.Black else Color.LightGray,
                    focusedBorderColor = animationColorError,
                    cursorColor = colorGreen,
                    unfocusedBorderColor = animationColorError,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(30.dp))
            OutlinedTextField(
                value = dishState.value,
                onValueChange = {
                    dishState.value = it
                },
                placeholder = { Text("Блюдо") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = if(addToMeal != null) Color.Black else Color.LightGray,
                    focusedBorderColor = animationColorError,
                    cursorColor = colorGreen,
                    unfocusedBorderColor = animationColorError,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(222.dp))
            var isLoading by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if(!isLoading) {
                        isLoading = true
                        coroutineScope.launch {
                            if (dishState.value.isNotBlank() && timeState.value.isNotBlank()) {
                                if (addToMeal != null) {
                                    viewModel.updateMeal(
                                        addToMeal.copy(
                                            mealTime = timeState.value.trim(),
                                            dish = dishState.value.trim()
                                        )
                                    )
                                } else {
                                    val time = timeState.value
                                    val dish = dishState.value

                                    if (time.isNotBlank() && dish.isNotBlank()) {
                                        val aiResponse = viewModel.analyzeMealResult("$time $dish")

                                        val parsed = aiResponse?.split(" ")?.mapNotNull { it.toFloatOrNull() }

                                        val proteins = parsed?.getOrNull(0)
                                        val fats = parsed?.getOrNull(1)
                                        val carbohydrates = parsed?.getOrNull(2)

                                        viewModel.addMeal(
                                            Meal(
                                                mealTime = timeState.value.trim(),
                                                dish = dishState.value.trim(),
                                                proteins = proteins,
                                                fats = fats,
                                                carbohydrates = carbohydrates
                                            )
                                        )
                                        viewModel.clearAi()
                                    }

                                }
                                navController.navigate("home_screen")
                            } else {
                                isError = true
                            }
                        }
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
                Text(text = if (addToMeal != null) "Сохранить" else "Добавить", color = Color.White)
            }
        }
    }
}
