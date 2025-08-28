package com.example.sf.database.fridge

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface FridgeDAO {
    @Query("SELECT * FROM fridge")
    fun getFood(): LiveData<List<Fridge>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addFood(food: Fridge)

    @Query("DELETE FROM fridge WHERE id = :id")
    suspend fun deleteFood(id: Int)
}

@Dao
interface MealsDAO {
    @Query("SELECT * FROM meals")
    fun getMeals(): LiveData<List<Meal>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMeal(meal: Meal)

    @Query("DELETE FROM meals WHERE mealId = :id")
    suspend fun deleteMeal(id: Int)

    @Update
    suspend fun upMeal(meal: Meal)
}

