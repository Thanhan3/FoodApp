package com.example.foodapp.data.local.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.foodapp.data.Pojo.Meal

@Dao
interface MealDao {
    @Query("SELECT * FROM meals")
     fun getAllMeals(): LiveData<List<Meal>>
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMeal(meal: Meal)
    @Update
    suspend fun updateMeal(meal: Meal)
    @Delete
    suspend fun deleteMeal(meal: Meal)


}