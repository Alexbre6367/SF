package com.example.sf.database.fridge

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Fridge::class, Meal::class], version = 10, exportSchema = false)
abstract class FridgeRoomDatabase: RoomDatabase() {
    abstract fun fridgeDao(): FridgeDAO
    abstract fun mealsDao(): MealsDAO

    companion object {
        @Volatile
        private var INSTANCE: FridgeRoomDatabase? = null


        fun getInstance(context: Context): FridgeRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    FridgeRoomDatabase::class.java,
                    "fridge_database"
                )
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}