package com.example.sf.database.fridge

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Keep
@Entity(tableName = "fridge")
data class Fridge(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var foodName: String = "",
    var foodDate: String = "",
    var foodQuantity: String = "",

    var proteins: Float?,
    var fats: Float?,
    var carbohydrates: Float?,
)

@Keep
@Entity(tableName = "meals")
data class Meal(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,

    var mealTime: String = "",
    var dish: String = "",
    var calorie: String? = "",

    var proteins: Float?,
    var fats: Float?,
    var carbohydrates: Float?,
)


