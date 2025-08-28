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
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.sf.R
import com.example.sf.database.fridge.Fridge
import com.example.sf.database.viewModel.SFViewModel
import com.example.sf.ui.theme.backgroundColor
import com.example.sf.ui.theme.colorGreen
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AddScreen(
    viewModel: SFViewModel,
    navController: NavHostController,
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    val foodNameState = remember { mutableStateOf("") }
    val quantityState = remember { mutableStateOf("") }
    val dateState = remember { mutableStateOf("") }

    var isError by remember { mutableStateOf(false) }
    if (isError) {
        LaunchedEffect(Unit) {
            delay(5000)
            isError = false
        }
    }

    val animationColorError by animateColorAsState(
        targetValue = if (isError) Color.Red else Color.Transparent,
    )

    val coroutineScope = rememberCoroutineScope()

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
               .padding(top = 8.dp + WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
               .padding(horizontal = 16.dp)
               .background(backgroundColor),
           verticalAlignment = Alignment.CenterVertically,
           horizontalArrangement = Arrangement.SpaceBetween
       )  {
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

           IconButton(
               onClick = {
                   keyboardController?.hide()
                   navController.navigate("add_dish_screen")
               }
           ) {
               Icon(
                   painter = painterResource(id = R.drawable.grocery_24dp_e3e3e3_fill0_wght400_grad0_opsz24),
                   contentDescription = "Блюдо",
                   tint = colorGreen,
                   modifier = Modifier.size(30.dp)
               )
           }
       }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(70.dp))
            Icon(
                Icons.Default.Fastfood,
                contentDescription = "Еда",
                tint = colorGreen,
                modifier = Modifier.size(120.dp)
            )

            Spacer(Modifier.height(85.dp))
            OutlinedTextField(
                value = foodNameState.value,
                onValueChange = {
                    foodNameState.value = it
                },
                singleLine = true,
                placeholder = { Text("Название") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(20.dp),
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

            Spacer(Modifier.height(30.dp))
            OutlinedTextField(
                value = quantityState.value,
                onValueChange = {
                    quantityState.value = it
                },
                singleLine = true,
                placeholder = { Text(text = "Колличество") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(size = 20.dp),
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

            Spacer(Modifier.height(30.dp))
            OutlinedTextField(
                value = dateState.value,
                onValueChange = {
                    dateState.value = it
                },
                singleLine = true,
                placeholder = { Text(text = "Срок годности") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                shape = RoundedCornerShape(size = 20.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.LightGray,
                    focusedBorderColor = Color.Transparent,
                    cursorColor = colorGreen,
                    unfocusedBorderColor = Color.Transparent,
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White
                )
            )

            Spacer(Modifier.height(135.dp))
            var isLoading by remember { mutableStateOf(false) }
            Button(
                onClick = {
                    if(!isLoading) {
                        isLoading = true
                        coroutineScope.launch {
                            val foodName = foodNameState.value
                            val quantity = quantityState.value

                            if (foodName.isNotBlank() && quantity.isNotBlank()) {
                                val aiResponse = viewModel.analyzeFoodResult("$foodName $quantity")

                                val parsed =
                                    aiResponse?.split(" ")?.mapNotNull { it.toFloatOrNull() }

                                val proteins = parsed?.getOrNull(0)
                                val fats = parsed?.getOrNull(1)
                                val carbohydrates = parsed?.getOrNull(2)

                                viewModel.addFood(
                                    Fridge(
                                        foodName = foodNameState.value.trim(),
                                        foodQuantity = quantityState.value.trim(),
                                        foodDate = dateState.value.trim(),
                                        proteins = proteins,
                                        fats = fats,
                                        carbohydrates = carbohydrates
                                    )
                                )
                                viewModel.clearAi()
                                navController.navigate("home_screen")

                            } else {
                                isError = true
                            }

                            isLoading = false
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
                ),
            ) {
                Text(text = "Добавить", color = Color.White)
            }
        }
    }
}