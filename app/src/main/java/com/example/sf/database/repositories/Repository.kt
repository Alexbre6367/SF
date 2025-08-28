package com.example.sf.database.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import com.example.sf.database.fridge.Fridge
import com.example.sf.database.fridge.FridgeDAO
import com.example.sf.database.fridge.Meal
import com.example.sf.database.fridge.MealsDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.invoke

class Repository(private val fridgeDao: FridgeDAO, private val mealsDao: MealsDAO) {
    val fridgeList: LiveData<List<Fridge>> = fridgeDao.getFood()
    val mealsList: LiveData<List<Meal>> = mealsDao.getMeals()

    suspend fun addFood(food: Fridge) {
        Dispatchers.IO {
            fridgeDao.addFood(food)
            Log.d("MyLog", "Добавлено ${food.foodName}")
        }
    }

    suspend fun deleteFood(id: Int) {
        Dispatchers.IO {
            fridgeDao.deleteFood(id)
        }
    }

    suspend fun addMeal(meal: Meal) {
        Dispatchers.IO {
            mealsDao.addMeal(meal)
            Log.d("MyLog", "Добавлено ${meal.dish}")
        }
    }

    suspend fun deleteMeal(id: Int) {
        Dispatchers.IO {
            mealsDao.deleteMeal(id)
        }
    }

    suspend fun updateMeal(meal: Meal) {
        Dispatchers.IO {
            mealsDao.upMeal(meal)
        }
    }
}